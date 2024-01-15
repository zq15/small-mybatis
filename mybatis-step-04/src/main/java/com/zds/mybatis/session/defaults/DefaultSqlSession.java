package com.zds.mybatis.session.defaults;

import cn.hutool.core.collection.CollectionUtil;
import com.zds.mybatis.mapping.Environment;
import com.zds.mybatis.mapping.MappedStatement;
import com.zds.mybatis.session.Configuration;
import com.zds.mybatis.session.SqlSession;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        try {
            // 拿到 MappedStatement 对象
            MappedStatement mappedStatement = configuration.getMappedStatement(statement);
            // 拿到 connection 对象执行 SQL
            Environment environment = configuration.getEnvironment();
            Connection connection = environment.getDataSource().getConnection();
            // 执行 SQL
            PreparedStatement preparedStatement = connection.prepareStatement(mappedStatement.getSql());
            preparedStatement.setLong(1, Long.parseLong(((Object[]) parameter)[0].toString()));
            ResultSet resultSet = preparedStatement.executeQuery();
            // 处理结果集
            List<T> results = resultSet2Object(resultSet, Class.forName(mappedStatement.getResultType()));
            // 返回结果和类型处理
            // todo 如果一行都没读到呢
            return CollectionUtil.isEmpty(results)?null:results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private <T> List<T> resultSet2Object(ResultSet resultSet, Class<?> clazz) throws SQLException {
        List<T> list = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 每次遍历行值
            while (resultSet.next()) {
                T obj = (T) clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    String columnName = metaData.getColumnName(i);
                    // 注意验证这个方法的合理性
                    String setMethod = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                    Method method;
                    // 单独处理 Timestamp 类型，因为 mysql 中的 timestamp 类型和 java 中的 timestamp 类型不一致
                    if (value instanceof Timestamp) {
                        method = clazz.getMethod(setMethod, java.util.Date.class);
                    } else {
                        method = clazz.getMethod(setMethod, value.getClass());
                    }
                    method.invoke(obj, value);
                }
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你的被代理了！" + statement);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    public static void main(String[] args) {
        // 测试命名的转换
        String columnName = "pId";
        System.out.println(columnName.substring(0, 1).toUpperCase() + columnName.substring(1));
    }
}
