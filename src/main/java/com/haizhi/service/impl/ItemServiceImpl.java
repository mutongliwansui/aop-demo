package com.haizhi.service.impl;

import com.haizhi.cache.Cacheable;
import com.haizhi.dao.ItemDao;
import com.haizhi.pojo.TbItem;
import com.haizhi.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mtl
 * @Description:
 * @date 2020/4/28 17:23
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDao itemDao;

    @Cacheable(key = "getItem_${id}")
    @Override
    public TbItem getItem(int id) {
        TbItem item = itemDao.getItem(id);
        return item;
    }

    @Cacheable
    @Override
    public TbItem getItem(int id,String name) {
        TbItem item = itemDao.getItem(id);
        return item;
    }

}
