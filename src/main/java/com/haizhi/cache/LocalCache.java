package com.haizhi.cache;


import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mtl
 * @Description: 简单的本地缓存，现如今一般使用redis或者memcached
 * @date 2020/4/28 16:18
 */
public class LocalCache implements Cache{

    private ConcurrentHashMap<String,Object> cacheMap;

    private LocalCache() {
        this.cacheMap = new ConcurrentHashMap<String, Object>();
    }

    /**
     * 新建一个缓存对象
     * @return
     */
    public static LocalCache newInstance() {
        LocalCache localCache = new LocalCache();
        return localCache;
    }

    /**
     * 添加到缓存
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        this.cacheMap.put(key,value);
        System.out.println("cache:"+this.cacheMap);
    }

    /**
     * 从缓存获取
     * @param key
     * @return
     */
    public Object get(String key) {
        System.out.println("cache:"+this.cacheMap);
        return this.cacheMap.get(key);
    }

    /**
     * 从缓存移除
     * @param key
     */
    public void remove(String key) {
        this.cacheMap.remove(key);
    }

    /**
     * 清空
     */
    public void flush() {
        this.cacheMap.clear();
    }

    /**
     * 获取缓存里所有的key值
     * @return
     */
    public Set<String> keys() {
        return this.cacheMap.keySet();
    }
}
