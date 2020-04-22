package fr.ladybug.team;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import javafx.application.Platform;
import org.w3c.dom.xpath.XPathResult;

import java.io.IOException;
import java.util.Scanner;

public class MyServer {
    private static final Object object = new Object();
    private static StreamObserver<MessageRequest> streamObserver = null;
    private int port;
    private String name;
    MyClientUI parent;

    public MyServer(int port, String name, MyClientUI parent) {
        this.port = port;
        this.name = name;
        this.parent = parent;
    }

    public void start() throws IOException {
        Server server = ServerBuilder.forPort(port)
                .addService(new GreetingServiceImpl()).build();
        server.start();
        System.out.println("Server started");

        while (true) {
            synchronized (object) {
                if (streamObserver != null) {
                    break;
                }
            }
        }
    }

    public void sendMessage(String message) {
        streamObserver.onNext(MessageRequest.newBuilder()
                                      .setName(name)
                                      .setText(message)
                                      .build());
    }
    public class GreetingServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {
        @Override
        public StreamObserver<MessageRequest> chat(StreamObserver<MessageRequest> responseObserver) {
            synchronized (object) {
                if (streamObserver == null) {
                    streamObserver = responseObserver;
                }
            }
            return new StreamObserver<>() {
                @Override
                public void onNext(MessageRequest note) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            parent.addMessage(note.getName(), "", note.getText());
                        }
                    });
                }

                @Override
                public void onError(Throwable t) {
                }

                @Override
                public void onCompleted() {
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
