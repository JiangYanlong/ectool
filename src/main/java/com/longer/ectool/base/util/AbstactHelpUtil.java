package com.longer.ectool.base.util;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.Random;

/**
 * AbstactHelpUtil.java
 *
 * @author jiangyanlong
 * @version 1.0
 * @Date 2023/4/27 10:14
 * @Description
 */
public class AbstactHelpUtil {

    public static Map<String,Object> createApi(String path, String className, String apiPath){
        return null;
    }

    public static Map<String,Object> createBizz(String path,String className,String apiPath) {
        return null;
    }

    public static Map<String,Object> createService(String path,String className,Map<String,Object> bizData) {
        return null;
    }

    public static Map<String,Object> createServiceImpl(String path,String className,Map<String,Object> bizData) {
        return null;
    }

    public static Map<String,Object> createCmd(String path,String className) {
        return null;
    }

    /**
     * 随机生成长度为5的字符串
     * @return 随机字符串
     */
    public static String getRandomString() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 5; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 字符串首字母转大写
     * @param s 字符串
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    public static String createFileByTemplate(String filePath, Map<String, Object> data, String ftlName) throws Exception {
        Configuration cfg = new Configuration();
        cfg.setTemplateLoader(new ClassTemplateLoader(CreaterestfulUtil.class,  "/config"));
        // 获取模板对象
        Template template = cfg.getTemplate(ftlName);
        // 渲染模板并输出结果
        File file = new File(filePath);
        FileWriter writer = new FileWriter(file);
        template.process(data, writer);
        writer.close();
        return file.getAbsolutePath();
    }

    /**
     * 文件夹不存在则创建
     * @param tempPath 文件夹路径
     */
    public static void createDirecrory(String tempPath) {
        // 如果目录不存在，则创建
        File tempFile = new File(tempPath);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
    }
}
