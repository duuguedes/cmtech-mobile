package com.example.api.cmtech.controller;

import com.example.api.cmtech.dto.login.AtualizacaoLoginDto;
import com.example.api.cmtech.dto.login.CadastroLoginDto;
import com.example.api.cmtech.dto.login.DetalhesLoginDto;
import com.example.api.cmtech.model.Login;
import com.example.api.cmtech.repository.LoginRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("logins")
@CrossOrigin(origins = "http://localhost:8081")
public class LoginController {
    @Autowired
    private LoginRepository loginRepository;

    @GetMapping
    public ResponseEntity<List<DetalhesLoginDto>> get(Pageable pageable){
        var login = loginRepository.findAll(pageable)
                .stream().map(DetalhesLoginDto::new).toList();
        return ResponseEntity.ok(login);
    }

    @GetMapping("{id}")
    public ResponseEntity<DetalhesLoginDto> get(@PathVariable("id")Long id){
        var login = loginRepository.getReferenceById(id);
        return ResponseEntity.ok(new DetalhesLoginDto(login));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<DetalhesLoginDto> post(@ModelAttribute @Valid CadastroLoginDto loginDto,
                                                 UriComponentsBuilder uriBuilder){
        var login = new Login(loginDto);
        loginRepository.save(login);
        var uri = uriBuilder.path("logins/{id}").buildAndExpand(login.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalhesLoginDto(login));
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable("id")Long id){
        loginRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<DetalhesLoginDto> put(@PathVariable("id")Long id,
                                                @RequestBody @Valid AtualizacaoLoginDto dto){
        var login = loginRepository.getReferenceById(id);
        login.atualizarInformacoesLogin(dto);
        return ResponseEntity.ok(new DetalhesLoginDto(login));
    }
}