package com.jbrowser;

import com.jbrowser.core.JTab;
import com.jbrowser.core.JWindow;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.web.WebEngine;

public class ContextMenuHandler {

    private JTab tab;
    private JWindow jWindow;
    private WebEngine engine;

    public ContextMenuHandler(JTab tab) {
        this.jWindow = tab.getJWindow();
        this.engine = tab.getEngine();
        this.tab = tab;
    }

    public MenuItem getNewTab() {
        MenuItem newTab = new MenuItem("New Tab");
        newTab.setOnAction(e -> {
            jWindow.getJTabPane().getTabPane().getSelectionModel().select(jWindow.getJTabPane().addTab());
            jWindow.getJTabPane().getTabPane().setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        });
        return newTab;
    }

    public MenuItem getReload() {
        MenuItem reload = new MenuItem("Reload");
        reload.setOnAction(e -> engine.reload());
        return reload;
    }

    public MenuItem getDuplicate() {
        MenuItem duplicate = new MenuItem("Duplicate");
        duplicate.setOnAction(e -> {
                    jWindow.getJTabPane().addTab();
                    JTab jT = jWindow.getJTabPane().getLatestTab();
                    jT.getRenderEngine().loadData(tab.getEngine().getLocation());
                    jWindow.getJTabPane().getTabPane().getSelectionModel().select(jT.getTab());
                    jWindow.getJTabPane().getTabPane().setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
                }
        );
        return duplicate;
    }

    public MenuItem getCloseTab() {
        MenuItem closeTab = new MenuItem("Close Tab");
        closeTab.setOnAction(e -> {
            if(jWindow.getJTabPane().getTabPane().getTabs().size() > 1)
                jWindow.getJTabPane().getTabPane().getTabs().remove(tab.getTab());

            if (jWindow.getJTabPane().getTabPane().getTabs().size() <= 1)
                jWindow.getJTabPane().getTabPane().setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        });
        return closeTab;
    }
}
