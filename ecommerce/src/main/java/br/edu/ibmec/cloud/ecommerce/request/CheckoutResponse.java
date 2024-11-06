package br.edu.ibmec.cloud.ecommerce.request;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CheckoutResponse {
    private String productId;
    private String status;
    private LocalDateTime dataCompra;
    private String orderId;

}
