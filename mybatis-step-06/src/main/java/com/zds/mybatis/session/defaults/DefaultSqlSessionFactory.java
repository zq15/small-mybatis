package com.zds.mybatis.session.defaults;

import com.zds.mybatis.binding.MapperRegistry;
import com.zds.mybatis.session.Configuration;
import com.zds.mybatis.session.SqlSession;
import com.zds.mybatis.session.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
