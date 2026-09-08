package br.com.mmc.bilhetinho_api.repository;

import br.com.mmc.bilhetinho_api.model.Bilhetinho;
import br.com.mmc.bilhetinho_api.model.StatusBilhetinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BilhetinhoRepository extends JpaRepository<Bilhetinho, Long> {

    List<Bilhetinho> findByEventoIdOrderByDataHoraDesc(Long eventoId);

    List<Bilhetinho> findByMusicoIdOrderByDataHoraDesc(Long musicoId);

    List<Bilhetinho> findByEventoIdAndStatusOrderByDataHoraAsc(Long eventoId, StatusBilhetinho status);

    long countByEventoIdAndStatus(Long eventoId, StatusBilhetinho status);
}
