package br.com.mmc.bilhetinho_api.mapper;

import br.com.mmc.bilhetinho_api.dto.EventoRequestDTO;
import br.com.mmc.bilhetinho_api.dto.EventoResponseDTO;
import br.com.mmc.bilhetinho_api.model.Evento;
import br.com.mmc.bilhetinho_api.model.EventoEndereco;
import br.com.mmc.bilhetinho_api.model.Musico;
import br.com.mmc.bilhetinho_api.model.StatusEvento;

import java.util.UUID;

public final class EventoMapper {

    private EventoMapper() {
        throw new UnsupportedOperationException("Classe utilitária não deve ser instanciada");
    }

    public static Evento toEntity(EventoRequestDTO dto, Musico musico) {
        if (dto == null) {
            return null;
        }

        Evento evento = Evento.builder()
                .musico(musico)
                .codEvento(UUID.randomUUID())
                .nome(dto.nome())
                .dataHora(dto.dataHora())
                .local(dto.local())
                .status(StatusEvento.ATIVO)
                .build();

        if (dto.endereco() != null) {
            EventoEndereco endereco = EventoEnderecoMapper.toEntity(dto.endereco());
            evento.setEndereco(endereco);
        }

        return evento;
    }

    public static EventoResponseDTO toDTO(Evento entity) {
        if (entity == null) {
            return null;
        }

        Long idMusico = entity.getMusico() != null ? entity.getMusico().getId() : null;
        String nomeMusico = entity.getMusico() != null ? entity.getMusico().getNome() : null;

        return new EventoResponseDTO(
                entity.getId(),
                idMusico,
                nomeMusico,
                entity.getCodEvento(),
                entity.getNome(),
                entity.getDataHora(),
                entity.getLocal(),
                entity.getStatus(),
                EventoEnderecoMapper.toDTO(entity.getEndereco())
        );
    }
}
