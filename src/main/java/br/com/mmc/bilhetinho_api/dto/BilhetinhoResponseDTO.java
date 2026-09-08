package br.com.mmc.bilhetinho_api.dto;

import br.com.mmc.bilhetinho_api.model.StatusBilhetinho;

import java.time.LocalDateTime;

public record BilhetinhoResponseDTO(
    Long id,
    Long idEvento,
    String nomeEvento,
    Long idMusico,
    String musica,
    String artista,
    String nomeSolicitante,
    String mensagem,
    LocalDateTime dataHora,
    StatusBilhetinho status
) {}
