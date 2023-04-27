package com.longer.ectool.flowintegration.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.longer.ectool.base.service.OperationService;
import com.longer.ectool.base.service.impl.FlowIntegrationServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * FlowIntegrationDialog.java
 *
 * @author jiangyanlong
 * @version 1.0
 * @Date 2023/4/27 09:13
 * @Description
 */
public class FlowIntegrationDialog extends DialogWrapper {

    private final Project project;

    private final List<FlowIntegrationDialog.DialectRow> showDialectRowList = new ArrayList<>();

    private OperationService operationService = new FlowIntegrationServiceImpl();

    public FlowIntegrationDialog(@Nullable Project project) {
        super(project);
        this.project = project;
        this.initDefaultFields();
        this.init();
        Messages.showMessageDialog(this.project, "1. 如果已经执行过，请勿重复执行，可能会导致其他共用的系统调用失败. \n 2. 系统访问地址要能访问通 .\n 3.APPID确保已经在ECOLOGY_BIZ_EC表中注册过并刷新过表缓存（/commcache/cacheMonitor.jsp）", "", Messages.getWarningIcon());
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = JBUI.insets(5);
        gridBagConstraints.fill = 0;
        gridBagConstraints.anchor = 15;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        JScrollPane scrollPane = this.building();
        layout.setConstraints(scrollPane, gridBagConstraints);
        BorderLayout borderLayout = new BorderLayout();
        JPanel contentPane = new JPanel(borderLayout);
        Dimension dimension = new Dimension(600, 150);
        contentPane.setPreferredSize(dimension);
        contentPane.add(scrollPane);
        return contentPane;
    }

    private JScrollPane building() {
        VerticalFlowLayout centerLayout = new VerticalFlowLayout();
        JPanel dialectsPanel = new JPanel(centerLayout);
        showDialectRowList.forEach(dialectsPanel::add);
        return new JBScrollPane(dialectsPanel);
    }

    // 创建底部按钮
    protected Action @NotNull [] createActions() {
        return new Action[]{this.createAnAction("OK", this::ok), this.createAnAction("Cancel", this::cancel)};
    }

    // 创建底部按钮
    private DialogWrapper.DialogWrapperAction createAnAction(String name, final Consumer<ActionEvent> consumer) {
        return new DialogWrapper.DialogWrapperAction(name) {
            private static final long serialVersionUID = -62877809570565207L;

            protected void doAction(ActionEvent e) {
                consumer.accept(e);
            }
        };
    }

    // 取消
    private void cancel(ActionEvent actionEvent) {
        this.dispose();
    }

    private void ok(ActionEvent e) {
        Map<String, String> map = new HashMap<>();
        showDialectRowList.forEach(dialectRow -> {
            if (dialectRow.labelValue.getText().equals("")) {
                Messages.showMessageDialog(this.project, dialectRow.labelName.getText().replace(":", "") + " 不能为空", "", Messages.getErrorIcon());
                throw new RuntimeException(dialectRow.labelName.getText().replace(":", "") + " 不能为空");
            }
            map.put(dialectRow.labelValue.getName(), dialectRow.labelValue.getText());
        });

        try {
            operationService.operate(this.project, map);
            Messages.showMessageDialog(this.project, "Success!!!", "", Messages.getInformationIcon());
        } catch (Exception ex) {
            Messages.showMessageDialog(this.project, "Fail:" + ex.getMessage(), "", Messages.getErrorIcon());
            throw new RuntimeException("异常:" + ex.getMessage());
        } finally {
            this.dispose();
        }
    }

    public void initDefaultFields() {
        Stream<FlowIntegrationDialog.DialectRow> stream = Stream.of(
                new FlowIntegrationDialog.DialectRow("系统的地址", "url", "系统访问地址：如 http://localhost:8080 "),
                new FlowIntegrationDialog.DialectRow("系统注册码", "appid", "注册到 ECOLOGY_BIZ_EC 表的 APPID"),
                new FlowIntegrationDialog.DialectRow("生成的路径", "direct", "如：生成到工程目录下填写 / , 生成到工程目录下的子目录填写 /xxx/xxx"));
        stream.forEach(showDialectRowList::add);
    }

    static class DialectRow extends JPanel {
        private static final long serialVersionUID = -8068557897824883675L;
        JLabel labelName;
        JTextField labelValue;

        public DialectRow(String labelName, String jTextFieldId, String toolTipText) {
            GridBagLayout gridBagLayout = new GridBagLayout();
            this.setLayout(gridBagLayout);
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.anchor = 17;
            gridBagConstraints.insets = JBUI.insets(5);
            this.labelName = new JLabel(labelName + ":");
            this.labelName.setAlignmentX(1.0F);
            this.labelValue = new JTextField();
            this.labelValue.setPreferredSize(new Dimension(240, 30));
            this.labelValue.setAlignmentY(0.5F);
            this.labelValue.setToolTipText(toolTipText);
            this.labelValue.setName(jTextFieldId);
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.fill = 0;
            gridBagLayout.setConstraints(this.labelName, gridBagConstraints);
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.fill = 1;
            gridBagLayout.setConstraints(this.labelValue, gridBagConstraints);
            this.add(this.labelName);
            this.add(this.labelValue);
            this.setAlignmentY(0.5F);
        }
    }
}
