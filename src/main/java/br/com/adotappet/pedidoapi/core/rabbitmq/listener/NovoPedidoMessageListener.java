package br.com.adotappet.pedidoapi.core.rabbitmq.listener;

import br.com.adotappet.pedidoapi.domain.service.PedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NovoPedidoMessageListener {
    private final PedidoService pedidoService;
    private final Logger log = LoggerFactory.getLogger(NovoPedidoMessageListener.class);

    public NovoPedidoMessageListener(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public void receiveMessage(Map<String, Object> message) {
        log.info("Received <" + message + ">");
        Long petId = Long.valueOf(message.get("pet_id").toString());
        Long usuarioId = Long.valueOf(message.get("usuario_id").toString());
        pedidoService.iniciaPedido(petId, usuarioId);
        log.info("Message processed...");
    }

}
