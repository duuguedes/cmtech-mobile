package com.example.api.cmtech.repository;

import com.example.api.cmtech.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Long> {
}