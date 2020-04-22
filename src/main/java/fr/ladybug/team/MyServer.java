package fr.ladybug.team;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.Scanner;

public class MyServer {
    private static final Object object = new Object();
    private static StreamObserver<MessageRequest> streamObserver = null;
    private int port;
    private String name;

    public MyServer(int port, String name) {
        this.port = port;
        this.name = name;
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

//        while (true) {
//            Scanner scanner = new Scanner(System.in);
//            String message = scanner.nextLine();
//            streamObserver.onNext(MessageRequest.newBuilder()
//                                          .setName(name)
//                                          .setText(message)
//                                          .build());
//        }
    }

    public void sendMessage(String message) {
        streamObserver.onNext(MessageRequest.newBuilder()
                                      .setName(name)
                                      .setText(message)
                                      .build());
    }
    public static class GreetingServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {
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
                    System.out.println(note.getText());
                }

                @Override
                public void onError(Throwable t) {
                    //logger.log(Level.WARNING, "routeChat cancelled");
                }

                @Override
                public void onCompleted() {
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
