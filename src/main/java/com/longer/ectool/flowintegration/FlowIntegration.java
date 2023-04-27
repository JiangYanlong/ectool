package com.longer.ectool.flowintegration;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.longer.ectool.flowintegration.dialog.FlowIntegrationDialog;
import org.jetbrains.annotations.NotNull;

/**
 * FlowIntegration.java
 *
 * @author jiangyanlong
 * @version 1.0
 * @Date 2023/4/27 09:13
 * @Description
 */
public class FlowIntegration extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        FlowIntegrationDialog flowIntegrationDialog = new FlowIntegrationDialog(e.getProject());
        flowIntegrationDialog.showAndGet();
    }
}
