package com.zds.mybatis.binding;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class MapperProxy<T> implements InvocationHandler {

    private final Class<T> mapperInterface;

    private final Map<String, String> sqlSession;

    public MapperProxy(Class<T> mapperInterface, Map<String, String> sqlSession) {
        this.mapperInterface = mapperInterface;
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return "你的被代理了！" + sqlSession.get(mapperInterface.getName() + "." + method.getName());
    }
}
