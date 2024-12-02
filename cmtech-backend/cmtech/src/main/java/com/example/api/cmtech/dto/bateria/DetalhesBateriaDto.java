package com.example.api.cmtech.dto.bateria;

import com.example.api.cmtech.model.Bateria;

public record DetalhesBateriaDto(
        Long id,
        String status,
        Double nivelEnergia) {

    public DetalhesBateriaDto(Bateria bateria) {
        this(
                bateria.getId(),
                bateria.getStatus(),
                bateria.getNivelEnergia()
        );
    }
}