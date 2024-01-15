package com.zds.mybatis.builder;

import com.zds.mybatis.session.Configuration;

/**
 * Description: 基础构建器
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
