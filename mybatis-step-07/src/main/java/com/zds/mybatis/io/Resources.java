package com.zds.mybatis.io;

import cn.hutool.core.io.resource.ResourceUtil;

import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Resources 通过类加载器获得resource的辅助类
 * 资源类
 */
public class Resources {

    /**
     * 从给定的类路径拿到Class
     *
     * @param className 类
     * @return Class
     */
    public static Class<?> classForName(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    /**
     * 从给定的类路径拿到资源
     *
     * @param resource 资源
     * @return Reader
     */
    public static Reader getResourceAsReader(String resource) {
        return new InputStreamReader(ResourceUtil.getStream(resource));
    }
}
