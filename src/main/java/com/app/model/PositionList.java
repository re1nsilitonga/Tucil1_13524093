package com.app.model;

public class PositionList {
    private int[][] data;
    private int size;
    private int capacity;
    
    private static final int INITIAL_CAPACITY = 26;
    
    public PositionList() {
        this.capacity = INITIAL_CAPACITY;
        this.data = new int[capacity][2];
        this.size = 0;
    }
    
    public PositionList(int initialCapacity) {
        this.capacity = initialCapacity;
        this.data = new int[capacity][2];
        this.size = 0;
    }
    
    public void add(int row, int col) {
        if (size >= capacity) {
            resize();
        }
        data[size][0] = row;
        data[size][1] = col;
        size++;
    }
    
    public int[] get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return data[index];
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public void clear() {
        size = 0;
    }
    
    private void resize() {
        int newCapacity = capacity * 2;
        int[][] newData = new int[newCapacity][2];
        
        for (int i = 0; i < size; i++) {
            newData[i][0] = data[i][0];
            newData[i][1] = data[i][1];
        }
        
        data = newData;
        capacity = newCapacity;
    }
}
