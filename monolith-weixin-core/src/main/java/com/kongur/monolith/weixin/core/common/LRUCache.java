package com.kongur.monolith.weixin.core.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 基于LinkedHashMap实现的LRU Cache
 * 
 * @author zhengwei
 * @date 2014-4-10
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    /**
     * 
     */
    private static final long   serialVersionUID    = -1870987125531590254L;

    private static final int    DEFAULT_CAPACITY    = 100;

    /**
     * 最大容量，默认100
     */
    private int                 maxCapacity         = DEFAULT_CAPACITY;

    private static final float  DEFAULT_LOAD_FACTOR = 0.75f;

    private final ReadWriteLock lock                = new ReentrantReadWriteLock();

    private final Lock          readLock            = lock.readLock();

    private final Lock          writeLock           = lock.writeLock();
    
    public LRUCache() {
        super(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, true);
    }

    public LRUCache(int maxCapacity) {
        super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
    }

    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {

        return size() > maxCapacity;
    }

    @Override
    public boolean containsKey(Object key) {
        try {
            readLock.lock();
            return super.containsKey(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public V get(Object key) {

        try {
            readLock.lock();
            return super.get(key);
        } finally {
            readLock.unlock();
        }

    }

    @Override
    public V put(K key, V value) {
        try {
            writeLock.lock();
            return super.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public int size() {
        try {
            readLock.lock();
            return super.size();
        } finally {
            readLock.unlock();
        }
    }

    public void clear() {
        try {
            writeLock.lock();
            super.clear();
        } finally {
            writeLock.unlock();
        }
    }

    public Collection<Map.Entry<K, V>> getAll() {
        try {
            readLock.lock();
            return new ArrayList<Map.Entry<K, V>>(super.entrySet());
        } finally {
            readLock.unlock();
        }
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

}
