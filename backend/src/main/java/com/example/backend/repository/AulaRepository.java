package com.example.backend.repository;

import com.example.backend.entity.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {
    Optional<Aula> findByCodigo(String codigo);
    List<Aula> findByTipo(String tipo);
    List<Aula> findByEstado(String estado);
}
