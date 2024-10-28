package br.edu.ibmec.cloud.ecommerce.errorHandler;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ValidationErrorResponse {
    public String mensagem = "Existem erros na sua requisição";
    public List<Validation> validationErrors = new ArrayList<>();
}