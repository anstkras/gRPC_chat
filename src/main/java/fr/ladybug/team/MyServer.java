package fr.ladybug.team;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.Scanner;

public class MyServer {
    private static final Object object = new Object();
    private static StreamObserver<MessageRequest> streamObserver = null;

    static public void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8080)
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
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            streamObserver.onNext(MessageRequest.newBuilder()
                                          .setName("server_name")
                                          .setText(message)
                                          .build());
        }
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
