package br.com.adotappet.pedidoapi.core.rabbitmq;

import br.com.adotappet.pedidoapi.domain.listener.NovoPedidoMessageListener;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NovoPedidoRabbitMQConfig extends AbstractRabbitMQConfig {
    private final static String NOVO_PEDIDO_MESSAGE_QUEUE = "novo-pedido-message-queue";

    @Override
    public String getRoutingKey() {
        return NOVO_PEDIDO_MESSAGE_QUEUE;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(NovoPedidoMessageListener receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
