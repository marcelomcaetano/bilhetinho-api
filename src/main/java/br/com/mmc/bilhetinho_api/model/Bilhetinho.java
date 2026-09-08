package br.com.mmc.bilhetinho_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidade que representa o pedido de música (bilhetinho digital) enviado pelo público.
 */
@Entity
@Table(name = "bilhetinho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bilhetinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_musico", nullable = false)
    private Musico musico;

    @Column(nullable = false, length = 100)
    private String musica;

    @Column(nullable = false, length = 100)
    private String artista;

    @Column(name = "nome_solicitante", nullable = false, length = 100)
    private String nomeSolicitante;

    @Column(columnDefinition = "TEXT")
    private String mensagem;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusBilhetinho status;

    @PrePersist
    public void prePersist() {
        if (this.dataHora == null) {
            this.dataHora = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = StatusBilhetinho.PENDENTE;
        }
    }
}
