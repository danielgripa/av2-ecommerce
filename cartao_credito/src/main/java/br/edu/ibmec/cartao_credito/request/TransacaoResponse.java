package br.edu.ibmec.cartao_credito.request;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class TransacaoResponse {
    private LocalDateTime dataTransacao;
    private double valor;
    private String status;
    private UUID codigoAutorizacao;
}
