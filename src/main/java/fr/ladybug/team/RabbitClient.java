package fr.ladybug.team;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import javafx.application.Platform;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitClient {

    boolean isClosed;

    Channel channel;
    Connection connection;
    GUI gui;

    RabbitClient(String host, GUI gui) throws IOException, TimeoutException {
        isClosed = false;

        var factory = new ConnectionFactory();
        factory.setHost(host);

        connection = factory.newConnection();
        channel = connection.createChannel();
        this.gui = gui;

    }

    public void close() throws IOException, TimeoutException {
        if (!isClosed) {
            channel.close();
            connection.close();
            isClosed = true;
        }
    }

    public void messageReceived(String message, String targetName) {
//        System.out.println(String.format("We've got a %s into %s",
//                message, targetName));
        Platform.runLater(() -> {
            gui.addMessage(targetName, message);
        });
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
//        RabbitClient server = new RabbitClient("localhost", null);
//        server.subscribe("@navalny");
//        server.subscribe("@kerenskiy");
//        server.sendMessage("Hello", "@navalny");
//        server.sendMessage("Hellod gds fdf", "@kerenskiy");

    }
}
