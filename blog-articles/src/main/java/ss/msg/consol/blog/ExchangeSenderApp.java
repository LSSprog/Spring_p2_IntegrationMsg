package ss.msg.consol.blog;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ExchangeSenderApp {
    private static final String EXCHANGE_NAME = "direct_exch";

    public static void main(String[] args) throws Exception { //IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        String inputText = "";
        Scanner scr = new Scanner(System.in);


        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()
        ){ channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            while (!inputText.equals("exit")) {
                System.out.println("Введите вашу статью ф формате: [тема] [статья]");
                System.out.println("Возможные темы: java, php, js, C++");
                System.out.println("Для выхода напишите exit");
                inputText = scr.nextLine();
                int i = inputText.indexOf(" ");
                if (i > 0) {
                    String direct = inputText.substring(0, i);
                    String myMessage = inputText.substring(i + 1);

                    channel.basicPublish(EXCHANGE_NAME, direct /*"php"*/, null, myMessage.getBytes(StandardCharsets.UTF_8));
                    System.out.println("Статья " + myMessage + " отправлена");
                }
            }
        }
        scr.close();
    }
}
