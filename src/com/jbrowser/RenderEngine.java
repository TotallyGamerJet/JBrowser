package com.jbrowser;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RenderEngine {

    private final WebEngine engine;

    public RenderEngine(WebEngine e) {
        this.engine = e;
    }

    public WebEngine getEngine() {
        return engine;
    }

    public static String getRoot(String URL) {
        final int protocolSepLoc = URL.indexOf("://");
        if (protocolSepLoc > 0) {
            final int pathSepLoc = URL.indexOf("/", protocolSepLoc + 3);
            return (pathSepLoc > 0) ? URL.substring(0, pathSepLoc) : URL;
        }
        return null;
    }

    public String loadData(String URL) {
        if(URL.contains(" ")) {
            engine.load("http://www.google.com/search?q=" + URL.replace(" ", "+"));
        } else {
            if (!URL.startsWith("http://") && !URL.startsWith("https://")) {
                URL = "http://" + URL;
            }
            engine.load(URL);
        }
        return URL;
    }

    public String getTitle() {
        Document doc = engine.getDocument();
        NodeList heads = doc.getElementsByTagName("head");
        String titleText = engine.getLocation() ; // use location if page does not define a title
        if (heads.getLength() > 0) {
            Element head = (Element)heads.item(0);
            NodeList titles = head.getElementsByTagName("title");
            if (titles.getLength() > 0) {
                Node title = titles.item(0);
                titleText = title.getTextContent();
            }
        }
        if(titleText.length() > 30) {
            titleText  = titleText.substring(0, Math.min(titleText.length(), 20));
        }
        return titleText;
    }

    //Adding functionality to the back button
    public String goBack() {
        final WebHistory history = engine.getHistory();
        ObservableList<WebHistory.Entry> entryList = history.getEntries();
        int currentIndex = history.getCurrentIndex();

        Platform.runLater(() -> history.go(-1));
        return entryList.get(currentIndex).getUrl();
    }

    //Adding functionality to the forward button
    public String goForward() {
        final WebHistory history = engine.getHistory();
        ObservableList<WebHistory.Entry> entryList = history.getEntries();
        int currentIndex = history.getCurrentIndex();

        Platform.runLater(() -> history.go(1));
        return entryList.get(currentIndex).getUrl();
    }
}