package com.zds.mybatis.datasource.hikari;

import com.zaxxer.hikari.HikariDataSource;
import com.zds.mybatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class HikariDataSourceFactory implements DataSourceFactory {
    Properties props;
    @Override
    public void setProperties(Properties props) {
        this.props = props;
    }

    @Override
    public DataSource getDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(props.getProperty("driver"));
        ds.setJdbcUrl(props.getProperty("url"));
        ds.setUsername(props.getProperty("username"));
        ds.setPassword(props.getProperty("password"));
        return ds;
    }
}
