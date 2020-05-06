package com.haizhi.cache;

import java.util.Set;

/**
 * @author mtl
 * @Description:
 * @date 2020/4/28 16:21
 */
public interface Cache {

    void put(String key,Object value);

    Object get(String key);

    void remove(String key);

    void flush();

    Set<String> keys();

}
