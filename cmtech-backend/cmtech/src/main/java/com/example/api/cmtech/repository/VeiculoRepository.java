package com.example.api.cmtech.repository;

import com.example.api.cmtech.model.Login;
import com.example.api.cmtech.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
}