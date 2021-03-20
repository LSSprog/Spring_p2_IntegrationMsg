package ss.msg.consol.sub;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ExchangeReceiverApp {
    private static final String EXCHANGE_NAME = "direct_exch";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        Scanner scr = new Scanner(System.in);

        String queueName = channel.queueDeclare().getQueue();
        System.out.println("Имя очереди: " + queueName);

        System.out.println("Выберите тему подписки, введите комнду в формате: [set_topic] [тема]");
        System.out.println("Возможные темы: java, php, js, C++");
        String cmd = "";

        do {
            cmd = scr.nextLine();
        } while (!cmd.startsWith("set_topic"));

            int i = cmd.indexOf(" ");
            String direct = cmd.substring(i + 1);
            scr.close();

            channel.queueBind(queueName, EXCHANGE_NAME, direct);
            System.out.println("Ждём новых статей по интересующей вас теме..." + direct);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Получена новая статья: " + message);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

    }
}
