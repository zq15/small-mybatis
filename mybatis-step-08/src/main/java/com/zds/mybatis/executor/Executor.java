package com.zds.mybatis.executor;

import com.zds.mybatis.mapping.BoundSql;
import com.zds.mybatis.mapping.MappedStatement;
import com.zds.mybatis.session.ResultHandler;
import com.zds.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * 执行器接口
 */
public interface Executor {

    ResultHandler NO_RESULT_HANDLER = null;

    <E> List<E> query(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql);

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);

}
