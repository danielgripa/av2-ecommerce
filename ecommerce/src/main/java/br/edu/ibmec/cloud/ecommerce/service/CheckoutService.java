package br.edu.ibmec.cloud.ecommerce.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.edu.ibmec.cloud.ecommerce.config.CosmosProperties;
import br.edu.ibmec.cloud.ecommerce.config.TransactionProperties;
import br.edu.ibmec.cloud.ecommerce.entity.Order;
import br.edu.ibmec.cloud.ecommerce.entity.Product;
import br.edu.ibmec.cloud.ecommerce.errorHandler.CheckoutException;
import br.edu.ibmec.cloud.ecommerce.repository.OrderRepository;

@Service
@EnableConfigurationProperties(TransactionProperties.class)
public class CheckoutService {
    
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionProperties transactionProperties;

    @Autowired
    private OrderRepository orderRepository;

    public Order checkout(Product product, int idUsuario, String numeroCartao) throws Exception {
        try {
            TransacaoResponse response = this.autorizar(product, idUsuario, numeroCartao);

            if (response.getStatus().equals("APROVADO") == false) {
                throw new CheckoutException("Não consegui realizar a compra");
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
            throw new CheckoutException("Não consegui realizar a compra");
        }
    }

    private TransacaoResponse autorizar(Product product, int idUsuario, String numeroCartao) {
        String url = transactionProperties.getTransactionUrl();
        TransacaoRequest request = new TransacaoRequest();

        request.setComerciante(transactionProperties.getMerchant());
        request.setIdUsuario(idUsuario);
        request.setNumeroCartao(numeroCartao);
        request.setValor(product.getPrice());
        ResponseEntity<TransacaoResponse> response = this.restTemplate.postForEntity(url, request, TransacaoResponse.class);
        return response.getBody();
    }
}
