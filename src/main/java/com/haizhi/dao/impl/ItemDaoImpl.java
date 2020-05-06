package com.haizhi.dao.impl;

import com.haizhi.dao.ItemDao;
import com.haizhi.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author mtl
 * @Description:
 * @date 2020/4/28 16:37
 */
@Repository
public class ItemDaoImpl implements ItemDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<TbItem> ITEM_MAPPER = new BeanPropertyRowMapper<>(TbItem.class);

    @Override
    public TbItem getItem(int id) {
        String sql = "select * from tb_item where id = ? ";
        return this.jdbcTemplate.queryForObject(sql,ITEM_MAPPER,id);
    }
}
