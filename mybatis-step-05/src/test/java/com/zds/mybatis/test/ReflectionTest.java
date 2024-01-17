package com.zds.mybatis.test;

import com.zds.mybatis.test.po.User;

import java.util.Date;

public class ReflectionTest {
    public static void main(String[] args) throws Exception {
        Class clazz = User.class;
        User user = (User) clazz.newInstance();
        clazz.getMethod("setCreateTime", Date.class).invoke(user, new Date());
        System.out.println(user.getCreateTime());
    }
}
