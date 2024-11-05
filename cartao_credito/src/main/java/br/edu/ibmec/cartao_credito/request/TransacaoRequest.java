package br.edu.ibmec.cartao_credito.request;

import lombok.Data;

@Data
public class TransacaoRequest {
    private int idUsuario;
    private String numeroCartao;
    private String comerciante;
    private Double valor;
}
