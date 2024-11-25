package br.edu.ibmec.cloud.ecommerce.service;

import lombok.Data;

@Data
public class TransacaoRequest {
    private String dataTransacao;
    private String comerciante;
    private Double valor;

}
