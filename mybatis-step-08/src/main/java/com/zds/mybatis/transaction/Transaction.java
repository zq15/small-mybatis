package com.zds.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Description: 事务
 */
public interface Transaction {

    Connection getConnection() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;
}
