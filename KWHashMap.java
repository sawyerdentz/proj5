
public interface KWHashMap<K, V> {
    V get(Object key);

    boolean isEmpty();

    V put(K key, V Value);

    V remove(Object key);

    int size();

    String toString();

    void rehash();
}
