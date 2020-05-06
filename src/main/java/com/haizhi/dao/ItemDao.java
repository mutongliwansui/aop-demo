package com.haizhi.dao;

import com.haizhi.pojo.TbItem;

import java.util.List;
import java.util.Map;

/**
 * @author mtl
 * @Description:
 * @date 2020/4/28 16:36
 */
public interface ItemDao {
    /**
     * 获取商品信息
     * @param id
     * @return
     */
    TbItem getItem(int id);
}
