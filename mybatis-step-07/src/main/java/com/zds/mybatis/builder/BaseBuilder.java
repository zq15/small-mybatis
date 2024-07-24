package com.zds.mybatis.builder;

import com.zds.mybatis.session.Configuration;
import com.zds.mybatis.type.TypeAliasRegistry;

/**
 * Description: 基础构建器
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;

    protected final TypeAliasRegistry typeAliasRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = configuration.getTypeAliasRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
