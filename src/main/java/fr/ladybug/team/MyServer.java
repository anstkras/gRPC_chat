package fr.ladybug.team;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class MyServer {
    static public void main(String [] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8080)
                .addService(new GreetingServiceImpl()).build();

        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started!");
        server.awaitTermination();
    }

    public static class GreetingServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {
        @Override
        public void greeting(MessageRequest request, StreamObserver<MessageResponse> responseObserver) {
            System.out.println(request);

            String greeting = "Hello there, " + request.getName();

            MessageResponse response = MessageResponse.newBuilder().setGreeting(greeting).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
