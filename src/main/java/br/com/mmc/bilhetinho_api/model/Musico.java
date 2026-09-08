package br.com.mmc.bilhetinho_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa os dados cadastrais do músico/artista.
 */
@Entity
@Table(name = "musico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Musico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "estilo_musical", nullable = false, length = 50)
    private String estiloMusical;
}
