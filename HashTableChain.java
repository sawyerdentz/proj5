import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


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


        public String toString() {
            return key.toString() + "=" + value.toString();
        }
    }


    // hash table data
    private LinkedList<Entry<K,V>>[] table;
    private int numKeys = 0;
    private static final int CAPACITY = 101;
    private static final double LOAD_THRESHOLD = 1;


    // constructors
    public HashTableChain() {
        table = new LinkedList[CAPACITY];
        numKeys = 0;
    }


    public HashTableChain(int cap) {
        table = new LinkedList[cap];
        numKeys = 0;
    }


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
        table[index].add(new Entry<K,V>(key, value));
        numKeys += 1;
        if (numKeys / CAPACITY > LOAD_THRESHOLD) {
            // rehash
            rehash();
        }
        return null;
        }


        public boolean isEmpty() {
           
            return false;


        }

        public void rehash() {

        }

        
       


    }
