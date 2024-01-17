package com.zds.mybatis.binding;

import com.zds.mybatis.session.Configuration;
import com.zds.mybatis.session.SqlSession;

import java.lang.reflect.Method;

public class MapperMethod {

    private final String methodId;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        // 需要做个转换，拿到方法的完整 id
        // 例如：com.zds.mybatis.mapper.UserMapper.selectById
        methodId = mapperInterface.getName() + "." + method.getName();
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        // 传入方法的id和参数，方便后面执行sql
        // 这里的方法id从哪里拿呢 SqlSession 中有的，构造的时候需要传入
        return sqlSession.selectOne(methodId, args);
    }
}
