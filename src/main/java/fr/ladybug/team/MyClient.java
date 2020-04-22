package fr.ladybug.team;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class MyClient {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        MessageServiceGrpc.MessageServiceBlockingStub stub =
                MessageServiceGrpc.newBlockingStub(channel);
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            MessageResponse messageResponse = stub.greeting(
                    MessageRequest.newBuilder()
                            .setName("Lady Bug")
                            .setText(message)
                            .build());

            System.out.println(messageResponse);
        }
       // channel.shutdown();
    }
}
