package br.com.adotappet.pedidoapi.core.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoAceitoRabbitMQConfig {
    public static final String PEDIDO_ACEITO_MESSAGE_QUEUE = "pedido-aceito-message-queue";

    @Bean
    Queue pedidoAceitoQueue() {
        return new Queue(PEDIDO_ACEITO_MESSAGE_QUEUE, false);
    }

}
