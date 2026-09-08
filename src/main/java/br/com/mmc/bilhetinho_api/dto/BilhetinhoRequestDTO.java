package br.com.mmc.bilhetinho_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record BilhetinhoRequestDTO(
    @NotNull(message = "O código do evento é obrigatório")
    UUID codEvento,

    @NotBlank(message = "O nome da música é obrigatório")
    @Size(max = 100, message = "O nome da música deve ter no máximo 100 caracteres")
    String musica,

    @NotBlank(message = "O nome do artista é obrigatório")
    @Size(max = 100, message = "O nome do artista deve ter no máximo 100 caracteres")
    String artista,

    @NotBlank(message = "O nome do solicitante é obrigatório")
    @Size(max = 100, message = "O nome do solicitante deve ter no máximo 100 caracteres")
    String nomeSolicitante,

    @Size(max = 255, message = "A mensagem deve ter no máximo 255 caracteres")
    String mensagem
) {}
