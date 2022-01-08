package br.com.adotappet.pedidoapi.domain.service;

import br.com.adotappet.pedidoapi.api.dto.PedidoDTO;
import br.com.adotappet.pedidoapi.domain.entity.Pedido;
import br.com.adotappet.pedidoapi.domain.exception.BusinessException;
import br.com.adotappet.pedidoapi.domain.repository.PedidoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, ModelMapper modelMapper) {
        this.pedidoRepository = pedidoRepository;
        this.modelMapper = modelMapper;
    }

    public PedidoDTO iniciaPedido(Long usuarioId, Long petId) {
        Pedido pedido = Pedido.builder()
                .usuarioId(usuarioId)
                .petId(petId)
                .status(Pedido.Status.ABERTO)
                .build();

        return toPedidoDTO(pedidoRepository.save(pedido));
    }

    private PedidoDTO aprovaRejeitaPedido(Long id, boolean aceito) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        if (!pedido.getStatus().equals(Pedido.Status.ABERTO)) {
            throw new BusinessException("Pedido não está em aberto");
        }
        pedido.setStatus(aceito ? Pedido.Status.ACEITO : Pedido.Status.REJEITADO);
        //TODO postar na fila que a apicore vai ler e alterar a disponibilidade do pet caso aceito
        return toPedidoDTO(pedidoRepository.save(pedido));
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
