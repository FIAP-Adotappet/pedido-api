package br.com.adotappet.pedidoapi.domain.service;

import br.com.adotappet.pedidoapi.api.dto.PedidoDTO;
import br.com.adotappet.pedidoapi.core.rabbitmq.PedidoAceitoRabbitMQConfig;
import br.com.adotappet.pedidoapi.domain.entity.Pedido;
import br.com.adotappet.pedidoapi.domain.exception.BusinessException;
import br.com.adotappet.pedidoapi.domain.repository.PedidoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ModelMapper modelMapper;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, ModelMapper modelMapper, RabbitTemplate rabbitTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.modelMapper = modelMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void iniciaPedido(Long usuarioId, Long petId) {
        Pedido pedido = Pedido.builder()
                .usuarioId(usuarioId)
                .petId(petId)
                .status(Pedido.Status.ABERTO)
                .build();

        pedidoRepository.save(pedido);
    }

    private PedidoDTO aprovaRejeitaPedido(Long id, boolean aceito) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        if (!pedido.getStatus().equals(Pedido.Status.ABERTO)) {
            throw new BusinessException("Pedido não está em aberto");
        }
        pedido.setStatus(aceito ? Pedido.Status.ACEITO : Pedido.Status.REJEITADO);
        sendRabbitMq(id, aceito);
        return toPedidoDTO(pedidoRepository.save(pedido));
    }

    private void sendRabbitMq(Long id, boolean aceito) {
        Map<String, Object> actionMap = Map.of("pedido_id", id, "aceito", aceito);
        rabbitTemplate.convertAndSend(PedidoAceitoRabbitMQConfig.PEDIDO_ACEITO_MESSAGE_QUEUE, actionMap);
    }

    public PedidoDTO aprovaPedido(Long id) {
        return aprovaRejeitaPedido(id, true);
    }

    public PedidoDTO rejeitaPedido(Long id) {
        return aprovaRejeitaPedido(id, false);
    }

    public PedidoDTO finalizaPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        if (pedido.getStatus().equals(Pedido.Status.ABERTO)) {
            throw new BusinessException("Pedido está em aberto");
        }
        if (pedido.getStatus().equals(Pedido.Status.REJEITADO)) {
            throw new BusinessException("Pedido está rejeitado");
        }
        pedido.setStatus(Pedido.Status.FINALIZADO);
        return toPedidoDTO(pedidoRepository.save(pedido));
    }

    public PedidoDTO cancelaPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        if (pedido.getStatus().equals(Pedido.Status.FINALIZADO)) {
            throw new BusinessException("Pedido já foi finalizado");
        }
        if (pedido.getStatus().equals(Pedido.Status.REJEITADO)) {
            throw new BusinessException("Pedido está rejeitado");
        }
        pedido.setStatus(Pedido.Status.CANCELADO);
        return toPedidoDTO(pedidoRepository.save(pedido));
    }

    private PedidoDTO toPedidoDTO(Pedido pedido) {
        return modelMapper.map(pedido, PedidoDTO.class);
    }
}
