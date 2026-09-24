package com.example.backend.repository;

import com.example.backend.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
    List<Horario> findBySeccionId(Long seccionId);
    List<Horario> findByAulaId(Long aulaId);
}
