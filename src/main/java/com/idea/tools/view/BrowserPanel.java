/*
 * Copyright (c) 2013 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idea.tools.view;

import com.idea.tools.dto.Server;
import com.idea.tools.settings.Settings;
import com.idea.tools.view.action.AddServerAction;
import com.idea.tools.view.action.RemoveServerAction;
import com.idea.tools.view.render.TreeRender;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.TreeSpeedSearch;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.Tree;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.List;

import static com.idea.tools.ApplicationManager.*;
import static com.idea.tools.JmsMessengerWindowManager.JMS_MESSENGER_WINDOW_ID;
import static com.idea.tools.markers.Listener.simple;
import static com.idea.tools.utils.GuiUtils.installActionGroupInToolBar;
import static com.intellij.ui.PopupHandler.installPopupHandler;

public class BrowserPanel extends SimpleToolWindowPanel implements Disposable {

    private static final Logger logger = Logger.getLogger(BrowserPanel.class);

    private static final String UNAVAILABLE = "No server server available";

    private static final String LOADING = "Loading...";
    private final Tree serversTree;
    private final Settings settings;
    private JPanel rootPanel;
    private JPanel serverPanel;

    public BrowserPanel(final Project project) {
        super(true);
        settings = settings();
        setProvideQuickActions(false);
        serversTree = createTree();

        serverService().addListener(simple(server -> fillServerTree()));

        serverPanel.setLayout(new BorderLayout());
        serverPanel.add(ScrollPaneFactory.createScrollPane(serversTree), BorderLayout.CENTER);

        setContent(rootPanel);
    }

    public static BrowserPanel of() {
        return fetch(BrowserPanel.class);
    }

    @Override
    public void dispose() {
        toolWindowManager().unregisterToolWindow(JMS_MESSENGER_WINDOW_ID);
    }

    public void init() {
        initGui();
    }

    private void initGui() {
        installActionsInToolbar();
        installActionsInPopupMenu();
        fillServerTree();
    }

    private void installActionsInToolbar() {
        DefaultActionGroup actionGroup = new DefaultActionGroup("JmsMessengerToolbarGroup", false);
        actionGroup.add(new AddServerAction(this));
        actionGroup.add(new RemoveServerAction(this));

        installActionGroupInToolBar(actionGroup, this, "JmsMessengerBrowserActions");
    }

    private void installActionsInPopupMenu() {
        DefaultActionGroup popupGroup = new DefaultActionGroup("JmsMessengerPopupAction", true);

        installPopupHandler(serversTree, popupGroup, "POPUP", ActionManager.getInstance());
    }

    private void fillServerTree() {
        List<Server> servers = settings.getState().getServersList();

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(servers);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        serversTree.setModel(model);

        servers.forEach(s -> {
            DefaultMutableTreeNode jobNode = new DefaultMutableTreeNode(s);
            fillQueueTree(s, jobNode);
            rootNode.add(jobNode);
        });

        serversTree.setRootVisible(true);
    }

    private void fillQueueTree(Server server, DefaultMutableTreeNode serverNode) {
        server.getQueues().forEach(queue -> serverNode.add(new DefaultMutableTreeNode(queue)));
    }

    private Tree createTree() {

        SimpleTree tree = new SimpleTree();
        tree.getEmptyText().setText(LOADING);
        tree.setCellRenderer(new TreeRender());
        tree.setName("serverTree");

        new TreeSpeedSearch(tree, treePath -> {
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            final Object userObject = node.getUserObject();
            if (userObject instanceof Server) {
                return ((Server) userObject).getName();
            }
            return "<empty>";
        });

        return tree;
    }

}