package com.zds.mybatis.proxy;

public class ProxyTest {
    public static void main(String[] args) {
        Greeting greeting = new GreetingImpl();
        GreetingProxy greetingProxy = new GreetingProxy(greeting);
        Greeting greetingImpl = (Greeting) java.lang.reflect.Proxy.newProxyInstance
                (greetingProxy.getClass().getClassLoader(), greeting.getClass().getInterfaces(), greetingProxy);
        greetingImpl.greeting();
    }
}
