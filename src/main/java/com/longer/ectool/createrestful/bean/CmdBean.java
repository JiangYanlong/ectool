package com.longer.ectool.createrestful.bean;

/**
 * CmdBean.java
 *
 * @author jiangyanlong
 * @version 1.0
 * @Date 2023/4/19 14:04
 * @Description
 */
public class CmdBean {

    public CmdBean(String path, String random, String cmdname) {
        this.path = path;
        this.random = random;
        this.cmdname = cmdname;
    }

    public String path;
    public String random;
    public String cmdname;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getCmdname() {
        return cmdname;
    }

    public void setCmdname(String cmdname) {
        this.cmdname = cmdname;
    }
}
