package br.edu.ibmec.cloud.ecommerce.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;

import br.edu.ibmec.cloud.ecommerce.entity.Order;
import br.edu.ibmec.cloud.ecommerce.entity.Product;

@Repository
public interface OrderRepository extends CosmosRepository<Order, String> {

    List<Order> findByStatus(String status);

    List<Order> findByNumeroCartao(int numeroCartao);

}
    