package org.example.app_nbp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MyApplication extends Application {
    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Walutozaur");

        TabPane tabPane = new TabPane();

        Tab tab1 = new Tab("Strona główna");
        Tab tab2 = new Tab("Wykres kursu walut");
        Tab tab3 = new Tab("Kursy walut i kalkulator");

        // Zakładka 1
        tab1.setContent(new StartTabBuilder(tabPane, tab2, tab3).build());
        tab1.setClosable(false);
        tabPane.getTabs().add(tab1);

        // Zakładka 2
        tab2.setContent( new Tab2Content().build(primaryStage));
        tab2.setClosable(false);
        tabPane.getTabs().add(tab2);

        // Zakładka 3
        SplitPane splitPane = new SplitPane();

        // Dodanie zawartości do lewej i prawej strony przez osobne klasy
        splitPane.getItems().add(new LeftPaneBuilder().build());
        splitPane.getItems().add(new RightPaneBuilder().build());

        splitPane.setDividerPositions(0.5);
        tab3.setContent(splitPane);
        tab3.setClosable(false);
        tabPane.getTabs().add(tab3);

        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());

        Image icon = new Image("https://cdn.pixabay.com/photo/2021/03/05/22/44/dinosaur-6072475_640.png");
        primaryStage.getIcons().add(icon);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // ignorowanie ostrzeżenia
        System.setErr(new java.io.PrintStream(new java.io.OutputStream() {
            @Override
            public void write(int b) {
            }
        }));
        launch();
    }
}
