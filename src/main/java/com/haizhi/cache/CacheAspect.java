package com.haizhi.cache;

import com.haizhi.common.utils.ClassUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
@Component
public class CacheAspect {


    private final static String CACHE_NULL = "NONE"; //缓存空，用于处理缓存穿透问题

    @Autowired
    private LocalCache localCache;

    @Pointcut("@annotation(com.haizhi.cache.Cacheable)")
    public void pointcut(){

    }

    @Around("pointcut()")
    public Object around(JoinPoint point) throws Throwable{
        MethodInvocationProceedingJoinPoint joinPoint = null;
        Object[] args = new Object[0];
        String cacheKey = null;
        try {
            joinPoint = (MethodInvocationProceedingJoinPoint) point;
            args = joinPoint.getArgs();
            String mtdname = joinPoint.getSignature().getName();
            Class[] clsArr = new Class[args.length];
            int i = 0;
            for (Object arg : args) {
                clsArr[i] = arg.getClass();
                i++;
            }
            Class proxyedCls = joinPoint.getTarget().getClass(); //被代理对象或者类的class
            Method method = getMethod(mtdname, clsArr, proxyedCls); //根据方法名和参数类型获取方法
            Cacheable cacheable = method.getAnnotation(Cacheable.class);
            String key = cacheable.key(); //获取key
            cacheKey = null;
            if(isEmpty(key)){ //如果没有指定key的格式，默认就是方法全名_参数
                cacheKey = generateCacheKey(method,args); //生成缓存key
                System.out.println(cacheKey);
            }else{//如果指定了key的格式
                ParameterNameDiscoverer discover = new DefaultParameterNameDiscoverer();
                String[] parameterNames = discover.getParameterNames(method); //获取方法的参数名
                cacheKey = generateCacheKey(key,parameterNames,args); //生成缓存key
                System.out.println(cacheKey);
            }
            Object cacheValue = getFromCache(cacheKey); //从缓存获取
            if(null != cacheValue){
                if(CACHE_NULL.equals(cacheValue)){
                    return null;
                }
                return cacheValue;
            }
        } catch (Exception e) {
            e.printStackTrace(); //打印日志
        }
        Object proceed = joinPoint.proceed(args); //执行被代理方法，获取结果
        putCache(cacheKey,proceed); //把结果放入缓存
        return proceed;
    }

    /**
     * 放入缓存
     * @param cacheKey
     * @param proceed
     */
    private void putCache(String cacheKey, Object proceed) {
        try {
            if(isEmpty(cacheKey)){
                return;
            }
            if(null == proceed){
                // 避免缓存穿透问题(就是结果为空值的情况下，存入缓存的是空值,
                // 后续从缓存获取都是空值，就会导致每次都是判断为缓存未命中，
                // 直接穿透缓存访问主逻辑代码进行结果获取，缓存就失去了意义)
                proceed = CACHE_NULL;
            }
            this.localCache.put(cacheKey,proceed);
        } catch (Exception e) {
            //catch异常，缓存模块不影响主逻辑代码
            //打印日志
            e.printStackTrace();
        }
    }

    /**
     * 从缓存获取
     * @param cacheKey
     * @return
     */
    private Object getFromCache(String cacheKey) {
        Object result = null;
        try {
            result = this.localCache.get(cacheKey);
        } catch (Exception e) {
            //catch异常，缓存模块不影响主逻辑代码
            //打印日志
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 生成缓存key
     * @param method
     * @param args
     * @return
     */
    private String generateCacheKey(Method method, Object[] args) {
        String methodName = getCustomMethodName(method);
        StringBuilder sb = new StringBuilder();
        sb.append(methodName);
        for (Object arg : args) {
            Class cls = arg.getClass();
            boolean isbasicType = ClassUtils.isBasicType(cls);
            if(isbasicType){
                sb.append("_").append(arg.toString());
            }else{
                //暂时把引用类型也用toString来处理,后续还需要考虑自定义类型的情况,比如getItem(User user)这种情况
                sb.append("_").append(arg.toString());
            }
        }
        return sb.toString();
    }

    /**
     * 生成缓存key
     * @param key
     * @param paramNames
     * @param args
     * @return
     */
    private String generateCacheKey(String key, String [] paramNames, Object[] args) {
        Map<String,String> param = new HashMap();
        for (int i = 0; i < paramNames.length; i++) {
            //暂时把引用类型也用toString来处理,后续还需要考虑自定义类型的情况比如getItem(User user)这种情况
            param.put(paramNames[i],args[i].toString());
        }
        String cacheKey = key;
        Pattern pattern = Pattern.compile("(\\$\\{([a-zA-Z_]+)\\})"); //正则表达式匹配${ABC}
        Matcher matcher = pattern.matcher(key);
        while (matcher.find()){
            String match = matcher.group(1); //${ABC}
            String field = matcher.group(2); //ABC
            cacheKey = cacheKey.replace(match,param.get(field));
        }
        return cacheKey;
    }

    /**
     * 获取方法全名
     * @param method
     * @return
     */
    private String getCustomMethodName(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        String mtdName = declaringClass.getName() + "." + method.getName();
        return mtdName;
    }

    /**
     * 判断字符串是否为空
     * @param content
     * @return
     */
    private boolean isEmpty(String content){
        return (null == content || content.length() == 0 || content.trim().length() == 0);
    }

    /**
     * 获取方法
     * @param mtdName 匹配方法名
     * @param cls 匹配参数类型数组
     * @param targetClass 目标类
     * @return
     */
    private Method getMethod(String mtdName, Class[] cls, Class targetClass){
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            //1.判断方法名称
            if(!mtdName.equals(methodName)){
                continue;
            }
            Class[] parameterTypes = method.getParameterTypes();
            boolean isEq = compareClsArr(cls,parameterTypes); //比较所有参数类型
            if(isEq){
               return method;
            }
        }
        return null;
    }

    /**
     * 比较参数数组
     * @param clsArr
     * @param parameterTypes
     * @return
     */
    private boolean compareClsArr(Class[] clsArr, Class[] parameterTypes) {
        //先比较参数个数，个数不一致直接返回false
        if(clsArr.length != parameterTypes.length){
            return false;
        }
        for (int i = 0; i < clsArr.length; i++) {
            Class cls = clsArr[i];
            Class tarParamCls = parameterTypes[i];
            if(!ClassUtils.isClassEq(cls,tarParamCls)){ //判断两个类是否一致
                return false;
            }
        }
        return true;
    }



}
