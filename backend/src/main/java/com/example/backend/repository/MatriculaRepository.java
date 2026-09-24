package com.example.backend.repository;

import com.example.backend.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    Optional<Matricula> findByCodigoMatricula(String codigoMatricula);
    Optional<Matricula> findByEstudianteIdAndPeriodoId(Long estudianteId, Long periodoId);
    List<Matricula> findByEstudianteId(Long estudianteId);
    List<Matricula> findByPeriodoId(Long periodoId);
    List<Matricula> findByEstado(String estado);

    @Query("SELECT COUNT(m) FROM Matricula m")
    Long countTotalMatriculas();
}
