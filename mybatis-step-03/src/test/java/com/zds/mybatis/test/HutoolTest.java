package com.zds.mybatis.test;

import cn.hutool.core.util.ClassUtil;

public class HutoolTest {
    public static void main(String[] args) {
        ClassUtil.scanPackage("com.zds.mybatis.test.dao.IUserDao").forEach(clazz -> {
            System.out.println(clazz.getName());
        });
    }
}
