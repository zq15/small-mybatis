package com.zds.mybatis.builder.xml;

import com.zds.mybatis.builder.BaseBuilder;
import com.zds.mybatis.io.Resources;
import com.zds.mybatis.mapping.MappedStatement;
import com.zds.mybatis.mapping.SqlCommandType;
import com.zds.mybatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // 创建方法读取 root 节点下的mapper节点，解析 mapper 节点下所有子节点
    public Configuration parse() {
        // 解析mapper节点
        try {
            mapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return configuration;
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

                // 方法签名
                String msId = namespace + "." + id;
                SqlCommandType sqlCommandType = SqlCommandType.SELECT;
                // 创建一个 MappedStatement 对象
                MappedStatement mappedStatement = new MappedStatement.Builder(configuration, msId, sqlCommandType, parameterType,
                        resultType, sql, parameterMap).build();
                // 将 MappedStatement 对象存储到 configuration 中
                configuration.addMappedStatement(msId, mappedStatement);
            }

            // 注册接口
            configuration.addMapper(Resources.classForName(namespace));
        }
    }
}
