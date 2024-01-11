package com.zds.mybatis.test;

import com.zds.mybatis.binding.MapperRegistry;
import com.zds.mybatis.session.SqlSession;
import com.zds.mybatis.session.SqlSessionFactory;
import com.zds.mybatis.session.defaults.DefaultSqlSessionFactory;
import com.zds.mybatis.test.dao.IUserDao;

public class RegistryTest {

    public static void main(String[] args) {
        MapperRegistry mapperRegistry = new MapperRegistry();
        mapperRegistry.addMappers("com.zds.mybatis.test.dao");
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(mapperRegistry);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        userDao.queryUserName("12312321");
    }
}
