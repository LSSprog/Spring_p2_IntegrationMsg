package ss.msg.consol.sub;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ExchangeReceiverApp {
    private static final String EXCHANGE_NAME = "direct_exch";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String queueName = channel.queueDeclare().getQueue();
        System.out.println("Имя очереди: " + queueName);

        channel.queueBind(queueName, EXCHANGE_NAME, "php");
        System.out.println("Ждём сообщения...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Получено сообщение: " + message);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

    }
}
//DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String message = new String(delivery.getBody(), "UTF-8");
////            System.out.println(Thread.currentThread().getName());
//            System.out.println(" [x] Received '" + message + "'");
//        };
//
////        System.out.println(Thread.getAllStackTraces().keySet());
//        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });