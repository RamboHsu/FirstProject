package cn.itcast;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class Test {
    public static void main(String[] args) throws Exception {
        //1.创建一个配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //2.设置模板所在目录
        configuration.setDirectoryForTemplateLoading(new File("D:\\IdeaProjects\\pingyougou_parent\\demo_Freemarker\\src\\main\\resources"));
        //3.设置字符集
        configuration.setDefaultEncoding("utf-8");
        //4.获取模板对象
        Template template = configuration.getTemplate("test.ftl");
        //5.创建数据模型(可以是对象，也可以是map)
        Map map = new HashMap();
        map.put("name","Lily");
        map.put("message","Nice to meet you!");
        map.put("success",true);

        List goodsList = new ArrayList();
        Map goods1 = new HashMap();
        goods1.put("name","Apple");
        goods1.put("price",8.8);
        Map goods2 = new HashMap();
        goods2.put("name","Banana");
        goods2.put("price",7.7);
        Map goods3 = new HashMap();
        goods3.put("name","Orange");
        goods3.put("price",9.9);
        goodsList.add(goods1);
        goodsList.add(goods2);
        goodsList.add(goods3);

        map.put("goodsList",goodsList);

        map.put("today",new Date());

        map.put("score",9999999);

        //6.创建一个输出流对象
        Writer out = new FileWriter("d:\\test.html");
        //7.输出
        template.process(map,out);
        //8.关闭out
        out.close();

    }
}
