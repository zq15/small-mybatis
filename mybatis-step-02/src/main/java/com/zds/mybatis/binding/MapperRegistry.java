package com.zds.mybatis.binding;

import cn.hutool.core.util.ClassUtil;
import com.zds.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistry {

    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

    /**
     * 注册接口
     *
     * @param type 接口类型
     * @param <T>  接口
     */
    public <T> void addMapper(Class<T> type) {
        // 判断类型是否是接口
        // 注意这个判断为什么要提到这里 需要满足每个方法的健壮性
        if (!type.isInterface())
            throw new RuntimeException("Type " + type + " is not interface.");
        // 重复添加则报错
        if (hasMapper(type))
            throw new RuntimeException("Type " + type + " is already known to the MapperRegistry.");
        knownMappers.put(type, new MapperProxyFactory<>(type));
    }

    /**
     * 判断是否已经注册
     *
     * @param type 接口类型
     * @param <T>  接口
     * @return 是否已经注册
     */
    private <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    /**
     * 获取接口
     *
     * @param type       接口类型
     * @param sqlSession sqlSession
     * @param <T>        接口
     * @return 接口
     */
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        System.out.println("你的接口被代理了！" + type.getName());
        MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        // 判断是否为空
        if (mapperProxyFactory == null)
            throw new RuntimeException("Type " + type + " is not known to the MapperRegistry.");
        // 返回代理类
        return mapperProxyFactory.newInstance(sqlSession);
    }

    /**
     * 通过包名扫描包下的所有接口，并注册
     *
     * @param packageName 包名
     */
    public void addMappers(String packageName) {
        // 扫描包下的所有接口
        ClassUtil.scanPackage(packageName)
                .forEach(this::addMapper);
    }
}
