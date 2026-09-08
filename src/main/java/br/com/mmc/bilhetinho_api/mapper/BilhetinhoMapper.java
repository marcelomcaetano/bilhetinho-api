package br.com.mmc.bilhetinho_api.mapper;

import br.com.mmc.bilhetinho_api.dto.BilhetinhoRequestDTO;
import br.com.mmc.bilhetinho_api.dto.BilhetinhoResponseDTO;
import br.com.mmc.bilhetinho_api.model.Bilhetinho;
import br.com.mmc.bilhetinho_api.model.Evento;
import br.com.mmc.bilhetinho_api.model.StatusBilhetinho;

import java.time.LocalDateTime;

public final class BilhetinhoMapper {

    private BilhetinhoMapper() {
        throw new UnsupportedOperationException("Classe utilitária não deve ser instanciada");
    }

    public static Bilhetinho toEntity(BilhetinhoRequestDTO dto, Evento evento) {
        if (dto == null) {
            return null;
        }

        return Bilhetinho.builder()
                .evento(evento)
                .musico(evento != null ? evento.getMusico() : null)
                .musica(dto.musica())
                .artista(dto.artista())
                .nomeSolicitante(dto.nomeSolicitante())
                .mensagem(dto.mensagem())
                .dataHora(LocalDateTime.now())
                .status(StatusBilhetinho.PENDENTE)
                .build();
    }

    public static BilhetinhoResponseDTO toDTO(Bilhetinho entity) {
        if (entity == null) {
            return null;
        }

        Long idEvento = entity.getEvento() != null ? entity.getEvento().getId() : null;
        String nomeEvento = entity.getEvento() != null ? entity.getEvento().getNome() : null;
        Long idMusico = entity.getMusico() != null ? entity.getMusico().getId() : null;

        return new BilhetinhoResponseDTO(
                entity.getId(),
                idEvento,
                nomeEvento,
                idMusico,
                entity.getMusica(),
                entity.getArtista(),
                entity.getNomeSolicitante(),
                entity.getMensagem(),
                entity.getDataHora(),
                entity.getStatus()
        );
    }
}
