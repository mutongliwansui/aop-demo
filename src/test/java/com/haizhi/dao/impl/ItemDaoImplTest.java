package com.haizhi.dao.impl;

import com.haizhi.config.AppConfig;
import com.haizhi.dao.ItemDao;
import com.haizhi.pojo.TbItem;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ItemDaoImplTest {


    private ItemDao itemDao;

    @Before
    public void preTest(){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.itemDao = context.getBean(ItemDao.class);
    }

    @Test
    public void getItem() {
        TbItem item = itemDao.getItem(536563);
        System.out.println(item);
    }
}