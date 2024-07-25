package com.zds.mybatis.builder.xml;

import com.zds.mybatis.builder.BaseBuilder;
import com.zds.mybatis.datasource.DataSourceFactory;
import com.zds.mybatis.io.Resources;
import com.zds.mybatis.mapping.BoundSql;
import com.zds.mybatis.mapping.Environment;
import com.zds.mybatis.mapping.MappedStatement;
import com.zds.mybatis.mapping.SqlCommandType;
import com.zds.mybatis.session.Configuration;
import com.zds.mybatis.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlConfigBuilder extends BaseBuilder {

    private Element root;

    /**
     * 构造器 保存xml根节点
     *
     * @param reader 读取xml文件
     */
    public XmlConfigBuilder(Reader reader) {
        super(new Configuration());
        // 读取xml文件，获取到根节点
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(reader);
            root = document.getRootElement();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    // 创建方法读取 root 节点下的 mapper 节点，解析 mapper 节点下所有子节点
    // 创建方法读取 root 节点下的 environments 节点，解析 environment 节点下所有子节点
    // 读取出来的信息封装到 MappedStatement 对象中
    public Configuration parse() {
        try {
            // 解析environments节点
            environmentsElement(root.element("environments"));
            // 解析mapper节点
            mapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return configuration;
    }

    /**
     * <environments default="development">
     * <environment id="development">
     * <transactionManager type="JDBC">
     * <property name="..." value="..."/>
     * </transactionManager>
     * <dataSource type="POOLED">
     * <property name="driver" value="${driver}"/>
     * <property name="url" value="${url}"/>
     * <property name="username" value="${username}"/>
     * <property name="password" value="${password}"/>
     * </dataSource>
     * </environment>
     * </environments>
     */

    /**
     * 解析environments节点
     * @param context environments节点
     */
    private void environmentsElement(Element context) throws Exception {
        String environmentValue = context.attributeValue("default");
        List<Element> elements = context.elements("environment");
        for (Element element : elements) {
            String id = element.attributeValue("id");
            if (id.equals(environmentValue)) {
                // 事务管理器
                Element transactionManagerElement = element.element("transactionManager");
                // 需要按照不同的别名解析创建不同的事务管理器
                TransactionFactory transactionFactory = (TransactionFactory) typeAliasRegistry.resolveAlias(transactionManagerElement.attributeValue("type"))
                        .getConstructor().newInstance();

                Element dataSourceElement = element.element("dataSource");
                // 需要按照不同的别名解析创建不同的数据源
                // 我们抽象出来一个类 TypeAliasRegistry 来处理这个公用的逻辑
                // 读取别名
                DataSourceFactory dataSourceFactory = (DataSourceFactory) typeAliasRegistry.resolveAlias(dataSourceElement.attributeValue("type"))
                        .getConstructor().newInstance();
                // 读取数据源属性
                List<Element> propertyElements = dataSourceElement.elements("property");
                Properties properties = new Properties();
                for (Element propertyElement : propertyElements) {
                    String name = propertyElement.attributeValue("name");
                    String value = propertyElement.attributeValue("value");
                    properties.setProperty(name, value);
                }
                dataSourceFactory.setProperties(properties);
                DataSource dataSource = dataSourceFactory.getDataSource();

                // 构建环境
                Environment environment = new Environment.Builder(id, dataSource, transactionFactory).build();
                configuration.setEnvironment(environment);
            }
        }
    }

    private void mapperElement(Element mappers) throws Exception {
        // mybatis-config-datasource.xml 中加载所有 mapper 文件的地址
        // 遍历所有的 mapper 文件
        // 读取到所有的方法声明到 configuration 中

        List<Element> mapperList = mappers.elements("mapper");
        for (Element element : mapperList) {
            // 获取到 mapper 文件的地址
            String resource = element.attributeValue("resource");
            // 读取 mapper 文件
            Reader reader = Resources.getResourceAsReader(resource);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(reader);
            // 获取到 mapper 文件的根节点
            Element root = document.getRootElement();
            // 获取到mapper节点的namespace属性
            String namespace = root.attributeValue("namespace");

            // 处理所有select节点
            List<Element> selectNodes = root.elements("select");

            // 解析mapper节点下的所有子节点
            for (Element selectNode : selectNodes) {
                // 读取出 id parameterType resultType
                String id = selectNode.attributeValue("id");
                String parameterType = selectNode.attributeValue("parameterType");
                String resultType = selectNode.attributeValue("resultType");

                // 读取 sql 语句
                String sql = selectNode.getText();
                // 正则 读取 #{param} 参数 两个捕获组 第一个#{param} 第二个param
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                // 读取出来的sql语句是 select * from user where id = #{id} and username = #{username}

                Matcher matcher = pattern.matcher(sql);
                // 创建一个 map 存储 位置 和 sql 语句中的参数
                Map<Integer, String> parameterMap = new HashMap<>();
                for (int i = 1; matcher.find(); i++) {
                    // 获取到参数 #{param}
                    String param1 = matcher.group(1);
                    // 获取到参数 param
                    String param2 = matcher.group(2);
                    // 将参数存储到 map 中
                    parameterMap.put(i, param2);
                    // 将 sql 语句中的 #{param} 替换成 ?
                    sql = sql.replace(param1, "?");
                }
                // 注册 映射语句 mappedStatements <String, MappedStatement>
                // 这个方法的签名是什么呢 类路径 + id
                // MappedStatement 还需要封装其他的从xml中读取出来的方法信息

                String msId = namespace + "." + id;
                String nodeName = selectNode.getName();
                SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));

                BoundSql boundSql = new BoundSql(sql, parameterMap, parameterType, resultType);

                MappedStatement mappedStatement = new MappedStatement.Builder(configuration, msId, sqlCommandType, boundSql).build();
                // 添加解析 SQL
                configuration.addMappedStatement(mappedStatement);
            }

            // 注册接口
            configuration.addMapper(Resources.classForName(namespace));
        }
    }
}
