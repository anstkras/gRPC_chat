package fr.ladybug.team;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class MyClientUI extends Application {
    private MyClient client;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        TextInputDialog remoteAddressSupplier = new TextInputDialog("ip.ad.dr.re:port");
        remoteAddressSupplier.setTitle("MyFTP");
        remoteAddressSupplier.setHeaderText("Welcome to MyFTP");
        remoteAddressSupplier.setContentText("Enter message");
        var result = remoteAddressSupplier.showAndWait();

        if (!result.isPresent()) {
            Platform.exit();
            return;
        }
    }
}