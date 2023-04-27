package com.longer.ectool.flowintegration.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * FlowIntegrationBean.java
 *
 * @author jiangyanlong
 * @version 1.0
 * @Date 2023/4/27 09:28
 * @Description
 */
public class FlowIntegrationBean {
    private String spk;
    private String secret;
    private String url;
    private String accessTokenUrl;
    private String appid;
    private String userid;
    private String createFlowUrl;

    public String getCreateFlowUrl() {
        return createFlowUrl;
    }

    public void setCreateFlowUrl(String createFlowUrl) {
        this.createFlowUrl = createFlowUrl;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSpk() {
        return spk;
    }

    public void setSpk(String spk) {
        this.spk = spk;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public void setAccessTokenUrl(String accessTokenUrl) {
        this.accessTokenUrl = accessTokenUrl;
    }

    public static FlowIntegrationBean build(String spk, String secret, String url, String accessTokenUrl, String appid, String userid,String createFlowUrl) {
        FlowIntegrationBean flowIntegrationBean = new FlowIntegrationBean();
        flowIntegrationBean.setSpk(spk);
        flowIntegrationBean.setSecret(secret);
        flowIntegrationBean.setUrl(url);
        flowIntegrationBean.setAccessTokenUrl(accessTokenUrl);
        flowIntegrationBean.setAppid(appid);
        flowIntegrationBean.setUserid(userid);
        flowIntegrationBean.setCreateFlowUrl(createFlowUrl);
        return flowIntegrationBean;
    }

    public static Map<String,Object> map(FlowIntegrationBean flowIntegrationBean) {
        Map<String,Object> map = new HashMap<>();
        map.put("spk",flowIntegrationBean.getSpk());
        map.put("secret",flowIntegrationBean.getSecret());
        map.put("url",flowIntegrationBean.getUrl());
        map.put("accessTokenUrl",flowIntegrationBean.getAccessTokenUrl());
        map.put("appid",flowIntegrationBean.getAppid());
        map.put("userid",flowIntegrationBean.getUserid());
        map.put("createFlowUrl",flowIntegrationBean.getCreateFlowUrl());
        return map;
    }
}
