package com.zds.mybatis.transaction;

import java.sql.Connection;

/**
 * Description: 事务
 */
public interface Transaction {

    /**
     * 获取连接
     */
    Connection getConnection();

    /**
     * 提交事务
     */
    void commit();

    /**
     * 回滚事务
     */
    void rollback();

    /**
     * 关闭事务
     */
    void close();
}
