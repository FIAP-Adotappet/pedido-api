package br.com.adotappet.pedidoapi.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Problem {
    private Integer status;
    private OffsetDateTime dateTime;
    private String title;
    private List<Field> fields;

    @Data
    @Builder
    static class Field {
        private String name;
        private String message;
    }
}
