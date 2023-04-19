package com.longer.ectool.createrestful;

import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
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
 * ViewDialog.java
 *
 * @author jiangyanlong
 * @version 1.0
 * &#064;Date  2023/4/19 09:15
 * &#064;Description
 */
public class ViewDialog extends DialogWrapper {

    private NotificationGroup toolWindow;

    private Project project;

    // 初始化默认字段
    private final List<DialectRow> list = new ArrayList<>();

    // 构造方法
    public ViewDialog(@Nullable Project project) {
        super(project);
        this.project = project;
        this.initDefaultFields();
        this.init();
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
        JScrollPane scrollPane = this.buildMainPane();
        layout.setConstraints(scrollPane, gridBagConstraints);
        BorderLayout borderLayout = new BorderLayout();
        JPanel contentPane = new JPanel(borderLayout);
        Dimension dimension = new Dimension(600, 250);
        contentPane.setPreferredSize(dimension);
        contentPane.add(scrollPane);
        return contentPane;
    }

    private JScrollPane buildMainPane() {
        VerticalFlowLayout centerLayout = new VerticalFlowLayout();
        JPanel dialectsPanel = new JPanel(centerLayout);
        list.forEach(dialectsPanel::add);
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
        Map<String,String> map = new HashMap<>();
        list.forEach(dialectRow -> {
            if(dialectRow.labelValue.getText().equals("")) {
                Notification toolWindowNotification = toolWindow.createNotification(dialectRow.labelName.getText().replace(":","") + " 不能为空", NotificationType.ERROR);
                Notifications.Bus.notify(toolWindowNotification);
                throw new RuntimeException(dialectRow.labelName.getText().replace(":","") + " 不能为空");
            }
            map.put(dialectRow.labelValue.getName(),dialectRow.labelValue.getText());
        });

        Opration opration = new Opration();
        try {
            opration.create(this.project,map);
            Notification toolWindowNotification = toolWindow.createNotification("生成成功!", NotificationType.INFORMATION);
            Notifications.Bus.notify(toolWindowNotification);
        } catch (Exception ex) {
            Notification toolWindowNotification = toolWindow.createNotification("异常:" + ex.getMessage(), NotificationType.ERROR);
            Notifications.Bus.notify(toolWindowNotification);
            throw new RuntimeException("异常:" + ex.getMessage());
        } finally {
            this.dispose();
        }
    }

    public void initDefaultFields() {
        // 获取通知组管理器
        NotificationGroupManager manager = NotificationGroupManager.getInstance();
        this.toolWindow = manager.getNotificationGroup("ec-tool.notification.tool.window");
        Stream<DialectRow> stream = Stream.of(new DialectRow("文件路径", "srcFilePath", "文件生成到的目录，如：/src/main/java")
                , new DialectRow("接口名称", "classPreName", "接口名称：如LongerAction,则只需要输入Longer")
                , new DialectRow("文件路径", "filePath", "文件路径：如存放src/com/api/longer/test,则只需要输入longer.test")
                , new DialectRow("一级路径", "apiFirstPath", "一级路径：如访问接口为/api/longer/create,则只需要输入longer")
                , new DialectRow("剩余路径", "apiRestPath", "剩余路径：如访问接口为/api/longer/create,则只需要输入create,如果要生成多个路径如/api/longer/create /api/longer/update 需要填写/create;/update"));
        stream.forEach(list::add);
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
