package com.zds.mybatis.binding;

import com.zds.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MapperProxy<T> implements InvocationHandler {

    private final Class<T> mapperInterface;

    private final SqlSession sqlSession;

    private final Map<Method, MapperMethod> methodCache;

    public MapperProxy(Class<T> mapperInterface, SqlSession sqlSession, Map<Method, MapperMethod> methodCache) {
        this.mapperInterface = mapperInterface;
        this.sqlSession = sqlSession;
        this.methodCache = methodCache;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 从缓存加载代理方法
        MapperMethod mapperMethod = cachedMapperMethod(method);
        // 执行代理方法
        return mapperMethod.execute(sqlSession, args);
    }

    private MapperMethod cachedMapperMethod(Method method) {
        // 不存在就去创建
        if (methodCache.get(method) == null) {
            methodCache.put(method, new MapperMethod(mapperInterface, method, sqlSession.getConfiguration()));
        }
        // 如果存在就从缓存中取
        return methodCache.get(method);
    }
}
