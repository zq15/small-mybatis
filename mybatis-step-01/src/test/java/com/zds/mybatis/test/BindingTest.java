package com.zds.mybatis.test;

import com.zds.mybatis.binding.MapperProxyFactory;
import com.zds.mybatis.test.dao.IUserDao;

import java.util.HashMap;
import java.util.Map;

public class BindingTest {
    public static void main(String[] args) {
        Map<String, String> sqlSession = new HashMap<>();
        sqlSession.put("com.zds.mybatis.test.dao.IUserDao.queryUserName", "select * from user where id = %s");
        sqlSession.put("com.zds.mybatis.test.dao.IUserDao.queryUserAge", "select * from user where id = %s");

        // 通过代理类工厂获取代理类
        MapperProxyFactory<IUserDao> mapperProxyFactory = new MapperProxyFactory<>(IUserDao.class);
        IUserDao userDao = mapperProxyFactory.newInstance(sqlSession);
        userDao.queryUserName("2111");
    }
}
