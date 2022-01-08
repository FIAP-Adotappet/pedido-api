package br.com.adotappet.pedidoapi.api.exceptionhandler;

import br.com.adotappet.pedidoapi.domain.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest web) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem problem = Problem.builder().status(status.value()).title(ex.getMessage()).dateTime(OffsetDateTime.now()).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, web);

    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusiness(BusinessException ex, WebRequest web) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Problem problem = Problem.builder().status(status.value()).title(ex.getMessage()).dateTime(OffsetDateTime.now()).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, web);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<Problem.Field> fields = new ArrayList<>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String name = ((FieldError) error).getField();
            String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            fields.add(Problem.Field.builder().message(message).name(name).build());
        }

        String title = "Um ou mais fields estão inválidos. Faça o preenchimento correto e tente novamente.";
        Problem problem = Problem.builder().status(status.value()).title(title).dateTime(OffsetDateTime.now()).fields(fields).build();

        return super.handleExceptionInternal(ex, problem, headers, status, request);
    }
}
