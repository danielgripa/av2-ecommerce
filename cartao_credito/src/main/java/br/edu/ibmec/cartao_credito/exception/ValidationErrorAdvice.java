package br.edu.ibmec.cartao_credito.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ValidationErrorAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationMessageErrors validationHandler(MethodArgumentNotValidException e) {
        ValidationMessageErrors response = new ValidationMessageErrors();
        for (FieldError item : e.getFieldErrors()) {
            ValidationError validation = new ValidationError();
            validation.setField(item.getField());
            validation.setMessage(item.getDefaultMessage());
            response.getErrors().add(validation);
        }
        return response;
    }

    @ExceptionHandler(UsuarioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationMessageErrors validationHandlerUsuario(UsuarioException e) {
        ValidationMessageErrors response = new ValidationMessageErrors();
        ValidationError error = new ValidationError();
        error.setField("exception");
        error.setMessage(e.getMessage());
        response.getErrors().add(error);
        return response;
    }



}
