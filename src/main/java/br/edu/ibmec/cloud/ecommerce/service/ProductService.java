package br.edu.ibmec.cloud.ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.cosmos.models.PartitionKey;

import br.edu.ibmec.cloud.ecommerce.entity.Product;
import br.edu.ibmec.cloud.ecommerce.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findProductByName(String productName) {
        return this.productRepository.findByProductName(productName);
    }

    public Optional<Product> findById(String productId, String category) {
        return this.productRepository.findById(productId, new PartitionKey(category));
    }

    public void save(Product product) {
        product.setProductId(UUID.randomUUID().toString());
        this.productRepository.save(product);
    }

    public void delete(String productId) {
        this.productRepository.deleteById(productId);
    }

}
