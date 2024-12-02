package com.example.api.cmtech.controller;

import com.example.api.cmtech.dto.veiculo.AtualizacaoVeiculoDto;
import com.example.api.cmtech.dto.veiculo.CadastroVeiculoDto;
import com.example.api.cmtech.dto.veiculo.DetalhesVeiculoDto;
import com.example.api.cmtech.model.Veiculo;
import com.example.api.cmtech.repository.VeiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("veiculos")
public class VeiculoController {
    @Autowired
    private VeiculoRepository veiculoRepository;


    @GetMapping
    public ResponseEntity<List<DetalhesVeiculoDto>> get(Pageable pageable){
        var veiculo = veiculoRepository.findAll(pageable)
                .stream().map(DetalhesVeiculoDto::new).toList();
        return ResponseEntity.ok(veiculo);
    }

    @GetMapping("{id}")
    public ResponseEntity<DetalhesVeiculoDto> get(@PathVariable("id")Long id){
        var veiculo = veiculoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DetalhesVeiculoDto(veiculo));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DetalhesVeiculoDto> post(@RequestBody CadastroVeiculoDto veiculoDto,
                                                   UriComponentsBuilder uriBuilder){
        var veiculo = new Veiculo(veiculoDto);
        veiculoRepository.save(veiculo);
        var uri = uriBuilder.path("veiculos/{id}").buildAndExpand(veiculo.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalhesVeiculoDto(veiculo));
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable("id")Long id){
        veiculoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<DetalhesVeiculoDto> put(@PathVariable("id")Long id,
                                                  @RequestBody AtualizacaoVeiculoDto dto){
        var veiculo = veiculoRepository.getReferenceById(id);
        veiculo.atualizarInformacoesVeiculo(dto);
        return ResponseEntity.ok(new DetalhesVeiculoDto(veiculo));
    }


}