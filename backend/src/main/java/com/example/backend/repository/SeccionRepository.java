package com.example.backend.repository;

import com.example.backend.entity.Seccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeccionRepository extends JpaRepository<Seccion, Long> {
    List<Seccion> findByPeriodoId(Long periodoId);
    List<Seccion> findByCursoIdAndPeriodoId(Long cursoId, Long periodoId);
    List<Seccion> findByDocenteId(Long docenteId);
    Optional<Seccion> findByCursoIdAndPeriodoIdAndCodigoSeccion(Long cursoId, Long periodoId, String codigoSeccion);

    @Query("SELECT s FROM Seccion s WHERE s.periodo.id = :periodoId AND (s.vacantes - s.matriculados) > 0 AND s.estado = 'ACTIVO'")
    List<Seccion> findDisponiblesByPeriodo(@Param("periodoId") Long periodoId);
}
