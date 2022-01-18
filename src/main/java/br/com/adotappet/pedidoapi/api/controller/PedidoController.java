package br.com.adotappet.pedidoapi.api.controller;

import br.com.adotappet.pedidoapi.api.dto.PedidoDTO;

import br.com.adotappet.pedidoapi.domain.service.PedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/aprova/{pedidoId}")
    public PedidoDTO aprova(@PathVariable Long pedidoId) {
        return pedidoService.aprovaPedido(pedidoId);
    }

    @GetMapping("/rejeita/{pedidoId}")
    public PedidoDTO rejeita(@PathVariable Long pedidoId) {
        return pedidoService.rejeitaPedido(pedidoId);
    }

    @GetMapping("/finaliza/{pedidoId}")
    public PedidoDTO finaliza(@PathVariable Long pedidoId) {
        return pedidoService.finalizaPedido(pedidoId);
    }

    @GetMapping("/cancela/{pedidoId}")
    public PedidoDTO cancela(@PathVariable Long pedidoId) {
        return pedidoService.cancelaPedido(pedidoId);
    }
}
