package com.example.api.cmtech.dto.veiculo;

import com.example.api.cmtech.model.Veiculo;

public record DetalhesVeiculoDto(
        Long id,
        String modelo,
        Integer ano) {

    public DetalhesVeiculoDto(Veiculo veiculo) {
        this(
                veiculo.getId(),
                veiculo.getModelo(),
                veiculo.getAno()
        );
    }
}