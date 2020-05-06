package com.haizhi.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mtl
 * @Description:
 * @date 2020/5/6 9:46
 */
public class ClassUtils {
    private final static Map<String,String> specialClsMap = new HashMap<>();

    static {
        initMap();
    }

    private static void initMap(){
        specialClsMap.put("byte","java.lang.Byte");
        specialClsMap.put("short","java.lang.Short");
        specialClsMap.put("char","java.lang.Character");
        specialClsMap.put("int","java.lang.Integer");
        specialClsMap.put("long","java.lang.Long");
        specialClsMap.put("float","java.lang.Float");
        specialClsMap.put("double","java.lang.Double");
        specialClsMap.put("boolean","java.lang.Boolean");
        specialClsMap.put("java.lang.Byte","byte");
        specialClsMap.put("java.lang.Short","short");
        specialClsMap.put("java.lang.Character","char");
        specialClsMap.put("java.lang.Integer","int");
        specialClsMap.put("java.lang.Long","long");
        specialClsMap.put("java.lang.Float","float");
        specialClsMap.put("java.lang.Double","double");
        specialClsMap.put("java.lang.Boolean","boolean");
    }

    /**
     * 判断是否同一个类,包容基础类型和其包装类的比较,比如int和Integer认定为是同一个类型
     * @param cls1
     * @param cls2
     * @return
     */
    public static boolean isClassEq(Class cls1,Class cls2){
        if(cls1 == cls2){
            return true;
        }
        String cls1Name = cls1.getName();
        String cls2Name = cls2.getName();
        String newcls1Name = specialClsMap.get(cls1Name);
        if(null == newcls1Name){
            return false;
        }
        if(!newcls1Name.equals(cls2Name)){
            return false;
        }
        return true;
    }

    /**
     * 是否基础类型
     * @param cls
     * @return
     */
    public static boolean isBasicType(Class cls){
        return specialClsMap.containsKey(cls.getName());
    }


}
