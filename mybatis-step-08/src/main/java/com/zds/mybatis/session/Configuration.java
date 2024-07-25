package com.zds.mybatis.session;

import com.zds.mybatis.binding.MapperRegistry;
import com.zds.mybatis.datasource.druid.DruidDataSourceFactory;
import com.zds.mybatis.datasource.hikari.HikariDataSourceFactory;
import com.zds.mybatis.datasource.pooled.PooledDataSourceFactory;
import com.zds.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import com.zds.mybatis.executor.Executor;
import com.zds.mybatis.executor.SimpleExecutor;
import com.zds.mybatis.executor.resultset.DefaultResultSetHandler;
import com.zds.mybatis.executor.resultset.ResultSetHandler;
import com.zds.mybatis.executor.statement.PreparedStatementHandler;
import com.zds.mybatis.executor.statement.StatementHandler;
import com.zds.mybatis.mapping.BoundSql;
import com.zds.mybatis.mapping.Environment;
import com.zds.mybatis.mapping.MappedStatement;
import com.zds.mybatis.transaction.Transaction;
import com.zds.mybatis.transaction.jdbc.JdbcTransactionFactory;
import com.zds.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * 包含了 Mybatis 初始化过程中所有的配置信息
 * 注册的所有 mapper，以及每个 mapper 中的每个方法的信息
 */
public class Configuration {

    private MapperRegistry mapperRegistry = new MapperRegistry(this);

    private final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    private final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    private Environment environment;

    public Configuration() {
        // 初始化别名
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);

        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("HIKARI", HikariDataSourceFactory.class);

        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public <T> boolean hasMapper(Class<T> type) {
        return mapperRegistry.hasMapper(type);
    }

    public <T> void addMapper(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    /**
     * 创建结果集处理器
     */
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, boundSql);
    }

    /**
     * 生产执行器
     */
    public Executor newExecutor(Transaction transaction) {
        return new SimpleExecutor(this, transaction);
    }

    /**
     * 创建语句处理器
     */
    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        return new PreparedStatementHandler(executor, mappedStatement, parameter, resultHandler, boundSql);
    }
}
