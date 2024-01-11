package com.zds.mybatis.session;

public interface SqlSession {

    <T> T selectOne(String statement, Object parameter);

    <T> T selectOne(String statement);

    <T> T getMapper(Class<T> type);
}
