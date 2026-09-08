package br.com.mmc.bilhetinho_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EventoRequestDTO(
    @NotNull(message = "O ID do músico é obrigatório")
    Long idMusico,

    @NotBlank(message = "O nome do evento é obrigatório")
    @Size(max = 100, message = "O nome do evento deve ter no máximo 100 caracteres")
    String nome,

    @NotNull(message = "A data e hora do evento são obrigatórias")
    LocalDateTime dataHora,

    @NotBlank(message = "O local do evento é obrigatório")
    @Size(max = 100, message = "O local deve ter no máximo 100 caracteres")
    String local,

    @NotNull(message = "O endereço do evento é obrigatório")
    @Valid
    EventoEnderecoDTO endereco
) {}
