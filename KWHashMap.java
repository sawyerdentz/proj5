/*
This program is an interface for a chained hash table designed to be used to compress files. It provides functions necessary for this process.
Created by Sawyer Dentz and Cavin Nguyen
Last modified 5/1/2026
*/

public interface KWHashMap<K, V> {
    V get(Object key);

    boolean isEmpty();

    V put(K key, V Value);

    V remove(Object key);

    int size();

    @Override
    String toString();

    void rehash();
}
