import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collector;
import java.util.stream.IntStream;

enum Direction {
    UP, DOWN, IDLE;
}

enum Status {
    MOVING, IDLE, IN_MAINTAINANCE;
}

enum DoorOperation {
    CLOSE, OPEN;
}

interface FloorRequest {
}

class ExternalFloorRequest implements FloorRequest {
    int sourceFloor;
    Direction direction;

    public ExternalFloorRequest(int sourceFloor, Direction direction) {
        this.sourceFloor = sourceFloor;
        this.direction = direction;
    }
}

class InternalFloorRequest implements FloorRequest {
    int sourceFloor;
    int destinationFloor;
    Direction direction;

    public InternalFloorRequest(int sourceFloor, int destinationFloor, Direction direction) {
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
        this.direction = direction;
    }
}

interface Button {
    public void press();
}

class InternalFloorButton implements Button {
    private int floorNumber;

    public InternalFloorButton(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    @Override
    public void press() {
    }

    public void press(Elevator elevator, int sourceFloor) {
        System.out.printf("Creating Internal Request from floor %d to floor: %d %n", sourceFloor, floorNumber);
        elevator.createInternalFloorRequest(sourceFloor, floorNumber);
        System.out.printf("Created Internal Request from floor %d to floor: %d %n", sourceFloor, floorNumber);

    }

    public int getFloorNumber() {
        return floorNumber;
    }
}

class InternalOperationButton implements Button {
    private DoorOperation doorOperation;

    public InternalOperationButton(DoorOperation doorOperation) {
        this.doorOperation = doorOperation;
    }

    @Override
    public void press() {
        switch (doorOperation) {
            case DoorOperation.OPEN:
                System.out.println("Door is Open, waiting for 15 sec until close...");
                break;
            case DoorOperation.CLOSE:
                System.out.println("Closing door...");
                System.out.println("Door closed...");
                break;
        }
    }

    public DoorOperation getDoorOperationButton() {
        return doorOperation;
    }
}

class ExternalFloorButton implements Button {
    Direction direction;

    @Override
    public void press() {
        // Create External floor request
    }

    public void press(ElevatorSystemController elevatorSystemController, int sourceFloor, Direction direction) {
        System.out.printf("Creating External Request: sourceFloor %d, Direction %s %n", sourceFloor, direction.name());
        elevatorSystemController.createFloorRequest(sourceFloor, direction);
        System.out.printf("Created External Request: sourceFloor %d, Direction %s %n", sourceFloor, direction.name());

    }
}

class Elevator {
    int id;
    Map<Integer, InternalFloorButton> internalFloorButtons;
    // List<Button> externalFloorButtons;
    Map<DoorOperation, Button> internalOperationButtons;
    Status status;
    Direction direction;
    int floorNumber;
    PriorityQueue<FloorRequest> floorRequests;


    public Elevator(int id, int floor) {
        this.id = id;
        this.direction = Direction.IDLE;
        this.status = Status.IDLE;
        this.floorNumber = 0;
        internalFloorButtons = new HashMap<>();
        internalOperationButtons = new HashMap<>();
        floorRequests = new PriorityQueue<>();
        for(int i=0;i<floor;i++) {
            addFloorButton(i);
        }
        internalOperationButtons.put(DoorOperation.CLOSE, new InternalOperationButton(DoorOperation.CLOSE));
        internalOperationButtons.put(DoorOperation.OPEN, new InternalOperationButton(DoorOperation.OPEN));
    }

    public void addFloorButton(int floorNumber) {
        if(!internalFloorButtons.containsKey(floorNumber)) {
            internalFloorButtons.put(floorNumber, new InternalFloorButton(floorNumber));
        }
    }

    public void openDoor() {
        internalOperationButtons.get(DoorOperation.OPEN).press();
    }

    public void closeDoor() {
        internalOperationButtons.get(DoorOperation.CLOSE).press();
    }
    
    private void move(int floorNumber, Direction direction) {
        this.floorNumber = floorNumber;
        this.direction = direction;
        
    }

    public void createExternalFloorRequest(int sourceFloor, Direction direction) {
        this.move(sourceFloor, direction);
        this.direction = direction;
        this.status = Status.MOVING;
        System.out.printf("Moved to floor: %d, status: %s, direction: %s %n", this.floorNumber, this.status, this.direction);
    }

    public void createInternalFloorRequest(int sourceFloor, int destinationFloor) {
        this.move(destinationFloor, Direction.IDLE);
        this.direction = Direction.IDLE;
        this.status = Status.IDLE;
        System.out.printf("Moved to floor: %d, status: %s, direction: %s %n", this.floorNumber, this.status, this.direction);
    }
    
    public void addFloorRequest(FloorRequest floorRequest) {
        this.floorRequests.add(floorRequest);
    }

    public Map<Integer, InternalFloorButton> getInternalFloorButtons() {
        return internalFloorButtons;
    }

    @Override
    public String toString() {
        return "id : "+ id + ", status : " + status.name() + ", Direction : " + direction.name() + ", FloorNumber: " + floorNumber;
    }

}

class Floor {
    private int id; // floorNumber
    private Map<Direction, ExternalFloorButton> externalFloorButtons;

    public Floor(int id) {
        this.id = id;
        this.externalFloorButtons = new HashMap<>();
        this.externalFloorButtons.put(Direction.DOWN, new ExternalFloorButton());
        this.externalFloorButtons.put(Direction.UP, new ExternalFloorButton());
    }

    public Map<Direction, ExternalFloorButton> getExternalFloorButtons() {
        return externalFloorButtons;
    }

    public int getFloorNumber() {
        return id;
    }

    public int getId() {
        return id;
    }
}

class Building {
    private Map<Integer, Floor> floors;

    public Building(int floorNumber) {
        this.floors = new HashMap<>();
        IntStream.range(0, floorNumber+1).forEach(floor -> addFloor(floor));
    }

    public void addFloor(int floorNumber) {
        if(!this.floors.containsKey(floorNumber)) {
            this.floors.put(floorNumber, new Floor(floorNumber));
            System.out.printf("Floor %d added to building%n", floorNumber);
        }
    }

    public Map<Integer, Floor> getFloors() {
        return floors;
    }
}


class ElevatorSystemController {
    Map<Integer, Elevator> elevators = new HashMap<>();
    Building building;

    public ElevatorSystemController(int elevatorNumber, int floorNumber) {
        this.elevators = new HashMap<>();
        IntStream.rangeClosed(1, elevatorNumber).forEach(number -> addElevator(number, floorNumber));
        building = new Building(floorNumber);

    }
    

    public void addElevator(int id, int floorNumber) {
        if(!elevators.containsKey(id)) {
            elevators.put(id, new Elevator(id, floorNumber));
            System.out.printf("Elevator with id %d added.%n", id);
        } else {
            System.out.printf("Elevator with id %d exist.%n", id);
        }
    }

    public void createFloorRequest(int sourceFloor, Direction direction) {
        Elevator elevator = elevators.entrySet().stream().filter(entry -> entry.getValue().status.equals(Status.IDLE)).findAny().get().getValue();
        System.out.println("Selected ELevator: " + elevator.toString());
        elevator.createExternalFloorRequest(sourceFloor, direction);

    }

    public void createInternalElevatorRequest(int elevatorId, int sourceFloor, int destinationFloor) {

        elevators.get(elevatorId).getInternalFloorButtons().get(destinationFloor).press(elevators.get(elevatorId), sourceFloor);
    }

    @Override
    public String toString() {
        return "Elevator Length: " + elevators.size() + " " + "Buildings: "+ building.getFloors().size();
    }

    
}

class ElevatorConstants {
    static final int ELEVATOR_NUMBER = 10;
    static final int FLOOR_NUMBER = 20;
}

public class ElevatorSystem {

    public static void main(String[] args) {

        ElevatorSystemController elevatorSystemController = new ElevatorSystemController(ElevatorConstants.ELEVATOR_NUMBER, ElevatorConstants.FLOOR_NUMBER);
        // System.out.println(elevatorSystemController.toString());
        
        elevatorSystemController.building.getFloors().get(3).getExternalFloorButtons().get(Direction.DOWN).press(elevatorSystemController, 3, Direction.UP);
        elevatorSystemController.building.getFloors().get(5).getExternalFloorButtons().get(Direction.DOWN).press(elevatorSystemController, 5, Direction.DOWN);

        elevatorSystemController.createInternalElevatorRequest(1, 3, 10);

        elevatorSystemController.createInternalElevatorRequest(2, 5, 1);


        
    }
}