package br.edu.ibmec.cloud.ecommerce.service;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class TransacaoResponse {
    private LocalDateTime dataTransacao;
    private double valor;
    private String status;
    private UUID codigoAutorizacao;
}
