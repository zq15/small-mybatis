package com.zds.mybatis.binding;

import java.util.Map;

/**
 * MapperProxyFactory
 *
 * 代理类的工厂
 *
 * @param <T>
 */
public class MapperProxyFactory<T> {
    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public T newInstance(Map<String, String> sqlSession) {
        // 返回代理类
        return (T) java.lang.reflect.Proxy.newProxyInstance(mapperInterface.getClassLoader(),
                new Class[]{mapperInterface},
                new MapperProxy(mapperInterface, sqlSession));
    }
}
