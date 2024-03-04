package ru.aston_hw;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public class MyArrayList<E> {

    private Object[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public MyArrayList() {
        this.elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public MyArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Вместимость ArrayList не может быть меньше 0");
        }
        this.elements = new Object[initialCapacity];
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(E e) {
        if (size == elements.length)
            elements = grow(size + 1);
        elements[size++] = e;
    }

    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Индекс %d находится вне ArrayList".formatted(index));
        return (E) elements[index];
    }

    public E first(){
        return (E) elements[0];
    }

    public E last(){
        return (E) elements[size - 1];
    }

    private Object[] grow(int minCapacity) {
        int currentCapacity = elements.length;
        if (minCapacity > currentCapacity) {
            int newCapacity = Math.max(currentCapacity * 2, minCapacity);
            return Arrays.copyOf(elements, newCapacity);
        }
        return elements;
    }
}