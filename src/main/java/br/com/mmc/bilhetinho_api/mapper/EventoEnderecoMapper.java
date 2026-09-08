package br.com.mmc.bilhetinho_api.mapper;

import br.com.mmc.bilhetinho_api.dto.EventoEnderecoDTO;
import br.com.mmc.bilhetinho_api.model.EventoEndereco;

public final class EventoEnderecoMapper {

    private EventoEnderecoMapper() {
        throw new UnsupportedOperationException("Classe utilitária não deve ser instanciada");
    }

    public static EventoEndereco toEntity(EventoEnderecoDTO dto) {
        if (dto == null) {
            return null;
        }
        return EventoEndereco.builder()
                .cep(dto.cep())
                .logradouro(dto.logradouro())
                .numero(dto.numero())
                .complemento(dto.complemento())
                .bairro(dto.bairro())
                .cidade(dto.cidade())
                .uf(dto.uf() != null ? dto.uf().toUpperCase() : null)
                .build();
    }

    public static EventoEnderecoDTO toDTO(EventoEndereco entity) {
        if (entity == null) {
            return null;
        }
        return new EventoEnderecoDTO(
                entity.getCep(),
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getCidade(),
                entity.getUf()
        );
    }
}
