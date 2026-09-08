package br.com.mmc.bilhetinho_api.repository;

import br.com.mmc.bilhetinho_api.model.Evento;
import br.com.mmc.bilhetinho_api.model.StatusEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    Optional<Evento> findByCodEvento(UUID codEvento);

    List<Evento> findByMusicoIdOrderByDataHoraDesc(Long musicoId);

    List<Evento> findByStatusOrderByDataHoraAsc(StatusEvento status);

    @Query("SELECT e FROM Evento e JOIN e.endereco end " +
           "WHERE LOWER(end.cidade) LIKE LOWER(CONCAT('%', :cidade, '%')) " +
           "AND e.status = :status ORDER BY e.dataHora ASC")
    List<Evento> findByCidadeAndStatus(@Param("cidade") String cidade, @Param("status") StatusEvento status);

    @Query("SELECT e FROM Evento e JOIN e.endereco end " +
           "WHERE LOWER(end.cidade) LIKE LOWER(CONCAT('%', :cidade, '%')) " +
           "AND e.dataHora BETWEEN :inicio AND :fim " +
           "AND e.status = :status ORDER BY e.dataHora ASC")
    List<Evento> findByCidadeAndDataAndStatus(@Param("cidade") String cidade,
                                              @Param("inicio") LocalDateTime inicio,
                                              @Param("fim") LocalDateTime fim,
                                              @Param("status") StatusEvento status);
}
