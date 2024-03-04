package ru.aston_hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyHashMapTest {

    private MyHashMap<Object,Object> sut;

    @BeforeEach
    void init() {
        sut = new MyHashMap<>();
    }

    @Test
    void constructor() {
        assertEquals(0, sut.getSize());
        assertEquals(16, sut.getCapacity());
        assertEquals(0.75f, sut.getLoadFactor());
    }

    @Test
    void constructorWithInitialCapacity() {
        int initialCapacity = 3;
        MyHashMap<Object,Object> newSut = new MyHashMap<>(initialCapacity);

        assertEquals(0, newSut.getSize());
        assertEquals(initialCapacity, newSut.getCapacity());
        assertEquals(0.75f, newSut.getLoadFactor());
    }

    @Test
    void constructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 3;
        float loadFactor = 0.6f;
        MyHashMap<Object,Object> newSut = new MyHashMap<>(initialCapacity, loadFactor);

        assertEquals(0, newSut.getSize());
        assertEquals(initialCapacity, newSut.getCapacity());
        assertEquals(loadFactor, newSut.getLoadFactor());
    }

    @Test
    void constructorWithNegativeInitialCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new MyHashMap<>(-1));
    }

    @Test
    void constructorWithNegativeLoadFactor() {
        assertThrows(IllegalArgumentException.class, () -> new MyHashMap<>(1, -0.5f));
    }

    @Test
    void constructorWithNaNLoadFactor() {
        assertThrows(IllegalArgumentException.class, () -> new MyHashMap<>(10, Float.NaN));
    }

    @Test
    void putAndGet() {
        sut.put(1, "Aston");
        sut.put(2, "Two");

        assertEquals("Aston", sut.get(1));
        assertEquals("Two", sut.get(2));
        assertNull(sut.get(3));
    }

    @Test
    void containsKey() {
        sut.put("Aston", 1);
        assertTrue(sut.containsKey("Aston"));
        assertFalse(sut.containsKey("ASTON"));
    }

    @Test
    void remove() {
        sut.put("One", 1);
        sut.put(1, "One");

        assertEquals(1, sut.remove("One"));
        assertNull(sut.get("One"));
        assertEquals("One", sut.get(1));
        assertNull(sut.remove("Aston"));
    }

    @Test
    void putWithResize() {
        MyHashMap<Object,Object> newSut = new MyHashMap<>(2);
        newSut.put(1, "One");
        newSut.put(2, "Two");
        newSut.put(3, "Three");

        assertEquals(3, newSut.getSize());
        assertEquals("One", newSut.get(1));
        assertEquals("Two", newSut.get(2));
        assertEquals("Three", newSut.get(3));
    }

    @Test
    void nullKeyInput() {
        assertThrows(IllegalArgumentException.class, () -> sut.put(null, "Aston"));
        assertThrows(IllegalArgumentException.class, () -> sut.containsKey(null));
        assertThrows(IllegalArgumentException.class, () -> sut.remove(null));
    }
}