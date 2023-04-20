package com.longer.ectool.base.util;

import com.longer.ectool.createrestful.bean.CmdBean;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * CreaterestfulUtil.java
 *
 * @author jiangyanlong
 * @version 1.0
 * @Date 2023/4/20 09:56
 * @Description
 */
public class CreaterestfulUtil {

    /**
     * 创建API入口文件
     * @param path 文件路径
     * @param className 类名
     * @param apiPath 接口路径
     * @return 模版填充数据
     */
    public static Map<String,Object> createApi(String path, String className, String apiPath) {
        Map<String, Object> data = new HashMap<>();
        data.put("package", "com.api." + path);
        data.put("parentPackage", "com.engine." + path + ".web");
        data.put("className", className);
        if(apiPath.startsWith("")) {
            data.put("path", "/" + apiPath);
        } else {
            data.put("path", apiPath);
        }
        return data;
    }

    /**
     * 创建API入口文件
     * @param path 文件路径
     * @param className 类名
     * @param apiPath 接口路径
     * @return 模版填充数据
     */
    public static Map<String,Object> createBizz(String path,String className,String apiPath) {
        Map<String, Object> data = new HashMap<>();
        data.put("package", "com.engine." + path + "." + Constants.WEB);
        data.put("path", path);
        data.put("className", className);
        List<CmdBean> list = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(apiPath, ";");
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            String cmdName = toUpperCaseFirstOne(token.substring(token.lastIndexOf("/") + 1));
            if(token.startsWith("/")) {
                list.add(new CmdBean(token, getRandomString(), cmdName));
            } else {
                list.add(new CmdBean("/" + token, getRandomString(), cmdName));
            }
        }
        data.put("lists",list);
        return data;
    }

    /**
     * 创建SERVICE入口文件
     * @param path 文件路径
     * @param className 类名
     * @param bizData biz中调用service方法名称
     * @return 模版填充数据
     */
    public static Map<String,Object> createService(String path,String className,Map<String,Object> bizData) {
        Map<String, Object> data = new HashMap<>();
        data.put("package", "com.engine." + path + "." + Constants.SERVICE);
        data.put("className", className);
        List<CmdBean> list = new ArrayList<>();
        for(Map.Entry<String,Object> map : bizData.entrySet()) {
            if(map.getKey().equals("lists")) {
                list = (List<CmdBean>) map.getValue();
            }
        }
        data.put("lists",list);
        return data;
    }

    /**
     * 创建SERVICEIMPL入口文件
     * @param path 文件路径
     * @param className 类名
     * @param bizData biz中调用service方法名称
     * @return 模版填充数据
     */
    public static Map<String,Object> createServiceImpl(String path,String className,Map<String,Object> bizData) {
        Map<String, Object> data = new HashMap<>();
        data.put("package", "com.engine." + path + "." + Constants.SERVICE + Constants.DOT + Constants.SERVICEIMPL);
        data.put("path", path);
        data.put("className", className);
        List<CmdBean> list = new ArrayList<>();
        for(Map.Entry<String,Object> map : bizData.entrySet()) {
            if(map.getKey().equals("lists")) {
                list = (List<CmdBean>) map.getValue();
            }
        }
        data.put("lists",list);
        return data;
    }


    /**
     * 创建CMD入口文件
     * @param path 文件路径
     * @param className 类名
     * @return 模版填充数据
     */
    public static Map<String,Object> createCmd(String path,String className) {
        Map<String, Object> data = new HashMap<>();
        data.put("package", "com.engine." + path + "." + Constants.CMD);
        data.put("className", className);
        return data;
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

    /**
     * 生成文件,返回文件路径
     * @param filePath 生成文件路径
     * @param data 模版填充数据
     * @param ftlName 模版名称
     * @return 文件路径
     * @throws Exception 异常
     */
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
