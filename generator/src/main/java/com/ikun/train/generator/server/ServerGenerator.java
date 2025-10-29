package com.ikun.train.generator.server;

import com.ikun.train.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ServerGenerator {
    // serverPath是目标生成的父文件夹位置
    static String serverPath = "[module]/src/main/java/com/ikun/train/[module]/";
    static String pomPath = "generator\\pom.xml";
    static {
        new File(serverPath).mkdirs();
    }

    public static void main(String[] args) throws Exception {
        // 获取src/main/resources/generator-config-xxx.xml文件路径
        String generatorPath = getGeneratorPath();
        // 比如generator-config-member.xml，得到module = member
        String module = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module: " + module);
        serverPath = serverPath.replace("[module]", module);
        // new File(serverPath).mkdirs();
        System.out.println("serverPath: " + serverPath);


        // 读取table节点
        Document document = new SAXReader().read("generator/" + generatorPath);
        Node table = document.selectSingleNode("//table");
        System.out.println(table);
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");
        System.out.println(tableName.getText() + "/" + domainObjectName.getText());

        // 示例：表名 jiawa_test
        // Domain = JiawaTest
        String Domain = domainObjectName.getText();
        // domain = jiawaTest
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        // do_main = jiawa-test
        String do_main = tableName.getText().replaceAll("_", "-");

        // 组装参数
        Map<String, Object> param = new HashMap<>();
        param.put("Domain", Domain);
        param.put("domain", domain);
        param.put("do_main", do_main);
        System.out.println("组装参数：" + param);

        gen(Domain, param,"service");
        gen(Domain, param,"controller");
    }

    private static void gen(String Domain, Map<String, Object> param, String target) throws IOException, TemplateException {
        // 用模版名初始化模版,target是目标模版的名字，比如service,controller
        FreemarkerUtil.initConfig(target + ".ftl");
        String toPath = serverPath + target + "/";  // 举例：这里的target相当于controller文件夹
        new File(toPath).mkdirs();
        String Target = target.substring(0,1).toUpperCase() + target.substring(1);
        String fileName = toPath + Domain + Target + ".java";
        System.out.println("开始生成：" + fileName);
        // 生成文件的最终位置
        FreemarkerUtil.generator(fileName, param);
    }

    // getGeneratorPath()作用：根据pom.xml读取配置文件路径
    private static String getGeneratorPath() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Map<String, String> map = new HashMap<String, String>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(pomPath);
        Node node = document.selectSingleNode("//pom:configurationFile");
        System.out.println(node.getText());
        return node.getText();
    }
}