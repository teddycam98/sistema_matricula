package com.example.backend.repository;

import com.example.backend.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    Optional<Curso> findByCodigo(String codigo);
    List<Curso> findByPlanEstudioId(Long planEstudioId);
    List<Curso> findByCiclo(Integer ciclo);
    List<Curso> findByEstado(String estado);
}
