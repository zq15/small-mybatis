package com.zds.mybatis.session.defaults;

import com.zds.mybatis.mapping.MappedStatement;
import com.zds.mybatis.session.Configuration;
import com.zds.mybatis.session.SqlSession;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        return (T) ("你的被代理了！" + "方法名: " + statement + "入参: " + parameter + "，执行sql: " + mappedStatement.getSql());
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你的被代理了！" + statement);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
