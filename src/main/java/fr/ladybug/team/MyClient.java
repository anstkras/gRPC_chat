package fr.ladybug.team;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import javafx.application.Platform;
import javafx.event.EventHandler;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Stream;

public class MyClient {

    private StreamObserver<MessageRequest> chat;
    private String username;
    private MyClientUI parent;

    public MyClient(int port, String ipAddress, String username, MyClientUI parent) {
        this.username = username;
        this.parent = parent;
        ManagedChannel channel = ManagedChannelBuilder.forAddress(ipAddress, port)
                .usePlaintext()
                .build();

        MessageServiceGrpc.MessageServiceStub stub =
                MessageServiceGrpc.newStub(channel);

        chat = stub.chat(
                new StreamObserver<>() {
                    @Override
                    public void onNext(MessageRequest note) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                parent.addMessage(note.getName(), note.getTime(), note.getText());
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onCompleted() {
                    }
                });
    }

    public void sendMessage(String text) {
        var note = MessageRequest.newBuilder().setName(username).setText(text).setTime(LocalDateTime.now().toString()).build();
        chat.onNext(note);
        parent.addMessage(note.getName(), note.getTime(), note.getText());
    }



//        while (true) {
//            Scanner scanner = new Scanner(System.in);
//            String message = scanner.nextLine();
//            chat.onNext(MessageRequest.newBuilder()
//                    .setName("client_name")
//                    .setText(message)
//                    .build());
//        }
//    }
}

