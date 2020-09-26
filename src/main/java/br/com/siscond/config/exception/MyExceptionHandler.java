package br.com.siscond.config.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class MyExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<MesagemErro> handle(MethodArgumentNotValidException exception) {
        List<MesagemErro> dto = new ArrayList<>();

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> {
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            MesagemErro erro = new MesagemErro(e.getField(), mensagem);
            dto.add(erro);
        });

        return dto;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public MesagemErro handleConstraintViolation(ConstraintViolationException e) {
        return new MesagemErro(e.getLocalizedMessage(), e.getCause().getLocalizedMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public MesagemErro handleIntegrityViolation(DataIntegrityViolationException e) {
        return new MesagemErro(e.getLocalizedMessage(), e.getRootCause().getLocalizedMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public MesagemErro handleNotAllowed(HttpRequestMethodNotSupportedException e) {

        return new MesagemErro("Algum parâmetro exigido não foi informado", "Método não permitido");
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public MesagemErro handleException(Exception e) {
        if(e != null)
            return new MesagemErro(e.getMessage(), e.getMessage());
        else
            return  new MesagemErro("Houve algum erro", "Não foi possível determinar a causa");
    }

}
