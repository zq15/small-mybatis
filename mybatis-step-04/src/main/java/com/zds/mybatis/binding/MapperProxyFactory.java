package com.zds.mybatis.binding;

import com.zds.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MapperProxyFactory
 * 代理类的工厂
 *
 * @param <T>
 */
public class MapperProxyFactory<T> {
    private final Class<T> mapperInterface;

    private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<>();

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public T newInstance(SqlSession sqlSession) {
        // 返回代理类
        return (T) java.lang.reflect.Proxy.newProxyInstance(mapperInterface.getClassLoader(),
                new Class[]{mapperInterface},
                new MapperProxy(mapperInterface, sqlSession, methodCache));
    }
}
