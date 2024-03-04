package ru.aston_hw;


import java.util.LinkedList;

public class MyHashMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static class Node<K, V> {
        private final K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
    private LinkedList<Node<K, V>>[] bins;

    private int size;

    private final float loadFactor;

    private int capacity;

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Вместимость не может быть отрицательной");
        this.capacity = initialCapacity;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.bins = new LinkedList[capacity];
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Вместимость не может быть отрицательной");
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Неподходящий load factor: " + loadFactor);
        this.loadFactor = loadFactor;
        this.capacity = initialCapacity;
        this.bins = new LinkedList[capacity];
    }

    public void put(K key, V value) {
        assertNotNull(key);
        int index = getIndex(key);
        if (bins[index] == null)
            bins[index] = new LinkedList<>();

        for (Node<K, V> node : bins[index]) {
            if (node.getKey().equals(key)) {
                node.setValue(value);
                return;
            }
        }
        bins[index].add(new Node<>(key, value));
        size++;
        if ((double) size / capacity > loadFactor)
            resize();
    }

    public V get(K key) {
        assertNotNull(key);
        int index = getIndex(key);

        if (bins[index] != null) {
            for (Node<K, V> node : bins[index]) {
                if (node.getKey().equals(key))
                    return node.getValue();

            }
        }
        return null;
    }

    public V remove(K key) {
        assertNotNull(key);
        int index = getIndex(key);

        if (bins[index] != null) {
            for (Node<K, V> node : bins[index]) {
                if (node.getKey().equals(key)) {
                    V value = node.getValue();
                    bins[index].remove(node);
                    size--;
                    return value;
                }
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public int getSize(){
        return size;
    }

    public float getLoadFactor() {
        return loadFactor;
    }

    public int getCapacity() {
        return capacity;
    }

    private int getHash(K key) {
        return key.hashCode();
    }

    private int getIndex(K key) {
        return key.hashCode() % capacity;
    }

    private void assertNotNull(K key) {
        if (key == null)
            throw new IllegalArgumentException("Key cannot be null");
    }

    private void resize() {
        capacity *= 2;
        LinkedList<Node<K, V>>[] newBins = new LinkedList[capacity];

        for (LinkedList<Node<K, V>> bin : bins) {
            if (bin != null) {
                for (Node<K, V> node : bin) {
                    int newIndex = getIndex(node.getKey());
                    if (newBins[newIndex] == null) {
                        newBins[newIndex] = new LinkedList<>();
                    }
                    newBins[newIndex].add(node);
                }
            }
        }
        bins = newBins;
    }
}