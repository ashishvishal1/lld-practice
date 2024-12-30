import java.util.Map;
import java.util.Optional;
import java.util.HashMap;


class DoublyLinkedListDS {
    String key;
    String val;
    DoublyLinkedListDS next;
    DoublyLinkedListDS prev;

    public DoublyLinkedListDS(String key, String val) {
        this.key = key;
        this.val = val;
        this.next = null;
        this.prev = next;
    }
}

class DoublyLinkedList {
    DoublyLinkedListDS head;
    DoublyLinkedListDS tail;
    int size;
    public DoublyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public DoublyLinkedListDS insertNodeToEnd(String key, String val) {
        DoublyLinkedListDS node = new DoublyLinkedListDS(key, val);
        if(head == null) {
            handleHeadNullDuringInsertion(node);
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
        return node;
    }

    public DoublyLinkedListDS insertNodeToStart(String key, String val) {
        DoublyLinkedListDS node = new DoublyLinkedListDS(key, val);
        if(head == null) {
            handleHeadNullDuringInsertion(node);
        } else {
            head.prev = node;
            node.next = head;
            head = node;
        }
        size++;
        return node;
    }

    private void handleHeadNullDuringInsertion(DoublyLinkedListDS node) {
        head = node;
        tail = node;
    }

    public Optional<String> removeLastNode() {
        Optional<String> removedkey = Optional.empty();
        if(head == null || tail == null) {
            System.out.println("Head or Tail is null, Operation cant be performed");
            return Optional.empty();
        } else if(head==tail) {
            removedkey = Optional.ofNullable(tail.key);
            head = null;
            tail = null;
        } else {
            removedkey = Optional.ofNullable(tail.key);
            DoublyLinkedListDS oldTail = tail;
            tail = tail.prev;
            tail.next = null;
            oldTail = null;
        }
        size--;
        return removedkey;
    }

    public void removeNode(DoublyLinkedListDS node) {
        if(node == null) {
            System.out.println("Node can't be null");
            return;
        }
        System.out.printf("Removal of node: %s started%n", node);
        if(head == null || tail == null) {
            System.out.println("Head or Tail is null, Operation cant be performed");
            return ;
        } else if(head==tail) {
            head = null;
            tail = null;
            size--;
        } else if(node==head) {
            head = head.next;
            head.prev = null;
            node = null;
            size--;
        } else if(node == tail) {
            removeLastNode();
        } else {
            DoublyLinkedListDS prevNode = node.prev;
            DoublyLinkedListDS nextNode = node.next;
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
            node = null;
            size--;
        }
         
        System.out.printf("Removal of node: %s completed%n", node);
    }
}



interface CachingAlgorithm {
    public void put(String key, String val);
    public String get(String key) throws Exception;
    public void evict();
    public void printCacheSize();
}

class LRUCachingAlgorithm implements CachingAlgorithm {

    private int cacheSize;
    private DoublyLinkedList cacheDoublyLinkedListStorage;
    private Map<String, DoublyLinkedListDS> cacheDoublyLinkedListStorageMapping;

    public LRUCachingAlgorithm(int cacheSize) {
        this.cacheSize = cacheSize;
        this.cacheDoublyLinkedListStorage = new DoublyLinkedList();
        cacheDoublyLinkedListStorageMapping = new HashMap<>();
    }

    public void put(String key, String val) {
        System.out.printf("Inserting key: %s, value: %s%n", key, val);
        if(cacheDoublyLinkedListStorageMapping.containsKey(key)) {
            cacheDoublyLinkedListStorageMapping.get(key).val = val;
            return;
        }
        DoublyLinkedListDS keyValuePair = cacheDoublyLinkedListStorage.insertNodeToStart(key, val);
        cacheDoublyLinkedListStorageMapping.put(key, keyValuePair);
        evict();
        System.out.printf("Inserted key: %s, value: %s%n", key, val);

    }

    public String get(String key) throws Exception {
        if(cacheDoublyLinkedListStorageMapping.containsKey(key)) {
            // insert at beginning of DLL storage and update mapping
            String val = cacheDoublyLinkedListStorageMapping.get(key).val;
            // delete the DLL storage
            cacheDoublyLinkedListStorage.removeNode(cacheDoublyLinkedListStorageMapping.get(key));
            cacheDoublyLinkedListStorageMapping.remove(key);
            put(key, val);
            
            return cacheDoublyLinkedListStorageMapping.get(key).val;
        }
        throw new Exception("Key Not Found, I can load from Database but unfortunately for you and fortunately this is an excercise.");
    }

    public void evict() {
        System.out.println("Eviction checking started...");
        while(cacheDoublyLinkedListStorage.size>cacheSize) {
            // remove from DLL storage
            Optional<String> key = cacheDoublyLinkedListStorage.removeLastNode();
            // remove from mapping
            if(key.isPresent()) {
                System.out.printf("Evicting key: %s%n", key.get());
                cacheDoublyLinkedListStorageMapping.remove(key.get());
            }
        }
        System.out.println("Eviction completed...");
    }

    public void printCacheSize() {
        System.out.printf("filled cache Size: %d %n", cacheDoublyLinkedListStorage.size);
    }

}

class CachingAlgorithmFactory {
    private CachingAlgorithmFactory(){}

    public static CachingAlgorithm getCachingAlgorithmFactory(CachingAlgorithmType cachingAlgorithmType, int cacheSize) {
        switch(cachingAlgorithmType) {
            case CachingAlgorithmType.LRU:
                return new LRUCachingAlgorithm(cacheSize);
            default:
                return new LRUCachingAlgorithm(cacheSize); 
        }
    }
}

enum CachingAlgorithmType {
    LRU;
}

public class LRUCache {
    public static void main(String[] args) {
        System.out.println("Starting caching service...");


        final int CACHE_SIZE = 5;
        CachingAlgorithm lruCachingAlgorithm = CachingAlgorithmFactory.getCachingAlgorithmFactory(CachingAlgorithmType.LRU, CACHE_SIZE);
        lruCachingAlgorithm.put("a", "Apple");
        lruCachingAlgorithm.printCacheSize();

        lruCachingAlgorithm.put("b", "Boy");lruCachingAlgorithm.printCacheSize();
        lruCachingAlgorithm.put("c", "Cat");lruCachingAlgorithm.printCacheSize();
        lruCachingAlgorithm.put("d", "Dog");lruCachingAlgorithm.printCacheSize();
        lruCachingAlgorithm.put("e", "Elephant");lruCachingAlgorithm.printCacheSize();

        try {
            System.out.println(lruCachingAlgorithm.get("a"));
            lruCachingAlgorithm.printCacheSize();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            System.out.println(lruCachingAlgorithm.get("b"));
            lruCachingAlgorithm.printCacheSize();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            System.out.println(lruCachingAlgorithm.get("c"));
            lruCachingAlgorithm.printCacheSize();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            System.out.println(lruCachingAlgorithm.get("d"));
            lruCachingAlgorithm.printCacheSize();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            System.out.println(lruCachingAlgorithm.get("e"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            System.out.println(lruCachingAlgorithm.get("f"));
            lruCachingAlgorithm.printCacheSize();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        lruCachingAlgorithm.put("f", "Fan");
        lruCachingAlgorithm.printCacheSize();
        lruCachingAlgorithm.put("b", "Ball");
        lruCachingAlgorithm.printCacheSize();
        try {
            System.out.println(lruCachingAlgorithm.get("f"));
            lruCachingAlgorithm.printCacheSize();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            System.out.println(lruCachingAlgorithm.get("a"));
            lruCachingAlgorithm.printCacheSize();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            System.out.println(lruCachingAlgorithm.get("b"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Finishing caching service...");


    }
}