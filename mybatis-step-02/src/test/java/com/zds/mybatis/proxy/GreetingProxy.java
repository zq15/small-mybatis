package com.zds.mybatis.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class GreetingProxy implements InvocationHandler {
    private Object target;

    public GreetingProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(target, args);
        return result;
    }
}
