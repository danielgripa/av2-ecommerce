package br.edu.ibmec.cloud.ecommerce.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
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

    public Order checkout(Product product, int idUsuario, String numeroCartao) throws CheckoutException {
        try {
            // Chama o método para autorizar a transação
            TransacaoResponse response = this.autorizar(product, idUsuario, numeroCartao);

            // Verifica se a transação foi aprovada
            if (!"APROVADO".equalsIgnoreCase(response.getStatus())) {
                throw new CheckoutException("Transação não aprovada: " + response.getStatus());
            }

            // Cria e salva o pedido no banco de dados
            Order order = new Order();
            order.setOrderId(String.valueOf(response.getIdTransacao())); // Usa o ID da transação
            order.setDataOrder(LocalDateTime.now());
            order.setProductId(product.getProductId());
            order.setUserId(idUsuario);
            order.setStatus("Produto Comprado");
            this.orderRepository.save(order);

            return order;
        } catch (CheckoutException e) {
            // Relança exceções específicas de checkout
            throw e;
        } catch (Exception e) {
            // Captura e encapsula outros erros
            throw new CheckoutException("Erro inesperado ao realizar a compra", e);
        }
    }

private TransacaoResponse autorizar(Product product, int idUsuario, String numeroCartao) {
    try {
        // Substitui {cartaoId} na URL
        String url = transactionProperties.getTransactionUrl().replace("{cartaoId}", numeroCartao);

        // Preenche os dados da transação
        TransacaoRequest request = new TransacaoRequest();
        request.setComerciante(transactionProperties.getMerchant());
        request.setDataTransacao(LocalDateTime.now().toString()); // Inclui a data e hora da transação
        request.setIdUsuario(idUsuario); // Adiciona o ID do usuário
        request.setValor(product.getPrice());

        // Faz a requisição ao serviço de transações
        ResponseEntity<TransacaoResponse> response = this.restTemplate.postForEntity(url, request, TransacaoResponse.class);

        // Verifica o status HTTP da resposta
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new CheckoutException("Erro na autorização: HTTP " + response.getStatusCode());
        }

        // Retorna o corpo da resposta
        return response.getBody();

    } catch (HttpClientErrorException | HttpServerErrorException e) {

        throw new CheckoutException("Erro HTTP ao autorizar a transação: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
    } catch (RestClientException e) {

        throw new CheckoutException("Erro de comunicação com o serviço de transações.", e);
    } catch (Exception e) {

        throw new CheckoutException("Erro inesperado ao autorizar a transação.", e);
    }
}

}
