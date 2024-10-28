package br.edu.ibmec.cloud.ecommerce.errorHandler;

import lombok.Data;

@Data
public class Validation {
    private String field;
    private String message;
}
