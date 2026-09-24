package com.example.backend.repository;

import com.example.backend.entity.PlanEstudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanEstudioRepository extends JpaRepository<PlanEstudio, Long> {
    Optional<PlanEstudio> findByCodigoPlan(String codigoPlan);
    List<PlanEstudio> findByCarreraId(Long carreraId);
    List<PlanEstudio> findByEstado(String estado);
}
