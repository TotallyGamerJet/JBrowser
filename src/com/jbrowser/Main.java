package com.jbrowser;

import java.io.IOException;

import com.jbrowser.core.JWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        new JWindow(stage);
    }


    public static void main(String[] args) throws IOException {
        Application.launch(args);
    }
}
