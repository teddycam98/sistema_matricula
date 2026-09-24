package com.example.backend.service.impl;

import com.example.backend.entity.PlanEstudio;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.PlanEstudioRepository;
import com.example.backend.service.IPlanEstudioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlanEstudioServiceImpl implements IPlanEstudioService {

    private final PlanEstudioRepository planRepository;

    public PlanEstudioServiceImpl(PlanEstudioRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanEstudio> findAll() {
        return planRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PlanEstudio findById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan de estudio no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanEstudio> findByCarreraId(Long carreraId) {
        return planRepository.findByCarreraId(carreraId);
    }

    @Override
    public PlanEstudio save(PlanEstudio plan) {
        return planRepository.save(plan);
    }

    @Override
    public PlanEstudio update(Long id, PlanEstudio plan) {
        PlanEstudio existing = findById(id);
        existing.setCarrera(plan.getCarrera());
        existing.setCodigoPlan(plan.getCodigoPlan());
        existing.setAnioVigencia(plan.getAnioVigencia());
        existing.setTotalCreditos(plan.getTotalCreditos());
        existing.setEstado(plan.getEstado());
        return planRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        PlanEstudio existing = findById(id);
        planRepository.delete(existing);
    }
}
