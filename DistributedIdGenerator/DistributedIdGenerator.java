import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;



class SequenceIdGeneratorConstants {
    static final int SIGNED_BIT = 1;
    static final int TIMESTAMP_BIT = 41;
    static final int DATACENTER_BIT = 5;
    static final int SERVER_BIT = 7;
    static final int SEQUENCE_BIT = 10;

}

enum IdType {
    RANDOM, SEQUENCE;
}

interface IdGenerator {
    public String generateId() throws Exception;
}

class RandomIdGenerator implements IdGenerator {
    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}

class SnowflakeSequenceIdGenerator implements IdGenerator {
    private static final long EPOCH_OFFSET = 1546300800000L; // January 1, 2019 00:00:00 UTC
    private static SnowflakeSequenceIdGenerator snowflakeSequenceIdGenerator;
    private volatile long lastTimeStamp = -1L;
    private volatile long currentSequence = -1L;
    private long dataCenter;
    private long serverNumber;
    private final long MAX_DATACENTER = (1<<SequenceIdGeneratorConstants.DATACENTER_BIT )-1;
    private final long MAX_SERVER = (1<<SequenceIdGeneratorConstants.SERVER_BIT) -1;
    private final long MAX_SEQUENCE = (1<<SequenceIdGeneratorConstants.SEQUENCE_BIT)-1;

    
    private final Object lock = new Object();

    private SnowflakeSequenceIdGenerator(int dataCenter, int serverNumber) throws Exception {
        verifyDatacenter(dataCenter);
        verifyServer(serverNumber);
        this.dataCenter = dataCenter;
        this.serverNumber = serverNumber;
    }

    

    private void verifyDatacenter(int dataCenter) throws Exception {
        if(dataCenter>this.MAX_DATACENTER) {
            throw new Exception("Max allowed data center is " + this.MAX_DATACENTER);
        }
    }

    private void verifyServer(int serverNumber) throws Exception {
        if(serverNumber>this.MAX_SERVER) {
            throw new Exception("Max allowed server is " + this.MAX_SERVER);
        }
    }

    public static SnowflakeSequenceIdGenerator getInstance(int dataCenter, int serverNumber) throws Exception{
        if(snowflakeSequenceIdGenerator == null) {
            snowflakeSequenceIdGenerator = new SnowflakeSequenceIdGenerator(dataCenter, serverNumber);
        }
        return snowflakeSequenceIdGenerator;
    }

    @Override
    public String generateId() throws Exception {
        synchronized (lock) {
            long currentTimeStamp = getTimeStamp();
            if (currentTimeStamp < lastTimeStamp) {
                throw new Exception("Clock moved back");
            }
            if (currentTimeStamp == lastTimeStamp) {
                currentSequence = (currentSequence + 1) & MAX_SEQUENCE;
                if (currentSequence != 0) {
                    currentTimeStamp = waitNextMillis(currentTimeStamp);
                }
            } else {
                currentSequence = 0; 
            }
            lastTimeStamp = currentTimeStamp;
            long id = currentTimeStamp << (SequenceIdGeneratorConstants.DATACENTER_BIT+ SequenceIdGeneratorConstants.SERVER_BIT + SequenceIdGeneratorConstants.SEQUENCE_BIT);
            id |= (dataCenter << (SequenceIdGeneratorConstants.SERVER_BIT + SequenceIdGeneratorConstants.SEQUENCE_BIT));
            id |= (serverNumber << SequenceIdGeneratorConstants.SEQUENCE_BIT);
            id |= currentSequence;
           
            return Long.toBinaryString(id);
        }
    }

    private long waitNextMillis(long timestamp){
        long currentTime = getTimeStamp();
        while(currentTime == timestamp) {
            currentTime = getTimeStamp();
        }
        return currentTime;
    }

    private long getTimeStamp() {
        return System.currentTimeMillis() - EPOCH_OFFSET;
    }
}

class IdGeneratorFactory {
    private IdGeneratorFactory() {

    }

    public static IdGenerator getIdGenerator(IdType idType, Optional<Integer> datacenter, Optional<Integer> server) throws Exception{
        switch (idType) {
            case IdType.RANDOM:
                return new RandomIdGenerator();
            case IdType.SEQUENCE:
                return SnowflakeSequenceIdGenerator.getInstance(datacenter.get(), server.get());
        
            default:
                throw new Exception("IdType is Invalid: " + idType.name());
        }
    }
}

class DistributedIdGeneratorController {
    
    public static String generateId(IdType idType, Optional<Integer> datacenter, Optional<Integer> server) {
        try {
            return IdGeneratorFactory.getIdGenerator(idType, datacenter, server).generateId();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}


public class DistributedIdGenerator {
    public static void main(String args[]) throws Exception {
        for(int i=0;i<10;i++) {
            System.out.println("RANDOM ID " + i +":" + DistributedIdGeneratorController.generateId(IdType.RANDOM, Optional.empty(), Optional.empty()));
        }

        Map<String, Boolean> alreadyMappedId = new HashMap<>();

        for(int i=0;i<100000;i++) {
            String id = DistributedIdGeneratorController.generateId(IdType.SEQUENCE, Optional.of(5), Optional.of(10));
            if(alreadyMappedId.containsKey(id)) {
                throw new Exception("Id Already exist, i: "+i+ " id: " + id);
            }
            alreadyMappedId.put(id, true);

        }
    }
}