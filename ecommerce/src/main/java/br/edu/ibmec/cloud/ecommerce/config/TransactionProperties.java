package br.edu.ibmec.cloud.ecommerce.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "ecommerce.api")
public class TransactionProperties {
    private String transactionUrl;
    private String merchant;
}
