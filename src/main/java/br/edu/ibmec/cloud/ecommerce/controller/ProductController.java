package br.edu.ibmec.cloud.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azure.core.annotation.Delete;
import com.azure.core.annotation.Get;

import br.edu.ibmec.cloud.ecommerce.entity.Product;
import br.edu.ibmec.cloud.ecommerce.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        this.service.save(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @GetMapping(value = "/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable("category") String category) {
        List<Product> products = this.service.findByCategory(category);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getByProductName(@RequestParam String productName) {
        List<Product> products = this.service.findProductByName(productName);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") String id) throws Exception{
        this.service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


     
}
