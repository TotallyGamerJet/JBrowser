package com.jbrowser.core;

import com.jbrowser.ContextMenuHandler;
import com.jbrowser.FavIconHandler;
import com.jbrowser.RenderEngine;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JTab {

    private Tab tab;
    private ImageView imgView;
    private Image logo;
    private BorderPane root;
    private Button reloadButton, backButton, forwardButton;
    private TextField field;
    private WebView view;
    private WebEngine engine;

    private ContextMenuHandler cm;
    private RenderEngine re;
    private JWindow jWindow;

    public JTab(JWindow jWindow) {
        this.jWindow =  jWindow;
        this.view = new WebView();
        this.engine = view.getEngine();
        this.re = new RenderEngine(engine);
        this.cm = new ContextMenuHandler(this);

        tab = new Tab();
        tab.setText("New Tab");
        tab.setOnClosed(event2 -> {
            if (jWindow.getJTabPane().getTabPane().getTabs().size() == 1)
                jWindow.getJTabPane().getTabPane().setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        });
        ContextMenu tabC = new ContextMenu();
        tabC.getItems().addAll(cm.getNewTab(), new SeparatorMenuItem(), cm.getReload(), cm.getDuplicate(), new SeparatorMenuItem(), cm.getCloseTab());
        tab.setContextMenu(tabC);

        logo = new Image("resources/unknown-document.png");
        imgView = new ImageView(logo);
        tab.setGraphic(imgView);

        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER);

        reloadButton = new Button("");
        reloadButton.setGraphic(new ImageView(new Image("resources/reload.png")));
        backButton = new Button("<");
        forwardButton = new Button(">");
        backButton.setDisable(true);
        forwardButton.setDisable(true);

        reloadButton.setOnAction(event1 -> {
            if(reloadButton.getText().equals("X")) {
                engine.getLoadWorker().cancel();
            }
            engine.reload();
            logo = new Image("resources/loading.gif");
            imgView = new ImageView(logo);
            tab.setGraphic(imgView);
            field.setText(engine.getLocation());
        });
        backButton.setOnAction(event1 -> re.loadData(re.goBack()));
        forwardButton.setOnAction(event1 -> re.loadData(re.goForward()));

        //The TextField for entering web addresses.
        field = new TextField("Search or Enter URL");
        field.setPrefColumnCount(50); //make the field at least 50 columns wide.
        field.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> { //When click on field entire thing selected
            Platform.runLater(() -> {
                if (field.isFocused() && !field.getText().isEmpty()) {
                    field.selectAll();
                }
            });
        });
        field.setOnKeyPressed(event -> { //When ENTER is pressed it will load page
            if (event.getCode() == KeyCode.ENTER) {
                if (!field.getText().isEmpty()) {
                    re.loadData(field.getText());
                }
            }
        });

        //Add all out navigation nodes to the vbox.
        hBox.getChildren().addAll(backButton, forwardButton, reloadButton, field);

        engine.setJavaScriptEnabled(true);
        engine.setOnAlert(event -> {
            Stage popup = new Stage();
            popup.initOwner(jWindow.getStage());
            popup.initStyle(StageStyle.UTILITY);
            popup.initModality(Modality.WINDOW_MODAL);

            StackPane content = new StackPane();
            Label label = new Label("The page at " + RenderEngine.getRoot(engine.getLocation()) + " is saying:" + "\n\n" + event.getData());
            label.setTextAlignment(TextAlignment.CENTER);
            content.getChildren().setAll(label);
            content.setPrefSize(300, 100);
            popup.setScene(new Scene(content));
            popup.showAndWait();
        });
        engine.setCreatePopupHandler(event -> new JWindow(new Stage()).getJTabPane().getLatestTab().getEngine());
        //engine.setConfirmHandler(event -> {});
        engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                reloadButton.setText("");
                reloadButton.setGraphic(new ImageView(new Image("resources/reload.png")));

                tab.setText(re.getTitle());
                tab.setGraphic(FavIconHandler.getInstance().fetchFavIcon(engine.getLocation()));

                int current = engine.getHistory().getCurrentIndex();
                if (current > 0) { // 3 > 0
                    backButton.setDisable(false);
                } else {
                    backButton.setDisable(true);
                }
                if (current < engine.getHistory().getEntries().size() - 1) { //5 < 6
                    forwardButton.setDisable(false);
                } else { // 6 == 6
                    forwardButton.setDisable(true);
                }
            } else if (newState == Worker.State.RUNNING) {
                reloadButton.setText("X");
                reloadButton.setGraphic(null);

                logo = new Image("resources/loading.gif");
                imgView = new ImageView(logo);
                tab.setGraphic(imgView);
                field.setText(engine.getLocation());
                tab.setText(engine.getLocation());
            }
        });

        re.loadData("google.com");

        root = new BorderPane();
        root.setPrefSize(1024, 768);
        root.setTop(hBox);
        root.setCenter(view);

        tab.setContent(root);
    }

    public RenderEngine getRenderEngine() {
        return re;
    }

    public JWindow getJWindow() {
        return jWindow;
    }

    public WebEngine getEngine() {
        return engine;
    }

    public Tab getTab() {
        return tab;
    }
}
