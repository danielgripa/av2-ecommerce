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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import br.edu.ibmec.cloud.ecommerce.config.TransactionProperties;
import br.edu.ibmec.cloud.ecommerce.entity.Order;
import br.edu.ibmec.cloud.ecommerce.entity.Product;
import br.edu.ibmec.cloud.ecommerce.errorHandler.CheckoutException;
import br.edu.ibmec.cloud.ecommerce.repository.OrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@EnableConfigurationProperties(TransactionProperties.class)
public class CheckoutService {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionProperties transactionProperties;

    @Autowired
    private OrderRepository orderRepository;

    public Order checkout(Product product, int idUsuario, int numeroCartao) throws CheckoutException {
        try {
            logger.info("[CHECKOUT] Iniciando processo de checkout...");
            
            // Autoriza a transação
            boolean transacaoAprovada = this.autorizar(product, numeroCartao);

            if (!transacaoAprovada) {
                logger.info("[CHECKOUT] Transação não aprovada.");
                throw new CheckoutException("Transação não aprovada.");
            }

            // Cria e salva o pedido
            Order order = new Order();
            order.setOrderId(UUID.randomUUID().toString()); // Gera um UUID para o pedido
            order.setDataOrder(LocalDateTime.now().toString());
            order.setProductId(product.getProductId());
            order.setUserId(idUsuario);
            order.setNumeroCartao(numeroCartao);
            order.setStatus("Produto Comprado");

            logger.info("[CHECKOUT] Ordem criada: " + order);
            
            // Tenta salvar a ordem no CosmosDB
            try {
                this.orderRepository.save(order);
                logger.info("[CHECKOUT] Ordem salva com sucesso no CosmosDB.");
            } catch (Exception e) {
                logger.error("[CHECKOUT] Erro ao salvar ordem no CosmosDB: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }

            return order;
        } catch (CheckoutException e) {
            logger.error("[CHECKOUT] Erro de checkout: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("[CHECKOUT] Erro inesperado ao realizar a compra: " + e.getMessage());
            e.printStackTrace();
            throw new CheckoutException("Erro inesperado ao realizar a compra", e);
        }
    }

    private boolean autorizar(Product product, int numeroCartao) throws CheckoutException {
        try {
            logger.info("[AUTORIZAÇÃO] Iniciando autorização...");
            
            // Substitui {cartaoId} na URL com o valor correto
            String url = transactionProperties.getTransactionUrl().replace("{cartaoId}", String.valueOf(numeroCartao));
    
            // Define o formato da data aceito pela API
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    
            // Preenche os dados da transação
            TransacaoRequest request = new TransacaoRequest();
            request.setComerciante(transactionProperties.getMerchant());
            request.setDataTransacao(LocalDateTime.now().format(formatter)); // Formata a data
            request.setValor(product.getPrice());
    
            // Log do JSON
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(request);
            logger.info("[AUTORIZAÇÃO] JSON enviado para a API de transações: " + requestBody);
    
            // Faz a requisição ao serviço de transações
            ResponseEntity<Map> response = this.restTemplate.postForEntity(url, request, Map.class);
    
            // Verifica o status da resposta
            Map<String, String> responseBody = response.getBody();
            logger.info("[AUTORIZAÇÃO] Resposta recebida da API de transações: " + responseBody);

            return responseBody != null && "Sucesso".equalsIgnoreCase(responseBody.get("status"));
    
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("[AUTORIZAÇÃO] Erro HTTP ao autorizar a transação: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new CheckoutException("Erro HTTP ao autorizar a transação: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            logger.error("[AUTORIZAÇÃO] Erro de comunicação com o serviço de transações.");
            throw new CheckoutException("Erro de comunicação com o serviço de transações.", e);
        } catch (Exception e) {
            logger.error("[AUTORIZAÇÃO] Erro inesperado ao autorizar a transação.");
            throw new CheckoutException("Erro inesperado ao autorizar a transação.", e);
        }
    }
}
