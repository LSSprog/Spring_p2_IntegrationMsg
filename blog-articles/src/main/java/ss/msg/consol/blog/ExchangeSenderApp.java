package ss.msg.consol.blog;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ExchangeSenderApp {
    private static final String EXCHANGE_NAME = "direct_exch";

    public static void main(String[] args) throws Exception { //IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()
        ){ channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            String message = "SS";
            channel.basicPublish(EXCHANGE_NAME, "php", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("сообщение " + message + " отправлено");

        }
    }
}
