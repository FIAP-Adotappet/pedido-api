package br.com.adotappet.pedidoapi.core.rabbitmq;

import br.com.adotappet.pedidoapi.core.rabbitmq.listener.NovoPedidoMessageListener;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;

public class NovoPedidoRabbitMQConfig extends AbstractRabbitMQConfig {
    public final static String NOVO_PEDIDO_MESSAGE_QUEUE = "novo-pedido-message-queue";

    @Bean
    Queue novoPedidoQueue() {
        return new Queue(NOVO_PEDIDO_MESSAGE_QUEUE, false);
    }

    @Bean
    Binding pedidoAceitoBinding(TopicExchange exchange) {
        return BindingBuilder.bind(novoPedidoQueue()).to(exchange).with(NOVO_PEDIDO_MESSAGE_QUEUE);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(NovoPedidoMessageListener receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
