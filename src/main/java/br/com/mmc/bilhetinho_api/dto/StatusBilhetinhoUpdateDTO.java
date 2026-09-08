package br.com.mmc.bilhetinho_api.dto;

import br.com.mmc.bilhetinho_api.model.StatusBilhetinho;
import jakarta.validation.constraints.NotNull;

public record StatusBilhetinhoUpdateDTO(
    @NotNull(message = "O status é obrigatório")
    StatusBilhetinho status
) {}
