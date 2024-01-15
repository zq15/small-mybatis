package com.zds.mybatis.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: 正则表达式测试，测试捕获组语法
 */
public class RegexTest {
    public static void main(String[] args) {
        // 测试文本，包含一些电子邮件地址
        String text = "Contact us at support@example.com, sales@example.net or admin@example.org";

        // 修改后的电子邮件地址的正则表达式
        // 使用捕获组分别获取用户名和域名
        String regex = "\\b([A-Za-z0-9._%+-]+)@([A-Za-z0-9.-]+\\.[A-Z|a-z]{2,7})\\b";

        // 编译正则表达式并创建 Matcher 对象
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        // 查找文本中的所有电子邮件地址
        while (matcher.find()) {
            // group(1) 返回电子邮件地址的用户名部分
            String localPart = matcher.group(1);
            // group(2) 返回电子邮件地址的域名部分
            String domainPart = matcher.group(2);

            System.out.println("Found email: " + localPart + "@" + domainPart);
            System.out.println("Username: " + localPart);
            System.out.println("Domain: " + domainPart);
        }
    }
}
