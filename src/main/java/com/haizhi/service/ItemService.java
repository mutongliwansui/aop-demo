package com.haizhi.service;

import com.haizhi.cache.Cacheable;
import com.haizhi.pojo.TbItem;

/**
 * @author mtl
 * @Description:
 * @date 2020/4/28 17:21
 */
public interface ItemService {
    TbItem getItem(int id);

    @Cacheable(key = "#id")
    TbItem getItem(int id, String name);
}
