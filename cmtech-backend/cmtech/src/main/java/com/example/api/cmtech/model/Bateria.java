package com.example.api.cmtech.model;

import com.example.api.cmtech.dto.bateria.AtualizacaoBateriaDto;
import com.example.api.cmtech.dto.bateria.CadastroBateriaDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_cmt_bateria")
@EntityListeners(AuditingEntityListener.class)
public class Bateria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bateria_seq")
    @SequenceGenerator(name = "bateria_seq", sequenceName = "seq_cmt_bateria", allocationSize = 1)
    @Column(name = "id_bateria", length = 9, nullable = false, updatable = false)
    private Long id;

    @Column(name = "status_bateria", length = 50, nullable = false)
    private String status;

    @Column(name = "nivel_energia_bateria", nullable = false)
    private Double nivelEnergia;

    public Bateria(CadastroBateriaDto dto) {
        this.status = dto.status();
        this.nivelEnergia = dto.nivelEnergia();
    }

    public void atualizarInformacoesBateria(AtualizacaoBateriaDto dto) {
        if (dto.status() != null)
            this.status = dto.status();
        if (dto.nivelEnergia() != null)
            this.nivelEnergia = dto.nivelEnergia();
    }
}
