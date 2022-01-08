package br.com.adotappet.pedidoapi.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PedidoDTO {

    private Long id;

    private Long petId;

    private Long usuarioId;

    private Status status;

    private String motivo;

    private LocalDateTime dataSolicitacao;

    private LocalDateTime dataAtualizacao;

    public enum Status {
        ABERTO,
        ACEITO,
        REJEITADO,
        FINALIZADO,
        CANCELADO
    }
}
