package com.jbrowser.core;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class JTabPane extends TabPane {

    public final TabPane tabPane;
    public final JWindow jWindow;
    public JTab latestTab;

    public JTabPane(JWindow jWindow) {
        tabPane = new TabPane();
        this.jWindow = jWindow;
    }

    public Tab addTab() {
        latestTab = new JTab(jWindow);
        tabPane.getTabs().add(latestTab.getTab());
        return latestTab.getTab();
    }

    public JTab getLatestTab() {
        return latestTab;
    }

    public TabPane getTabPane() {
        return tabPane;
    }
}