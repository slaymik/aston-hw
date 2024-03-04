package ru.aston_hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyArrayListTest {

    private MyArrayList<Object> sut;

    @BeforeEach
    void init() {
        sut = new MyArrayList<>();
    }

    @Test
    void constructor() {
        assertEquals(0, sut.size());
        assertTrue(sut.isEmpty());
    }

    @Test
    void constructorWithNegativeCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new MyArrayList<>(-1));
    }

    @Test
    void get() {
        sut.add("Aston");

        assertEquals("Aston", sut.get(0));
    }

    @Test
    void getWithIndexOutOfBounds() {
        sut.add(1);

        assertThrows(IndexOutOfBoundsException.class, () -> sut.get(1));
    }

    @Test
    void add() {
        sut.add(1);

        assertEquals(1, sut.size());
        assertTrue(!sut.isEmpty());
        assertEquals(1, sut.get(0));
    }

    @Test
    void addMultipleElements() {
        sut.add(1);
        sut.add("Aston");
        sut.add('1');

        assertEquals(3, sut.size());
        assertEquals(1, sut.get(0));
        assertEquals("Aston", sut.get(1));
        assertEquals('1', sut.get(2));
    }

    @Test
    void first(){
        sut.add(1);
        sut.add("Aston");

        assertEquals(1,sut.first());
    }

    @Test
    void last(){
        sut.add(1);
        sut.add("Aston");

        assertEquals("Aston", sut.last());
    }

}