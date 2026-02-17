package com.app.model;

public class ColorMap {
    
    private static final int DEFAULT_CAPACITY = 101;
    private static final float LOAD_FACTOR = 0.7f;
    
    private MapEntry[] data;
    private int capacity;
    private int size;
    
    private static class MapEntry {
        char key;
        PositionList value;
        boolean occupied;
        
        MapEntry() {
            this.occupied = false;
        }
        
        MapEntry(char key, PositionList value) {
            this.key = key;
            this.value = value;
            this.occupied = true;
        }
    }
    
    public ColorMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.data = new MapEntry[capacity];
        this.size = 0;
        
        for (int i = 0; i < capacity; i++) {
            data[i] = new MapEntry();
        }
    }
    
    public ColorMap(int initialCapacity) {
        this.capacity = nextPrime(initialCapacity);
        this.data = new MapEntry[capacity];
        this.size = 0;
        
        for (int i = 0; i < capacity; i++) {
            data[i] = new MapEntry();
        }
    }
    
    private int hash(char key) {
        return Math.abs(key) % capacity;
    }
    
    public void put(char key, PositionList value) {
        if ((float) size / capacity > LOAD_FACTOR) {
            resize();
        }
        
        int idx = hash(key);
        int startIdx = idx;
        
        while (data[idx].occupied && data[idx].key != key) {
            idx = (idx + 1) % capacity;
            if (idx == startIdx) {
                resize();
                put(key, value);
                return;
            }
        }
        
        if (!data[idx].occupied) {
            size++;
        }
        
        data[idx] = new MapEntry(key, value);
    }
    
    public PositionList get(char key) {
        int idx = hash(key);
        int startIdx = idx;
        
        while (data[idx].occupied) {
            if (data[idx].key == key) {
                return data[idx].value;
            }
            idx = (idx + 1) % capacity;
            if (idx == startIdx) {
                break;
            }
        }
        
        return null;
    }
    
    public boolean containsKey(char key) {
        return get(key) != null;
    }
    
    public PositionList getOrCreate(char key) {
        PositionList existing = get(key);
        if (existing != null) {
            return existing;
        }
        
        PositionList newList = new PositionList();
        put(key, newList);
        return newList;
    }
    
    public char[] keySet() {
        char[] keys = new char[size];
        int count = 0;
        
        for (int i = 0; i < capacity; i++) {
            if (data[i].occupied) {
                keys[count++] = data[i].key;
            }
        }
        
        return keys;
    }
    
    public int size() {
        return size;
    }
    
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            data[i] = new MapEntry();
        }
        size = 0;
    }
    
    private void resize() {
        int newCapacity = nextPrime(capacity * 2);
        MapEntry[] newData = new MapEntry[newCapacity];
        
        for (int i = 0; i < newCapacity; i++) {
            newData[i] = new MapEntry();
        }
        
        for (int i = 0; i < capacity; i++) {
            if (data[i].occupied) {
                int newIdx = Math.abs(data[i].key) % newCapacity;
                while (newData[newIdx].occupied) {
                    newIdx = (newIdx + 1) % newCapacity;
                }
                newData[newIdx] = data[i];
            }
        }
        
        data = newData;
        capacity = newCapacity;
    }
    
    private int nextPrime(int n) {
        while (!isPrime(n)) {
            n++;
        }
        return n;
    }
    
    private boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
}
