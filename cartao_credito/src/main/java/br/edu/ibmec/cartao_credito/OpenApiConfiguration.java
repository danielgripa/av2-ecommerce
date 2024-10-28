package br.edu.ibmec.cartao_credito;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("IBMEC - CLOUD")
                        .description("API de Autorização do Cartão de Credito - Projeto Cloud & Big Data 2024.2")
                        .version("1.0")
                        .contact(new Contact()
                                .email("rafael.cruz@professores.ibmec.edu.br")
                                .name("Rafael Cruz")
                        )
                );
    }
}
