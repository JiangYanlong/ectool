package com.longer.ectool.base.util;

import com.longer.ectool.base.constants.Constants;
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
public class CreaterestfulUtil extends AbstactHelpUtil {

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
}
