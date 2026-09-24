package com.example.backend.service;

import com.example.backend.entity.PlanEstudio;
import java.util.List;

public interface IPlanEstudioService {
    List<PlanEstudio> findAll();
    PlanEstudio findById(Long id);
    List<PlanEstudio> findByCarreraId(Long carreraId);
    PlanEstudio save(PlanEstudio plan);
    PlanEstudio update(Long id, PlanEstudio plan);
    void delete(Long id);
}
