package com.example.backend.repository;

import com.example.backend.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByDni(String dni);
    Optional<Estudiante> findByCodigoEstudiante(String codigoEstudiante);
    Optional<Estudiante> findByEmail(String email);
    List<Estudiante> findByCarreraId(Long carreraId);
    List<Estudiante> findByEstado(String estado);
}
