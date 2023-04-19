package com.longer.ectool;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.longer.ectool.createrestful.ViewDialog;

/**
 * EcTool.java
 *
 * @author jiangyanlong
 * @version 1.0
 * @Date 2023/4/18 15:07
 * @Description
 */
public class EcTool extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        ViewDialog viewDialog = new ViewDialog(e.getProject());
        viewDialog.showAndGet();
    }
}
