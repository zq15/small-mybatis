package com.zds.mybatis.proxy;

public class GreetingImpl implements Greeting {
    @Override
    public void greeting() {
        System.out.println("Hello World!");
    }
}
