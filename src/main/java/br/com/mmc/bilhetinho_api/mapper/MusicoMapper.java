package br.com.mmc.bilhetinho_api.mapper;

import br.com.mmc.bilhetinho_api.dto.MusicoRequestDTO;
import br.com.mmc.bilhetinho_api.dto.MusicoResponseDTO;
import br.com.mmc.bilhetinho_api.model.Musico;

public final class MusicoMapper {

    private MusicoMapper() {
        throw new UnsupportedOperationException("Classe utilitária não deve ser instanciada");
    }

    public static Musico toEntity(MusicoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Musico.builder()
                .nome(dto.nome())
                .email(dto.email())
                .estiloMusical(dto.estiloMusical())
                .build();
    }

    public static MusicoResponseDTO toDTO(Musico entity) {
        if (entity == null) {
            return null;
        }
        return new MusicoResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getEstiloMusical()
        );
    }
}
