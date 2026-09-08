package br.com.mmc.bilhetinho_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bilhetinhoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bilhetinho API — MVP PUC-Rio")
                        .description("Gestao de pedido de musicas")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Marcelo M. Caetano")
                                .url("https://www.linkedin.com/in/marcelomcaetano/")));
    }
}
