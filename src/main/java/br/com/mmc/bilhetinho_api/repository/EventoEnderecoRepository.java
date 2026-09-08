package br.com.mmc.bilhetinho_api.repository;

import br.com.mmc.bilhetinho_api.model.EventoEndereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventoEnderecoRepository extends JpaRepository<EventoEndereco, Long> {

    Optional<EventoEndereco> findByEventoId(Long eventoId);
}
