package fr.ladybug.team;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.filechooser.FileView;

public class MyClientUI extends Application {
    private static final int BASE_SCREEN_WIDTH = 200;
    private static final int BASE_SCREEN_HEIGHT = 200;
    private static final int MIN_SCREEN_HEIGHT = 200;
    private static final int MIN_SCREEN_WIDTH = 100;

    private MyClient client;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        var dataSupplier = FXCollections.observableArrayList();
        var listView = new ListView<>(dataSupplier);

        final TextField comment = new TextField();
        comment.setPromptText("Enter your comment.");
        comment.setOnAction(actionEvent -> {
            System.out.println(comment.getText());
            dataSupplier.addAll(comment.getText());
            comment.setText("");
        });

        GridPane pane = new GridPane();
        GridPane.setConstraints(comment, 0, 0);
        GridPane.setConstraints(listView, 0, 1);
        pane.setAlignment(Pos.CENTER);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
        pane.getColumnConstraints().add(cc);

        pane.getChildren().add(comment);
        pane.getChildren().add(listView);

        Scene scene = new Scene(pane, BASE_SCREEN_WIDTH, BASE_SCREEN_HEIGHT);
        primaryStage.setTitle("Messenger");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(MIN_SCREEN_HEIGHT);
        primaryStage.setMinWidth(MIN_SCREEN_WIDTH);
        primaryStage.show();

    }
}