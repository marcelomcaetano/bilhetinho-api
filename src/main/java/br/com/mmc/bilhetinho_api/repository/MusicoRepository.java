package br.com.mmc.bilhetinho_api.repository;

import br.com.mmc.bilhetinho_api.model.Musico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MusicoRepository extends JpaRepository<Musico, Long> {

    Optional<Musico> findByEmail(String email);

    boolean existsByEmail(String email);
}
