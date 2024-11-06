package br.edu.ibmec.cloud.ecommerce.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ibmec.cloud.ecommerce.entity.Order;
import br.edu.ibmec.cloud.ecommerce.entity.Product;
import br.edu.ibmec.cloud.ecommerce.request.CheckoutRequest;
import br.edu.ibmec.cloud.ecommerce.request.CheckoutResponse;
import br.edu.ibmec.cloud.ecommerce.service.CheckoutService;
import br.edu.ibmec.cloud.ecommerce.service.ProductService;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService service;

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(@RequestBody CheckoutRequest request) throws Exception{
        
        Optional<Product> optProduct = this.productService.findById(request.getProductId());

        if (optProduct.isPresent() == false)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Product product = optProduct.get();
        Order order = this.service.checkout(product, request.getIdUsuario(), request.getNumeroCartao());

        CheckoutResponse response = new CheckoutResponse();
        response.setDataCompra(order.getDataOrder()); 
        response.setProductId(order.getProductId());
        response.setStatus(order.getStatus());
        response.setOrderId(order.getOrderId());

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
    
}
