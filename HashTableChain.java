import java.math.BigInteger;
import java.util.LinkedList;


public class HashTableChain<K,V> implements KWHashMap<K,V> {


    // internal class defines an entry in the hash table chain
    public static class Entry<K,V> {
        private final K key;
        private V value;
       
        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }


        public K getKey() {
            return key;
        }


        public V getValue() {
            return value;
        }


        public V setValue(V val) {
            V oldVal = value;
            value = val;
            return oldVal;
        }


        @Override
        public String toString() {
            return key.toString() + "=" + value.toString();
        }
    }


    // hash table data
    private LinkedList<Entry<K,V>>[] table;
    private int numKeys = 0;
    private static int capacity = 101;
    // load_factor = num_keys/capacity
    private static final double LOAD_THRESHOLD = 0.75;


    // constructors
    @SuppressWarnings("unchecked")
    public HashTableChain() {
        table = new LinkedList[capacity];
        numKeys = 0;
    }


    @SuppressWarnings("unchecked")
    public HashTableChain(int cap) {
        capacity = cap;
        table = new LinkedList[cap];
        numKeys = 0;
    }


    @Override
    public void rehash() {
        // double the capacity to lower load factor and find next prime to prevent collisions
        BigInteger number = BigInteger.valueOf(capacity * 2);
        int newCapacity = number.nextProbablePrime().intValue();
        @SuppressWarnings("unchecked")
        LinkedList<Entry<K,V>>[] newTable = new LinkedList[newCapacity];
        // loop through old table and rehash entries into new table
        for (LinkedList<Entry<K,V>> table_bin : table) {
            if (table_bin != null) {
                for (Entry<K,V> nextEntry : table_bin) {
                    int newIndex = Math.abs(nextEntry.key.hashCode() % newCapacity);


                    if (newTable[newIndex] == null) {
                        newTable[newIndex] = new LinkedList<>();
                    }


                    newTable[newIndex].add(nextEntry);
                }
            }
        }
        // need to use a set table method
        table = newTable;
        capacity = newCapacity;
    }


    @Override
    public V get(Object key) {
        // get index in hash table
        int index = key.hashCode() % table.length;
        if (index < 0) {
            index += table.length;
        }
        // if the list is empty, return null
        if (table[index] == null) {
            return null;
        }
        // traverse linked list for value
        for (Entry<K,V> nextEntry : table[index]) {
            if (nextEntry.getKey().equals(key)) {
                return nextEntry.getValue();
            }
        }
        return null;
    }


    @Override
    public V put(K key, V value) {
        // get index in hash table
        int index = key.hashCode() % table.length;
        if (index < 0) {
            index += table.length;
        }


        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }
        // traverse linked list for value
        for (Entry<K,V> nextEntry : table[index]) {
            // if the entry already exists, overwrite. Else add new entry
            if (nextEntry.getKey().equals(key)) {
                V old_val = nextEntry.getValue();
                nextEntry.setValue(value);
                return old_val;
            }
        }
        // key not found, so insert new key-value pair
        table[index].add(new Entry<>(key, value));
        numKeys += 1;
        if (((float)numKeys/(float)capacity) > LOAD_THRESHOLD) {
            rehash();
        }
        return null;
    }


    @Override
    public V remove(Object key) {
        // get index in hash table
        int index = key.hashCode() % table.length;
        if (index < 0) {
            index += table.length;
        }


        if (table[index] == null) {
            return null;
        }


        for (Entry<K,V> nextEntry : table[index]) {
            if (nextEntry.getKey().equals(key)) {
                V old_val = nextEntry.getValue();
                nextEntry.setValue(null);
                return old_val;
            }
        }
        return null;
    }


    @Override
    public int size() {
        return capacity;


    }


    @Override
    public boolean isEmpty() {
        return (numKeys == 0);
    }


    }


