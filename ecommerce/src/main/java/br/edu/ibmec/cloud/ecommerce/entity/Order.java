package br.edu.ibmec.cloud.ecommerce.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Container(containerName = "orders")
public class Order {
    @Id
    private String orderId;
    @PartitionKey
    private String productId;
    private int userId;
    private LocalDateTime dataOrder;
    private String status;
    
}
