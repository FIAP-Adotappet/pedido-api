package br.com.adotappet.pedidoapi.core.rabbitmq;

import br.com.adotappet.pedidoapi.core.rabbitmq.listener.NovoPedidoMessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class NovoPedidoRabbitMQConfig {
    public final static String NOVO_PEDIDO_MESSAGE_QUEUE = "novo-pedido-message-queue";
    private final NovoPedidoMessageListener messageListener;

    public NovoPedidoRabbitMQConfig(NovoPedidoMessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Bean
    Queue novoPedidoQueue() {
        return new Queue(NOVO_PEDIDO_MESSAGE_QUEUE, false);
    }

    @RabbitListener(queues = NOVO_PEDIDO_MESSAGE_QUEUE)
    public void listen(Map<String, Object> message) {
        messageListener.receiveMessage(message);
    }
}
