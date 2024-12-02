package com.example.api.cmtech.controller;

import com.example.api.cmtech.dto.estacaoCarregamento.AtualizacaoEstacaoCarregamentoDto;
import com.example.api.cmtech.dto.estacaoCarregamento.CadastroEstacaoCarregamentoDto;
import com.example.api.cmtech.dto.estacaoCarregamento.DetalhesEstacaoCarregamentoDto;
import com.example.api.cmtech.model.EstacaoCarregamento;
import com.example.api.cmtech.repository.EstacaoCarregamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("estacoes-carregamento")
public class EstacaoCarregamentoController {
    @Autowired
    private EstacaoCarregamentoRepository estacaoCarregamentoRepository;


    @GetMapping
    public ResponseEntity<List<DetalhesEstacaoCarregamentoDto>> get(Pageable pageable){
        var estacaoCarregamento = estacaoCarregamentoRepository.findAll(pageable)
                .stream().map(DetalhesEstacaoCarregamentoDto::new).toList();
        return ResponseEntity.ok(estacaoCarregamento);
    }

    @GetMapping("{id}")
    public ResponseEntity<DetalhesEstacaoCarregamentoDto> get(@PathVariable("id")Long id){
        var estacaoCarregamento = estacaoCarregamentoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DetalhesEstacaoCarregamentoDto(estacaoCarregamento));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DetalhesEstacaoCarregamentoDto> post(@RequestBody CadastroEstacaoCarregamentoDto estacaoCarregamentoDto,
                                                   UriComponentsBuilder uriBuilder){
        var estacaoCarregamento = new EstacaoCarregamento(estacaoCarregamentoDto);
        estacaoCarregamentoRepository.save(estacaoCarregamento);
        var uri = uriBuilder.path("estacoes-carregamento/{id}").buildAndExpand(estacaoCarregamento.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalhesEstacaoCarregamentoDto(estacaoCarregamento));
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable("id")Long id){
        estacaoCarregamentoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<DetalhesEstacaoCarregamentoDto> put(@PathVariable("id")Long id,
                                                  @RequestBody AtualizacaoEstacaoCarregamentoDto dto){
        var estacaoCarregamento = estacaoCarregamentoRepository.getReferenceById(id);
        estacaoCarregamento.atualizarInformacoesEstacaoCarregamento(dto);
        return ResponseEntity.ok(new DetalhesEstacaoCarregamentoDto(estacaoCarregamento));
    }


}