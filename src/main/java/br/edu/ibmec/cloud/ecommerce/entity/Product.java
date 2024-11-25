package br.edu.ibmec.cloud.ecommerce.entity;

import org.springframework.data.annotation.Id;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Container(containerName = "products")
public class Product {

    @Id
    private String productId;

    @PartitionKey
    @NotBlank(message = "Categoria do produto não pode estar vazia")
    private String productCategory;
    
    @NotBlank(message = "Nome do produto não pode estar vazia")
    private String productName;

    private double price;

    private String urlFoto;

    private String productDescription;
}
