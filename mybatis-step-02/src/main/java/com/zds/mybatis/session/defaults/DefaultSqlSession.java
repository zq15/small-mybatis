package com.zds.mybatis.session.defaults;

import com.zds.mybatis.binding.MapperRegistry;
import com.zds.mybatis.session.SqlSession;

public class DefaultSqlSession implements SqlSession {

    private MapperRegistry mapperRegistry = new MapperRegistry();

    public DefaultSqlSession(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return (T) ("你的被代理了！" + statement + parameter);
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你的被代理了！" + statement);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return mapperRegistry.getMapper(type, this);
    }
}
