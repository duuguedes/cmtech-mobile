package com.example.api.cmtech.controller;

import com.example.api.cmtech.dto.bateria.AtualizacaoBateriaDto;
import com.example.api.cmtech.dto.bateria.CadastroBateriaDto;
import com.example.api.cmtech.dto.bateria.DetalhesBateriaDto;
import com.example.api.cmtech.model.Bateria;
import com.example.api.cmtech.repository.BateriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("baterias")
public class BateriaController {
    @Autowired
    private BateriaRepository bateriaRepository;


    @GetMapping
    public ResponseEntity<List<DetalhesBateriaDto>> get(Pageable pageable){
        var bateria = bateriaRepository.findAll(pageable)
                .stream().map(DetalhesBateriaDto::new).toList();
        return ResponseEntity.ok(bateria);
    }

    @GetMapping("{id}")
    public ResponseEntity<DetalhesBateriaDto> get(@PathVariable("id")Long id){
        var bateria = bateriaRepository.getReferenceById(id);
        return ResponseEntity.ok(new DetalhesBateriaDto(bateria));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DetalhesBateriaDto> post(@RequestBody CadastroBateriaDto bateriaDto,
                                                   UriComponentsBuilder uriBuilder){
        var bateria = new Bateria(bateriaDto);
        bateriaRepository.save(bateria);
        var uri = uriBuilder.path("baterias/{id}").buildAndExpand(bateria.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalhesBateriaDto(bateria));
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable("id")Long id){
        bateriaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<DetalhesBateriaDto> put(@PathVariable("id")Long id,
                                                  @RequestBody AtualizacaoBateriaDto dto){
        var bateria = bateriaRepository.getReferenceById(id);
        bateria.atualizarInformacoesBateria(dto);
        return ResponseEntity.ok(new DetalhesBateriaDto(bateria));
    }


}