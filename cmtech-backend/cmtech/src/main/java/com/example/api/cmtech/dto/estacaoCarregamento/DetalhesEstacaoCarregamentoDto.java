package com.example.api.cmtech.dto.estacaoCarregamento;

import com.example.api.cmtech.model.EstacaoCarregamento;

public record DetalhesEstacaoCarregamentoDto(
        Long id,
        String localizacao,
        Double capacidadeMaxima) {

    public DetalhesEstacaoCarregamentoDto(EstacaoCarregamento estacaoCarregamento) {
        this(
                estacaoCarregamento.getId(),
                estacaoCarregamento.getLocalizacao(),
                estacaoCarregamento.getCapacidadeMaxima()
        );
    }
}
