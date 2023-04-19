package com.longer.ectool.createrestful;

import com.intellij.openapi.project.Project;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.*;

/**
 * Opration.java
 *
 * @author jiangyanlong
 * @version 1.0
 * @Date 2023/4/19 14:02
 * @Description
 */
public class Opration {


    public boolean create(Project project, Map<String, String> params) throws Exception {
        // 接口名称前缀 如果是接口名称为：LongerAction，那么这里就是Longer
        String srcFilePath = params.get("srcFilePath");
        String classPreName = params.get("classPreName");
        // 文件存放路径，放在 api下面及engine下的目录，这个目录下存放对应的开发文件 如 longer，则创建文件会放在 api/longer下 以及 engine/longer下，前端输入格式为：longer
        // 如果有多级目录，则输入 longer.test
        String filePath = params.get("filePath");
        // 接口访问路径，如果接口路径为/api/longer/create，则输入longer
        String apiFirstPath = params.get("apiFirstPath");
        // 接口访问路径，如果接口路径为/api/longer/create，则输入/create 如果需要多个接口，输入/create;/update
        String apiRestPath = params.get("apiRestPath");

        String basePath = project.getBasePath();

        // /Users/jiangyanlong/Documents/dev/iead/gpt/src/main/java
        String templatePath = basePath + File.separator + srcFilePath;
        // 生成临时文件夹到ecology下, 在临时目录下创建当前时间戳

        // 生成API文件 ，把 filePath 中的 . 替换成 File.Seperator
        String apiPathTemp = templatePath + File.separator + Constants.COM + File.separator + Constants.API + File.separator + filePath.replace(Constants.DOT, File.separator);
        createDirecrory(apiPathTemp);
        String actionFilePath = apiPathTemp + File.separator + classPreName + Constants.ACTION_SUFFIX;
        Map<String,Object> apiData = createApi(filePath, classPreName, apiFirstPath);
        String apiFilePath = createFileByTemplate(actionFilePath, apiData, Constants.API_TEMPLATE);
        // 生成BIZ文件
        String bizPathTemp = templatePath + File.separator + Constants.COM + File.separator + Constants.ENGINE + File.separator + filePath.replace(Constants.DOT, File.separator) + File.separator + Constants.WEB;
        createDirecrory(bizPathTemp);
        String bizFilePath = bizPathTemp + File.separator + classPreName + Constants.BIZ_SUFFIX;
        Map<String,Object> bizData = createBizz(filePath, classPreName, apiRestPath);
        String bizFilePathTemp = createFileByTemplate(bizFilePath, bizData, Constants.BIZ_TEMPLATE);

        // 生成Service文件
        String servicePathTemp = templatePath + File.separator + Constants.COM + File.separator + Constants.ENGINE + File.separator + filePath.replace(Constants.DOT, File.separator) + File.separator + Constants.SERVICE;
        createDirecrory(servicePathTemp);
        String serviceFilePath = servicePathTemp + File.separator + classPreName + Constants.SERVICE_SUFFIX;
        Map<String,Object> serviceData = createService(filePath, classPreName, bizData);
        String serviceFilePathTemp = createFileByTemplate(serviceFilePath, serviceData, Constants.SERVICE_TEMPLATE);

        // 生成serviceImpl文件
        String serviceImplPathTemp = templatePath + File.separator + Constants.COM + File.separator + Constants.ENGINE + File.separator + filePath.replace(Constants.DOT, File.separator) + File.separator + Constants.SERVICE + File.separator + Constants.SERVICEIMPL;
        createDirecrory(serviceImplPathTemp);
        String serviceImplFilePath = serviceImplPathTemp + File.separator + classPreName + Constants.SERVICEIMPL_SUFFIX;
        Map<String,Object> serviceImplData = createServiceImpl(filePath, classPreName, serviceData);
        String serviceImplFilePathTemp = createFileByTemplate(serviceImplFilePath, serviceImplData, Constants.SERVICEIMPL_TEMPLATE);

        // 生成cmd文件
        String cmdPathTemp = templatePath + File.separator + Constants.COM + File.separator + Constants.ENGINE + File.separator + filePath.replace(Constants.DOT, File.separator) + File.separator + Constants.CMD;
        createDirecrory(cmdPathTemp);
        List<BizzBean> bizzBeanList = (List<BizzBean>) serviceData.get("lists");
        if(bizzBeanList != null && bizzBeanList.size() > 0) {
            for(BizzBean bizzBean : bizzBeanList) {
                String cmdFilePath = cmdPathTemp + File.separator + bizzBean.getCmdname() + Constants.CMD_SUFFIX;
                Map<String,Object> cmdData = createCmd(filePath, bizzBean.getCmdname());
                String cmdFilePathTemp = createFileByTemplate(cmdFilePath, cmdData, Constants.CMD_TEMPLATE);
            }
        }
        return true;
    }

    /**
     * 创建API入口文件
     * @param path 文件路径
     * @param className 类名
     * @param apiPath 接口路径
     * @return 模版填充数据
     */
    public Map<String,Object> createApi(String path, String className, String apiPath) {
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
    public Map<String,Object> createBizz(String path,String className,String apiPath) {
        Map<String, Object> data = new HashMap<>();
        data.put("package", "com.engine." + path + "." + Constants.WEB);
        data.put("path", path);
        data.put("className", className);
        List<BizzBean> list = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(apiPath, ";");
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            String cmdName = toUpperCaseFirstOne(token.substring(token.lastIndexOf("/") + 1));
            if(token.startsWith("/")) {
                list.add(new BizzBean(token, getRandomString(), cmdName));
            } else {
                list.add(new BizzBean("/" + token, getRandomString(), cmdName));
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
    public Map<String,Object> createService(String path,String className,Map<String,Object> bizData) {
        Map<String, Object> data = new HashMap<>();
        data.put("package", "com.engine." + path + "." + Constants.SERVICE);
        data.put("className", className);
        List<BizzBean> list = new ArrayList<>();
        for(Map.Entry<String,Object> map : bizData.entrySet()) {
            if(map.getKey().equals("lists")) {
                list = (List<BizzBean>) map.getValue();
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
    public Map<String,Object> createServiceImpl(String path,String className,Map<String,Object> bizData) {
        Map<String, Object> data = new HashMap<>();
        data.put("package", "com.engine." + path + "." + Constants.SERVICE + Constants.DOT + Constants.SERVICEIMPL);
        data.put("path", path);
        data.put("className", className);
        List<BizzBean> list = new ArrayList<>();
        for(Map.Entry<String,Object> map : bizData.entrySet()) {
            if(map.getKey().equals("lists")) {
                list = (List<BizzBean>) map.getValue();
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
    public Map<String,Object> createCmd(String path,String className) {
        Map<String, Object> data = new HashMap<>();
        data.put("package", "com.engine." + path + "." + Constants.CMD);
        data.put("className", className);
        return data;
    }

    /**
     * 随机生成长度为5的字符串
     * @return 随机字符串
     */
    public String getRandomString() {
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
    public String toUpperCaseFirstOne(String s) {
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
    public String createFileByTemplate(String filePath, Map<String, Object> data, String ftlName) throws Exception {
        Configuration cfg = new Configuration();
//        cfg.setDirectoryForTemplateLoading(new File(templatePath));
        cfg.setTemplateLoader(new ClassTemplateLoader(this.getClass(),  "/config"));
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
    public void createDirecrory(String tempPath) {
        // 如果目录不存在，则创建
        File tempFile = new File(tempPath);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
    }
}
