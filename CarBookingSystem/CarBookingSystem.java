import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


class Location {
    double x;
    double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Location x: "+x +" , y: "+ y;
    }
}

abstract class User {
    private String id;
    private String name;
    private Location location;

    public User(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

class Rider extends User {
    public Rider(String id, String name, Location location) {
        super(id, name, location);
    }
}

class Driver extends User {
    private String drivingLicense;
    private Boolean isAvailable;
    public Driver(String id, String name, Location location, String drivingLicense, Boolean isAvailable) {
        super(id, name, location);
        this.drivingLicense = drivingLicense;
        this.isAvailable = isAvailable;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}


abstract class Vehicle {
    private String id;
    private String vehicleNumber;

    public Vehicle(String id, String vehicleNumber) {
        this.id = id;
        this.vehicleNumber = vehicleNumber;
    }
}

class FourWheelerVehicle extends Vehicle {
    public FourWheelerVehicle(String id, String vehicleNumber) {
        super(id, vehicleNumber);
    }
}

enum BookingStatus {
    INITIATED, STARTED, INPROGRESS, END, CANCELLED;
}

class RideBooking {
    String id;
    String riderId;
    String driverId;
    String carId;
    Location source;
    Location destination;
    Date journeyStart;
    Date journeyEnd;
    BookingStatus status;

    public RideBooking(String id, String riderId, String driverId, String carId, Location source, Location destination) {
        this.id = id;
        this.riderId = riderId;
        this.driverId = driverId;
        this.carId = carId;
        this.source = source;
        this.destination = destination;
        this.setStatus(BookingStatus.INITIATED);
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setJourneyEnd(Date journeyEnd) {
        this.journeyEnd = journeyEnd;
    }
    public void setJourneyStart(Date journeyStart) {
        this.journeyStart = journeyStart;
    }

    public Long getDurationInSeconds() {
        if(journeyEnd == null || journeyStart == null) {
            return null;
        } 
        return (journeyEnd.getTime() - journeyStart.getTime())/1000;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Ride Info:  id: "+id + " Rider Id: "+ riderId + " driverId: "+ driverId + " BookingStatus: "+ status.name();
    }

}




class CarBookingSystemController {

    Map<String, Rider> riders = new HashMap<>();
    Map<String, Driver> drivers = new HashMap<>();
    Map<String, Vehicle> cars = new HashMap<>();
    Map<String, RideBooking> rideBookings = new HashMap<>();
    Map<String, List<String> > ridersBookingsMap = new HashMap<>();
    Map<String, List<String> > driversBookingsMap = new HashMap<>();

    public void createRider(String id, String name, Location location) {
        riders.put(id, new Rider(id, name, location));
        ridersBookingsMap.put(id, new ArrayList<>());

    }

    public void createDriver(String id, String name, Location location, String drivingLicense, boolean isAvailable) {
        drivers.put(id, new Driver(id, name, location, drivingLicense, isAvailable));
        driversBookingsMap.put(id, new ArrayList<>());
    }

    public void createCar(String id, String numberPlate) {
        cars.put(id, new FourWheelerVehicle(id, numberPlate));
    }

    public void createBooking(String id, String riderId, String driverId, String carId, Location sourceLocation, Location destination) throws Exception {
        if(!riders.containsKey(riderId)) {
            throw new Exception("rider " + riderId+ " not reistered");
        }

        if(!drivers.containsKey(driverId)) {
            throw new Exception("Driver " + driverId+ " not reistered");   
        }

        Driver driver = drivers.get(driverId);
        if(!driver.getIsAvailable()) {
            throw new Exception("Driver " + driverId+ " is offline");   
        }

        if(!cars.containsKey(carId)) {
            throw new Exception("Car " + carId+ " not reistered");   

        }


        RideBooking rideBooking = new RideBooking(id, riderId, driverId, carId, sourceLocation, destination);
        rideBookings.put(id, rideBooking);
        List<String> updatedRidersBooking = ridersBookingsMap.get(riderId);
        updatedRidersBooking.add(id);

        List<String> updatedDriverBooking = driversBookingsMap.get(driverId);
        updatedDriverBooking.add(id);

        ridersBookingsMap.put(riderId, updatedRidersBooking);
        driversBookingsMap.put(driverId, updatedDriverBooking);
    }

    public void cancelBooking(String id) {
        RideBooking rideBooking = rideBookings.get(id);
        rideBooking.setStatus(BookingStatus.CANCELLED);
        rideBooking.setJourneyStart(new Date());
        rideBooking.setJourneyEnd(new Date());
        rideBookings.put(id, rideBooking);
    }

    public void startBooking(String id) {
        RideBooking rideBooking = rideBookings.get(id);
        rideBooking.setStatus(BookingStatus.STARTED);
        rideBooking.setJourneyStart(new Date());
        rideBookings.put(id, rideBooking);
    }

    public void endBooking(String id) {
        RideBooking rideBooking = rideBookings.get(id);
        rideBooking.setStatus(BookingStatus.END);
        rideBooking.setJourneyEnd(new Date());
        rideBookings.put(id, rideBooking);
    }

    public List<RideBooking> getRiderBookingHistory(String userId) {
        List<String> rideBookingIds = ridersBookingsMap.get(userId);
        return rideBookingIds.stream().map(id -> rideBookings.get(id)).collect(Collectors.toList());
    }

    public List<RideBooking> getDriverBookingHistory(String userId) {
        List<String> rideBookingIds = driversBookingsMap.get(userId);
        return rideBookingIds.stream().map(id -> rideBookings.get(id)).collect(Collectors.toList());
    }

}

public class CarBookingSystem {
    public static void main(String[] args) {
        CarBookingSystemController carBookingSystemController = new CarBookingSystemController();
        carBookingSystemController.createRider("r1", "rider1", new Location(0, 0));
        carBookingSystemController.createRider("r2", "rider2", new Location(0, 0));
        carBookingSystemController.createRider("r3", "rider3", new Location(0, 0));

        carBookingSystemController.createDriver("d1", "driver1", new Location(0, 0), "D1", false);
        carBookingSystemController.createDriver("d2", "driver2", new Location(0, 0), "D2", true);
        carBookingSystemController.createDriver("d3", "driver3", new Location(0, 0), "D3", true);

        carBookingSystemController.createCar("c1", "C1");
        carBookingSystemController.createCar("c2", "C2");
        carBookingSystemController.createCar("c3", "C3");


        System.out.println(carBookingSystemController.getRiderBookingHistory("r1").toString());
        try {
            carBookingSystemController.createBooking("b1", "r1", "d1", "c1", new Location(0, 0), new Location(0, 0));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(carBookingSystemController.getRiderBookingHistory("r1").toString());
        try {
            carBookingSystemController.createBooking("b2", "r1", "d2", "c1", new Location(0, 0), new Location(0, 0));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(carBookingSystemController.getRiderBookingHistory("r1").toString());

        

    }
}
