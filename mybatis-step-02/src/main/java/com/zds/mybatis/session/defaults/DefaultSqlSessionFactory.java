package com.zds.mybatis.session.defaults;

import com.zds.mybatis.binding.MapperRegistry;
import com.zds.mybatis.session.SqlSession;
import com.zds.mybatis.session.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final MapperRegistry mapperRegistry = new MapperRegistry();

    public DefaultSqlSessionFactory(MapperRegistry mapperRegistry) {
        this.mapperRegistry.addMappers("com.zds.mybatis.test.dao");
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(mapperRegistry);
    }
}
