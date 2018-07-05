package com.idea.tools.view.action;

import com.idea.tools.dto.Server;
import com.idea.tools.view.BrowserPanel;
import com.idea.tools.view.ServerEditDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.util.IconUtil;

import javax.swing.*;

public abstract class AbstractEditServerAction extends AbstractBrowserPanelAction {

    private static final Icon ICON = IconUtil.getEditIcon();

    AbstractEditServerAction(BrowserPanel browserPanel) {
        super("Edit server", "", ICON, browserPanel);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        browserPanel.getSelectedValue(Server.class).ifPresent(ServerEditDialog::showDialog);
    }

    boolean isServerSelected() {
        return isSelected(Server.class);
    }

}
