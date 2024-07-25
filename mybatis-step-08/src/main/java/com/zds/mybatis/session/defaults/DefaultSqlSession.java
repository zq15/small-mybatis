package com.zds.mybatis.session.defaults;

import cn.hutool.core.collection.CollectionUtil;
import com.zds.mybatis.executor.Executor;
import com.zds.mybatis.mapping.Environment;
import com.zds.mybatis.mapping.MappedStatement;
import com.zds.mybatis.session.Configuration;
import com.zds.mybatis.session.SqlSession;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        // 拿到 MappedStatement 对象
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);

        List<T> results = executor.query(mappedStatement, parameter, Executor.NO_RESULT_HANDLER, mappedStatement.getBoundSql());

        // 返回结果和类型处理
        return CollectionUtil.isEmpty(results)?null:results.get(0);
    }

    @Override
    public <T> T selectOne(String statement) {
        return this.selectOne(statement, null);
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
