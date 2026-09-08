package br.com.mmc.bilhetinho_api.model;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa um show ou apresentação ao vivo criada pelo músico.
 */
@Entity
@Table(name = "evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_musico", nullable = false)
    private Musico musico;

    @Column(name = "cod_evento", nullable = false, unique = true)
    private UUID codEvento;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "local", nullable = false, length = 100)
    private String local;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusEvento status;

    @OneToOne(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private EventoEndereco endereco;

    @PrePersist
    public void prePersist() {
        if (this.codEvento == null) {
            this.codEvento = UUID.randomUUID();
        }
        if (this.status == null) {
            this.status = StatusEvento.PENDENTE;
        }
    }
}
