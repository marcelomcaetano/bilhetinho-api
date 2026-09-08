package br.com.mmc.bilhetinho_api.dto;

import br.com.mmc.bilhetinho_api.model.StatusEvento;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventoResponseDTO(
    Long id,
    Long idMusico,
    String nomeMusico,
    UUID codEvento,
    String nome,
    LocalDateTime dataHora,
    String local,
    StatusEvento status,
    EventoEnderecoDTO endereco
) {}
