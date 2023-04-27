package com.longer.ectool.base.service.impl;

import com.intellij.openapi.project.Project;
import com.longer.ectool.base.constants.Constants;
import com.longer.ectool.base.util.CreaterestfulUtil;
import com.longer.ectool.createrestful.bean.CmdBean;
import com.longer.ectool.base.service.OperationService;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * CreaterestfulOperationServiceImpl.java
 *
 * @author jiangyanlong
 * @version 1.0
 * @Date 2023/4/20 09:55
 * @Description
 */
public class CreaterestfulOperationServiceImpl implements OperationService {

    @Override
    public boolean operate(Project project, Map<String, String> params) throws Exception {
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
        CreaterestfulUtil.createDirecrory(apiPathTemp);
        String actionFilePath = apiPathTemp + File.separator + classPreName + Constants.ACTION_SUFFIX;
        Map<String,Object> apiData = CreaterestfulUtil.createApi(filePath, classPreName, apiFirstPath);
        String apiFilePath = CreaterestfulUtil.createFileByTemplate(actionFilePath, apiData, Constants.API_TEMPLATE);
        // 生成BIZ文件
        String bizPathTemp = templatePath + File.separator + Constants.COM + File.separator + Constants.ENGINE + File.separator + filePath.replace(Constants.DOT, File.separator) + File.separator + Constants.WEB;
        CreaterestfulUtil.createDirecrory(bizPathTemp);
        String bizFilePath = bizPathTemp + File.separator + classPreName + Constants.BIZ_SUFFIX;
        Map<String,Object> bizData = CreaterestfulUtil.createBizz(filePath, classPreName, apiRestPath);
        String bizFilePathTemp = CreaterestfulUtil.createFileByTemplate(bizFilePath, bizData, Constants.BIZ_TEMPLATE);

        // 生成Service文件
        String servicePathTemp = templatePath + File.separator + Constants.COM + File.separator + Constants.ENGINE + File.separator + filePath.replace(Constants.DOT, File.separator) + File.separator + Constants.SERVICE;
        CreaterestfulUtil.createDirecrory(servicePathTemp);
        String serviceFilePath = servicePathTemp + File.separator + classPreName + Constants.SERVICE_SUFFIX;
        Map<String,Object> serviceData = CreaterestfulUtil.createService(filePath, classPreName, bizData);
        String serviceFilePathTemp = CreaterestfulUtil.createFileByTemplate(serviceFilePath, serviceData, Constants.SERVICE_TEMPLATE);

        // 生成serviceImpl文件
        String serviceImplPathTemp = templatePath + File.separator + Constants.COM + File.separator + Constants.ENGINE + File.separator + filePath.replace(Constants.DOT, File.separator) + File.separator + Constants.SERVICE + File.separator + Constants.SERVICEIMPL;
        CreaterestfulUtil.createDirecrory(serviceImplPathTemp);
        String serviceImplFilePath = serviceImplPathTemp + File.separator + classPreName + Constants.SERVICEIMPL_SUFFIX;
        Map<String,Object> serviceImplData = CreaterestfulUtil.createServiceImpl(filePath, classPreName, serviceData);
        String serviceImplFilePathTemp = CreaterestfulUtil.createFileByTemplate(serviceImplFilePath, serviceImplData, Constants.SERVICEIMPL_TEMPLATE);

        // 生成cmd文件
        String cmdPathTemp = templatePath + File.separator + Constants.COM + File.separator + Constants.ENGINE + File.separator + filePath.replace(Constants.DOT, File.separator) + File.separator + Constants.CMD;
        CreaterestfulUtil.createDirecrory(cmdPathTemp);
        List<CmdBean> cmdBeanList = (List<CmdBean>) serviceData.get("lists");
        if(cmdBeanList != null && cmdBeanList.size() > 0) {
            for(CmdBean cmdBean : cmdBeanList) {
                String cmdFilePath = cmdPathTemp + File.separator + cmdBean.getCmdname() + Constants.CMD_SUFFIX;
                Map<String,Object> cmdData = CreaterestfulUtil.createCmd(filePath, cmdBean.getCmdname());
                String cmdFilePathTemp = CreaterestfulUtil.createFileByTemplate(cmdFilePath, cmdData, Constants.CMD_TEMPLATE);
            }
        }
        return true;
    }
}
