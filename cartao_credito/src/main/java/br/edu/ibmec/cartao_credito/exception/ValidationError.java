package br.edu.ibmec.cartao_credito.exception;

import lombok.Data;

@Data
public class ValidationError {
    private String field;
    private String message;
}
