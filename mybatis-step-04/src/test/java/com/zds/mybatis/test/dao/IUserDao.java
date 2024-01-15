package com.zds.mybatis.test.dao;

import com.zds.mybatis.test.po.User;

public interface IUserDao {
    User queryUserInfoById(String id);
}
