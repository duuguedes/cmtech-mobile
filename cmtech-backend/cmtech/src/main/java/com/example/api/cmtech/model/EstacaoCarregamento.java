package com.example.api.cmtech.model;

import com.example.api.cmtech.dto.estacaoCarregamento.AtualizacaoEstacaoCarregamentoDto;
import com.example.api.cmtech.dto.estacaoCarregamento.CadastroEstacaoCarregamentoDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_cmt_estacao_carregamento")
@EntityListeners(AuditingEntityListener.class)
public class EstacaoCarregamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estacao_carregamento_seq")
    @SequenceGenerator(name = "estacao_carregamento_seq", sequenceName = "seq_cmt_estacao_carregamento", allocationSize = 1)
    @Column(name = "id_estacao_carregamento", length = 9, nullable = false, updatable = false)
    private Long id;

    @Column(name = "localizacao_estacao_carregamento", length = 100, nullable = false)
    private String localizacao;

    @Column(name = "capacidade_maxima_estacao_carregamento", nullable = false)
    private Double capacidadeMaxima;

    public EstacaoCarregamento(CadastroEstacaoCarregamentoDto dto) {
        this.localizacao = dto.localizacao();
        this.capacidadeMaxima = dto.capacidadeMaxima();
    }

    public void atualizarInformacoesEstacaoCarregamento(AtualizacaoEstacaoCarregamentoDto dto) {
        if (dto.localizacao() != null)
            this.localizacao = dto.localizacao();
        if (dto.capacidadeMaxima() != null)
            this.capacidadeMaxima = dto.capacidadeMaxima();
    }
}