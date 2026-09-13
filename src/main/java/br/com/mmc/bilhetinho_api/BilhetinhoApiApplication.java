package br.com.mmc.bilhetinho_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class BilhetinhoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BilhetinhoApiApplication.class, args);
	}

	@Bean
	CommandLineRunner dropArtistaNotNull(JdbcTemplate jdbcTemplate) {
		return args -> {
			try {
				jdbcTemplate.execute("ALTER TABLE bilhetinho ALTER COLUMN artista DROP NOT NULL;");
			} catch (Exception ignored) {
				// Ignora se a tabela ainda não existir ou a coluna já aceitar nulo
			}
		};
	}

}
