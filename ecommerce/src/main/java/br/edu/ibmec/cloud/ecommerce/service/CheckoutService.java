package br.edu.ibmec.cloud.ecommerce.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.edu.ibmec.cloud.ecommerce.entity.Order;
import br.edu.ibmec.cloud.ecommerce.entity.Product;
import br.edu.ibmec.cloud.ecommerce.repository.OrderRepository;

@Service
public class CheckoutService {
    
    @Autowired
    private RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8090";
    private final String merchant = "BOT-COMMERCE";

    @Autowired
    private OrderRepository orderRepository;

    public Order checkout(Product product, int idUsuario, String numeroCartao) throws Exception {
        try {
            TransacaoResponse response = this.autorizar(product, idUsuario, numeroCartao);

            if (response.getStatus().equals("APROVADO") == false) {
                throw new Exception("Não consegui realizar a compra");
            }

            Order order = new Order();
            order.setOrderId(UUID.randomUUID().toString());
            order.setDataOrder(LocalDateTime.now());
            order.setProductId(product.getProductId());
            order.setUserId(idUsuario);
            order.setStatus("Produto Comprado");
            this.orderRepository.save(order);
            return order;
        }
        catch (Exception e) {
            //Gera um erro
            throw new Exception("Não consegui realizar a compra");
        }
    }

    private TransacaoResponse autorizar(Product product, int idUsuario, String numeroCartao) {
        String url = baseUrl + "/autorizar";
        TransacaoRequest request = new TransacaoRequest();

        request.setComerciante(merchant);
        request.setIdUsuario(idUsuario);
        request.setNumeroCartao(numeroCartao);
        request.setValor(product.getPrice());
        ResponseEntity<TransacaoResponse> response = this.restTemplate.postForEntity(url, request, TransacaoResponse.class);
        return response.getBody();
    }
}
