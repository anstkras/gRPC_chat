package fr.ladybug.team;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;


public class MyClientUI extends Application {
    private static final int BASE_SCREEN_WIDTH = 200;
    private static final int BASE_SCREEN_HEIGHT = 200;
    private static final int MIN_SCREEN_HEIGHT = 200;
    private static final int MIN_SCREEN_WIDTH = 100;

    private MyClient client;
    private static MyServer myServer;
    private ObservableList<Object> dataSupplier;

    public static void main(String[] args) {
        if (args.length == 2) { // we are server
            myServer = new MyServer(Integer.parseInt(args[0]), args[1]);
        }
        launch(args);
    }

    public void addMessage(String name, String time, String text) {
        dataSupplier.addAll("Name: " + name + "\nTime: " + time + "\n" + text);
    }

    public void sendMessage(String message) {
    }

    @Override
    public void start(Stage primaryStage) {
        dataSupplier = FXCollections.observableArrayList();
        var listView = new ListView<>(dataSupplier);

        final TextField comment = new TextField();
        comment.setPromptText("Enter your comment.");
        comment.setOnAction(actionEvent -> {
            sendMessage(comment.getText());
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