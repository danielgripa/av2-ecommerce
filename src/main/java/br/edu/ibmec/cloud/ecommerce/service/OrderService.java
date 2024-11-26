package br.edu.ibmec.cloud.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.cosmos.models.PartitionKey;

import br.edu.ibmec.cloud.ecommerce.entity.Order;
import br.edu.ibmec.cloud.ecommerce.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Consulta todas as ordens
    public List<Order> findAllOrders() {
        Iterable<Order> iterableOrders = this.orderRepository.findAll();
        List<Order> orders = new ArrayList<>();
        iterableOrders.forEach(orders::add); // Converte Iterable para List
        return orders;
    }

    // Consulta por ID
    public Optional<Order> findById(String orderId, String productId) {
        return this.orderRepository.findById(orderId, new PartitionKey(productId));
    }

    // Consulta por status
    public List<Order> findByStatus(String status) {
        return this.orderRepository.findByStatus(status);
    }

    // Consulta por número do cartão
    public List<Order> findByNumeroCartao(int numeroCartao) {
        return this.orderRepository.findByNumeroCartao(numeroCartao);
    }


    // Salva uma nova ordem
    public void save(Order order) {
        this.orderRepository.save(order);
    }

    // Deleta uma ordem
    public void delete(String orderId, String productId) throws Exception {
        Optional<Order> optOrder = this.orderRepository.findById(orderId, new PartitionKey(productId));
        if (!optOrder.isPresent()) {
            throw new Exception("Não foi encontrada a ordem a ser excluída.");
        }
        this.orderRepository.deleteById(orderId, new PartitionKey(productId));
    }
}
