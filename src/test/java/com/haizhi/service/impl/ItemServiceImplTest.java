package com.haizhi.service.impl;

import com.haizhi.config.AppConfig;
import com.haizhi.pojo.TbItem;
import com.haizhi.service.ItemService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;



public class ItemServiceImplTest {

    private ItemService itemService;

    @Before
    public void preTest(){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.itemService = context.getBean(ItemService.class);
    }

    @Test
    public void getItem() {
        TbItem item = itemService.getItem(536563);
        TbItem cacheitem = itemService.getItem(536563);
        System.out.println(item);
    }

    @Test
    public void testClassEq(){
        Map map = new HashMap<>();
        int a = 1;
        map.put("c",a);
        Class<?> cls = map.get("c").getClass();
        Class cls2 = Integer.class;
        System.out.println(cls == cls2);
    }
}