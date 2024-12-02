package com.example.api.cmtech.model;

import com.example.api.cmtech.dto.veiculo.AtualizacaoVeiculoDto;
import com.example.api.cmtech.dto.veiculo.CadastroVeiculoDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_cmt_veiculo")
@EntityListeners(AuditingEntityListener.class)
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "veiculo_seq")
    @SequenceGenerator(name = "veiculo_seq", sequenceName = "seq_cmt_veiculo", allocationSize = 1)
    @Column(name = "id_veiculo", length = 9, nullable = false, updatable = false)
    private Long id;

    @Column(name = "modelo_veiculo", length = 100, nullable = false)
    private String modelo;

    @Column(name = "ano_veiculo", nullable = false)
    private Integer ano;

    public Veiculo(CadastroVeiculoDto dto) {
        this.modelo = dto.modelo();
        this.ano = dto.ano();
    }

    public void atualizarInformacoesVeiculo(AtualizacaoVeiculoDto dto) {
        if (dto.modelo() != null)
            this.modelo = dto.modelo();
        if (dto.ano() != null)
            this.ano = dto.ano();
    }
}