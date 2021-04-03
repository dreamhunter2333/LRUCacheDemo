package com.dreamhunter.lru;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUSimple<K, V> extends LinkedHashMap<K, V> {

    private int cacheSize;

    public LRUSimple(int cacheSize) {
        super(16, (float) 0.75, true);
        this.cacheSize = cacheSize;
    }

    public static void main(String[] args) {
        LRUSimple<Integer, Integer> testLRU = new LRUSimple<>(4);
        testLRU.put(1, 1);
        System.out.println(testLRU);
        testLRU.put(2, 2);
        System.out.println(testLRU);
        testLRU.put(3, 3);
        System.out.println(testLRU);
        testLRU.put(4, 4);
        System.out.println(testLRU);
        testLRU.put(5, 5);
        System.out.println(testLRU);
        testLRU.put(4, 4);
        System.out.println(testLRU);
        testLRU.get(3);
        System.out.println(testLRU);
    }

    private void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > cacheSize;
    }
}