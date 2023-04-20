package com.longer.ectool.base.service;

import com.intellij.openapi.project.Project;

import java.util.Map;

/**
 * OperationService.java
 *
 * @author jiangyanlong
 * @version 1.0
 * @Date 2023/4/20 09:53
 * @Description
 */
public interface OperationService {
    public boolean operate(Project project, Map<String, String> params) throws Exception;
}
