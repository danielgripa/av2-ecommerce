package br.edu.ibmec.cloud.ecommerce.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;

import br.edu.ibmec.cloud.ecommerce.entity.Product;

@Repository
public interface ProductRepository extends CosmosRepository<Product, String> {
    List<Product> findByProductName(String productName);
    List<Product> findByProductCategory(String productCategory);
}
