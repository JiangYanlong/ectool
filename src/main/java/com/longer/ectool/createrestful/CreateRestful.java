package com.longer.ectool.createrestful;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.longer.ectool.createrestful.dialog.ViewDialog;

/**
 * CreateRestful.java
 *
 * @author jiangyanlong
 * @version 1.0
 * @Date 2023/4/18 15:07
 * @Description
 */
public class CreateRestful extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        ViewDialog viewDialog = new ViewDialog(e.getProject());
        viewDialog.showAndGet();
    }
}
