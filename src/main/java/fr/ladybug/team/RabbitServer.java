package fr.ladybug.team;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitServer {

//    private final static String QUEUE_NAME = "hello";

    boolean isClosed;

    Channel channel;
    Connection connection;

    RabbitServer() throws IOException, TimeoutException {
        isClosed = false;

        var factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();

    }

    public void close() throws IOException, TimeoutException {
        if (!isClosed) {
            channel.close();
            connection.close();
            isClosed = true;
        }
    }

    public void messageReceived(String message, String targetName) {
        System.out.println(String.format("We've got a %s into %s",
                message, targetName));
    }

    public void subscribe(String targetName) throws IOException {
        channel.exchangeDeclare(targetName, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, targetName, "");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            messageReceived(message, targetName);
        };
        channel.basicConsume(queueName, true,
                deliverCallback, consumerTag -> {});
    }

    public void sendMessage(String message, String targetName) throws IOException {
        channel.exchangeDeclare(targetName, "fanout");
        channel.basicPublish(targetName, "", null,
                message.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] argv) throws Exception {
        RabbitServer server = new RabbitServer();
        server.subscribe("@navalny");
        server.subscribe("@kerenskiy");
        server.sendMessage("Hello", "@navalny");
        server.sendMessage("Hellod gds fdf", "@kerenskiy");

    }
}
