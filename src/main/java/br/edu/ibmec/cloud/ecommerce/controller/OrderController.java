package br.edu.ibmec.cloud.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ibmec.cloud.ecommerce.entity.Order;
import br.edu.ibmec.cloud.ecommerce.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Endpoint para buscar todas as ordens
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.findAllOrders();
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/cartao/{numeroCartao}")
    public ResponseEntity<List<Order>> findByNumeroCartao(@PathVariable int numeroCartao) {
        List<Order> orders = this.orderService.findByNumeroCartao(numeroCartao);
        return ResponseEntity.ok(orders);
    }

    // Endpoint para buscar uma ordem específica pelo ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId, @RequestParam String productId) {
        Optional<Order> order = orderService.findById(orderId, productId);
        return order.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para buscar ordens por status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String status) {
        List<Order> orders = orderService.findByStatus(status);
        return ResponseEntity.ok(orders);
    }

    // Endpoint para criar uma nova ordem
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        orderService.save(order);
        return ResponseEntity.ok(order);
    }

    // Endpoint para deletar uma ordem
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderId, @RequestParam String productId) {
        try {
            orderService.delete(orderId, productId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
