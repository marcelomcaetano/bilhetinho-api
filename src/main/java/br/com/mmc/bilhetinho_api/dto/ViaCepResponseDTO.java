package br.com.mmc.bilhetinho_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear a resposta JSON retornada pela API externa do ViaCEP.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepResponseDTO(
    String cep,
    String logradouro,
    String complemento,
    String bairro,
    String localidade,
    String uf,
    String ibge,
    String ddd,
    @JsonProperty("erro")
    String erro
) {
    public boolean hasError() {
        return "true".equalsIgnoreCase(erro) || "1".equals(erro);
    }
}
