package com.zds.mybatis.type;

import com.zds.mybatis.io.Resources;

import java.util.HashMap;
import java.util.Map;

// 类型别名注册器
public class TypeAliasRegistry {

    private final Map<String, Class<?>> TYPE_ALIASES = new HashMap<>();

    public TypeAliasRegistry() {
        registerAlias("string", String.class);

        registerAlias("byte", Byte.class);
        registerAlias("long", Long.class);
        registerAlias("int", Integer.class);
        registerAlias("short", Short.class);
        registerAlias("double", Double.class);
        registerAlias("float", Float.class);
        registerAlias("boolean", Boolean.class);
        registerAlias("integer", Integer.class);
    }

    // 注册
    public void registerAlias(String alias, Class<?> value) {
        if (alias == null) {
            throw new IllegalArgumentException("The parameter alias cannot be null");
        }
        String key = alias.toLowerCase();
        if (TYPE_ALIASES.containsKey(key) && TYPE_ALIASES.get(key) != null && !TYPE_ALIASES.get(key).equals(value)) {
            throw new IllegalArgumentException("The alias '" + alias + "' is already mapped to the value '" + TYPE_ALIASES.get(key).getName() + "'.");
        }
        TYPE_ALIASES.put(key, value);
    }

    // 读取
    public <T> Class<T> resolveAlias(String string) {
        if (string == null) {
            return null;
        }
        String key = string.toLowerCase();
        Class<T> value;
        if (TYPE_ALIASES.containsKey(key)) {
            value = (Class<T>) TYPE_ALIASES.get(key);
        } else {
            try {
                value = (Class<T>) Resources.classForName(string);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Could not resolve type alias '" + string + "'.  Cause: " + e, e);
            }
        }
        return value;
    }
}
