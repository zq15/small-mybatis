package com.zds.mybatis.test;

import com.zds.mybatis.io.Resources;
import com.zds.mybatis.session.SqlSession;
import com.zds.mybatis.session.SqlSessionFactory;
import com.zds.mybatis.session.SqlSessionFactoryBuilder;
import com.zds.mybatis.test.dao.IUserDao;
import com.zds.mybatis.test.po.User;

import java.io.Reader;

public class DataSourceTest {
    public static void main(String[] args) {
        // 拿到 SqlSession
        // 从类路径读取文件
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 从 SqlSession 中拿到 Mapper
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        // 调用 Mapper 中的方法
        User user = userDao.queryUserInfoById("1");
        System.out.println(user.toString());

    }
}
