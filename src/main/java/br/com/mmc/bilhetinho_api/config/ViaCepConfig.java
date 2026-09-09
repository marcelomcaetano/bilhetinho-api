package br.com.mmc.bilhetinho_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/**
 * Configuração do cliente HTTP RestClient dedicado para a integração com a API externa do ViaCEP.
 */
@Configuration
public class ViaCepConfig {

    @Value("${app.viacep.url}")
    private String viaCepUrl;

    @Value("${app.viacep.connect-timeout-ms:5000}")
    private int connectTimeoutMs;

    @Value("${app.viacep.read-timeout-ms:5000}")
    private int readTimeoutMs;

    @Bean
    public RestClient viaCepRestClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(connectTimeoutMs));
        factory.setReadTimeout(Duration.ofMillis(readTimeoutMs));

        return RestClient.builder()
                .baseUrl(viaCepUrl)
                .requestFactory(factory)
                .build();
    }
}
