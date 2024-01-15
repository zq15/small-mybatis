package com.zds.mybatis.mapping;

import com.zds.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;

// 建造者模式 单例
public final class Environment {

    // 环境id
    private String id;

    // 数据源
    private DataSource dataSource;

    // 事务工厂
    private TransactionFactory transactionFactory;

    private Environment() {
        // disable constructor
    }

    // 内部静态构建器类
    public static class Builder {
        private Environment environment = new Environment();

        public Builder(String id, DataSource dataSource, TransactionFactory transactionFactory) {
            environment.id = id;
            environment.dataSource = dataSource;
            environment.transactionFactory = transactionFactory;
        }

        // build 方法用于创建最终的 Environment 对象
        public Environment build() {
            // 用断言做校验
            assert environment.id != null;
            assert environment.dataSource != null;
            assert environment.transactionFactory != null;
            return environment;
        }
    }
}
