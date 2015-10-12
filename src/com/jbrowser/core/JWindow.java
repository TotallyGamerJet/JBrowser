package com.jbrowser.core;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class JWindow {

    private Stage stage;
    private Scene scene;
    private AnchorPane pane;
    private JTabPane tabPane;
    private Button addButton;

    public JWindow(Stage stage) {
        this.stage = stage;
        tabPane = new JTabPane(this);
        pane = new AnchorPane();
        addButton = new Button("+");

        tabPane.addTab();
        tabPane.getTabPane().setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        addButton.setOnAction(event -> {
            tabPane.getTabPane().getSelectionModel().select(tabPane.addTab());
            tabPane.getTabPane().setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        });

        AnchorPane.setTopAnchor(tabPane.getTabPane(), 5.0);
        AnchorPane.setLeftAnchor(tabPane.getTabPane(), 5.0);
        AnchorPane.setRightAnchor(tabPane.getTabPane(), 5.0);
        AnchorPane.setTopAnchor(addButton, 5.0);
        AnchorPane.setLeftAnchor(addButton, 10.0);

        pane.getChildren().addAll(tabPane.getTabPane(), addButton);

        scene = new Scene(pane, 1200, 800);
        scene.getStylesheets().add("resources/tabs.css");
        stage.setTitle("JBrowser");
        stage.setScene(scene);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public JTabPane getJTabPane() {
        return tabPane;
    }
}
