package fr.ladybug.team;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Scanner;

public class MyClient {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        MessageServiceGrpc.MessageServiceStub stub =
                MessageServiceGrpc.newStub(channel);

        StreamObserver<MessageRequest> chat = stub.chat(
                new StreamObserver<>() {
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
                    }
                });
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            chat.onNext(MessageRequest.newBuilder()
                                .setName("client_name")
                                .setText(message)
                                .build());
        }
    }
}

