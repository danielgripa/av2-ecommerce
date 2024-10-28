package br.edu.ibmec.cloud.ecommerce.errorHandler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse validationHandler(MethodArgumentNotValidException e) {
        ValidationErrorResponse errors = new ValidationErrorResponse();

        for (FieldError item : e.getBindingResult().getFieldErrors()) {
            Validation validation = new Validation();
            validation.setField(item.getField());
            validation.setMessage(item.getDefaultMessage());

            errors.getValidationErrors().add(validation);
        }
        return errors;
    }

}