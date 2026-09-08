package br.com.mmc.bilhetinho_api.dto;

public record MusicoResponseDTO(
    Long id,
    String nome,
    String email,
    String estiloMusical
) {}
