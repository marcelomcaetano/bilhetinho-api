package br.com.mmc.bilhetinho_api.dto;

import br.com.mmc.bilhetinho_api.model.StatusEvento;
import jakarta.validation.constraints.NotNull;

public record StatusEventoUpdateDTO(
    @NotNull(message = "O status é obrigatório")
    StatusEvento status
) {}
