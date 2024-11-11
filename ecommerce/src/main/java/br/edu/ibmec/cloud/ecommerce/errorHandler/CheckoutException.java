package br.edu.ibmec.cloud.ecommerce.errorHandler;

public class CheckoutException extends Exception {
    public CheckoutException(String message) {
        super(message);
    }
}
