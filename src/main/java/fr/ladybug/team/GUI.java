package fr.ladybug.team;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.HashMap;


public class GUI extends Application {
    private static final int BASE_SCREEN_WIDTH = 200;
    private static final int BASE_SCREEN_HEIGHT = 400;
    private static final int MIN_SCREEN_HEIGHT = 200;
    private static final int MIN_SCREEN_WIDTH = 400;

    private HashMap<String, ObservableList<Object>> dataSupplier;
    private TabPane tabPane;
    private HashMap<String, Tab> channelMap;

    public static void main(String[] args) {
        launch(args);
    }

    public void addMessage(String channelName, String name, String time, String text) {
        dataSupplier.get(channelName).addAll("Name: " + name + "\nTime: " + time + "\n" + text);
    }

    public void sendMessage(String message, String channelName) {
//        if (client != null) {
//            client.sendMessage(message);
//        } else if (server != null) {
//            server.sendMessage(message);
//        }
    }

    public void joinChannel(String name) {
        if (channelMap.containsKey(name)) {
            return;
        }
        final TextField message = new TextField();
        message.setPromptText("enter message");

        message.setOnAction(actionEvent -> {
            sendMessage(message.getText(), name);
            message.setText("");
        });


//        var supplier = FXCollections.observableArrayList();
//        var listView = new ListView<>(supplier);
//        dataSupplier.put(name, supplier);

//        GridPane pane = new GridPane();
//        GridPane.setConstraints(message, 0, 0);
//        GridPane.setConstraints(listView, 0, 1);
//        pane.setAlignment(Pos.TOP_CENTER);

//        ColumnConstraints cc = new ColumnConstraints();
//        cc.setHgrow(Priority.ALWAYS);
//        pane.getColumnConstraints().add(cc);

//        pane.getChildren().add(message);
//        pane.getChildren().add(listView);

        Tab tab = new Tab(name, message);
        channelMap.put(name, tab);
        tabPane.getTabs().addAll(tab);
    }

    @Override
    public void start(Stage primaryStage) {
//        TextInputDialog remoteAddressSupplier = new TextInputDialog("ip.ad.dr.re:port");
//        remoteAddressSupplier.setTitle("Messenger");
//        remoteAddressSupplier.setHeaderText("Enter address and port");
//        remoteAddressSupplier.setContentText("Please either address:port or port");
//
//        TextInputDialog remoteNameSupplier = new TextInputDialog("Your Name");
//        remoteNameSupplier.setTitle("Messenger");
//        remoteNameSupplier.setHeaderText("Enter name");
//        remoteNameSupplier.setContentText("Please enter your name");
//        String name = "anon";
//        var result2 = remoteNameSupplier.showAndWait();
//        if (result2.isPresent()) {
//            name = result2.get();
//        }
//
//        while (true) {
//            var result = remoteAddressSupplier.showAndWait();
//
//            if (!result.isPresent()) {
//                Platform.exit();
//                return;
//            }
//
//            String[] userInput = result.get().split(":", 2);
//            System.out.println(userInput);
//
//            try {
//                if (userInput.length == 2) {
//                    int port = Integer.parseInt(userInput[1]);
//                    client = new MyClient(port, userInput[0] , name, this);
//                    break;
//                }
//                if (userInput.length == 1) {
//                    int port = Integer.parseInt(userInput[0]);
//                    server = new MyServer(port, name, this);
//                    server.start();
//                    break;
//                }
//            } catch (NumberFormatException | IOException ignore) {
//            }
//            var alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Input");
//            alert.setHeaderText("Incorrect");
//            alert.setContentText("Please follow the following pattern: host:port");
//            alert.showAndWait();
//        }

        channelMap = new HashMap();
        tabPane = new TabPane();

        final TextField channelName = new TextField();
        channelName.setPromptText("Enter a channel name.");
        Button channelButton = new Button("join channel");
        channelButton.setWrapText(true);

        channelButton.setOnAction(actionEvent -> {
            joinChannel(channelName.getText());
            channelName.setText("");
        });

        GridPane pane = new GridPane();
        GridPane.setConstraints(channelName, 0, 0);
        GridPane.setConstraints(channelButton, 1, 0);
        GridPane.setConstraints(tabPane, 0, 1);
        pane.setAlignment(Pos.TOP_CENTER);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
        pane.getColumnConstraints().add(cc);

        pane.getChildren().add(channelName);
        pane.getChildren().add(channelButton);
        pane.getChildren().add(tabPane);

        Scene scene = new Scene(pane, BASE_SCREEN_WIDTH, BASE_SCREEN_HEIGHT);
        primaryStage.setTitle("Messenger");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(MIN_SCREEN_HEIGHT);
        primaryStage.setMinWidth(MIN_SCREEN_WIDTH);
        primaryStage.show();
    }
}