package com.zds.mybatis.transaction;

import com.zds.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;

public class JdbcTransaction implements Transaction{
    public JdbcTransaction(Connection conn) {

    }

    public JdbcTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {

    }

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }

    @Override
    public void close() {

    }
}
