package com.zds.mybatis.session;

import com.zds.mybatis.binding.MapperRegistry;
import com.zds.mybatis.datasource.DruidDataSourceFactory;
import com.zds.mybatis.mapping.Environment;
import com.zds.mybatis.mapping.MappedStatement;
import com.zds.mybatis.transaction.JdbcTransactionFactory;
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
        typeAliasRegistry.registerAlias("jdbc", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("druid", DruidDataSourceFactory.class);
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

    public void addMappedStatement(String msId, MappedStatement mappedStatement) {
        mappedStatements.put(msId, mappedStatement);
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
}
