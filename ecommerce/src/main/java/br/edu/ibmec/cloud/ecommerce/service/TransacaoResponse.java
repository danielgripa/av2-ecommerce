
package br.edu.ibmec.cloud.ecommerce.service;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TransacaoResponse {
    private LocalDateTime dataTransacao;
    private double valor;
    private String status;
    private String comerciante;
}
