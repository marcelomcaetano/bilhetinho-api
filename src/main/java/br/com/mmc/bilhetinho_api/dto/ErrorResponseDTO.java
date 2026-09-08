package br.com.mmc.bilhetinho_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDTO(
    LocalDateTime timestamp,
    Integer status,
    String error,
    String message,
    String path,
    String trace
) {}
