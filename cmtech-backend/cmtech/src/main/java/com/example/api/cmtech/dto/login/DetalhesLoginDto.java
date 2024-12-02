package com.example.api.cmtech.dto.login;

import com.example.api.cmtech.model.Login;

public record DetalhesLoginDto(Long id, String email, String senha) {

    public DetalhesLoginDto(Login login) {
        this(login.getId(), login.getEmail(), login.getSenha());
    }
}