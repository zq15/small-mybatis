package com.zds.mybatis.mapping;

import com.zds.mybatis.session.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * mapper 中 方法的描述抽象
 */
public class MappedStatement {

    private Configuration configuration;

    private String id;
    private String parameterType;
    private String resultType;

    // sql 操作类型
    private SqlCommandType sqlCommandType;
    private String sql;

    Map<Integer, String> parameterMap = new HashMap<>();

    // 构造器为私有，不允许直接实例化
    private MappedStatement() {
    }

    // 内部静态构建器类
    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, String parameterType, String resultType, String sql, Map<Integer, String> parameterMap) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.parameterType = parameterType;
            mappedStatement.resultType = resultType;
            mappedStatement.sql = sql;
            mappedStatement.parameterMap = parameterMap;
        }

        // build 方法用于创建最终的 MappedStatement 对象
        public MappedStatement build() {
            // 用断言做校验
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<Integer, String> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<Integer, String> parameterMap) {
        this.parameterMap = parameterMap;
    }
}
