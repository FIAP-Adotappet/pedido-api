package br.com.adotappet.pedidoapi.core.rabbitmq;

public class PedidoAceitoRabbitMQConfig extends AbstractRabbitMQConfig{
    private static final String PEDIDO_ACEITO_MESSAGE_QUEUE = "pedido-aceito-message-queue";

    @Override
    public String getRoutingKey() {
        return PEDIDO_ACEITO_MESSAGE_QUEUE;
    }
}
