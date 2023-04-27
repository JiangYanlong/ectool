package com.longer.ectool.base.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.project.Project;
import com.longer.ectool.base.constants.Constants;
import com.longer.ectool.base.service.OperationService;
import com.longer.ectool.base.util.CreaterestfulUtil;
import com.longer.ectool.base.util.FlowIntegrationUtil;
import com.longer.ectool.flowintegration.bean.FlowIntegrationBean;

import java.io.File;
import java.util.Map;

/**
 * CreaterestfulOperationServiceImpl.java
 *
 * @author jiangyanlong
 * @version 1.0
 * @Date 2023/4/20 09:55
 * @Description
 */
public class FlowIntegrationServiceImpl implements OperationService {

    @Override
    public boolean operate(Project project, Map<String, String> params) throws Exception {
        String url = params.get("url");
        String appid = params.get("appid");
        String direct = params.get("direct");
        String basePath = project.getBasePath();
        FlowIntegrationBean flowIntegrationBean = register(url,appid);
        String path = basePath+File.separator+direct;
        FlowIntegrationUtil.createDirecrory(path);
        FlowIntegrationUtil.createFileByTemplate(path+File.separator+ Constants.MD_SUFFIX,FlowIntegrationBean.map(flowIntegrationBean),Constants.MD_TEMPLATE);
        return true;
    }

    public static FlowIntegrationBean register(String address, String appid){
        //获取当前系统RSA加密的公钥
        RSA rsa = new RSA();
        String publicKey = rsa.getPublicKeyBase64();
        String data = HttpRequest.post(address + "/api/ec/dev/auth/regist")
                .header("appid",appid)
                .header("cpk",publicKey)
                .timeout(2000)
                .execute().body();
        Map<String,Object> datas = JSONUtil.parseObj(data);
        String spk = StrUtil.nullToEmpty((String)datas.get("spk"));
        String secret = StrUtil.nullToEmpty((String)datas.get("secrit"));
        return FlowIntegrationBean.build(spk,secret,address,address+"/api/ec/dev/auth/applytoken",appid,"1","/api/workflow/paService/doCreateRequest");
    }
}
