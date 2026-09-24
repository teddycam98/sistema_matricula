package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.entity.PlanEstudio;
import com.example.backend.service.IPlanEstudioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planes-estudio")
public class PlanEstudioController {

    private final IPlanEstudioService planEstudioService;

    public PlanEstudioController(IPlanEstudioService planEstudioService) {
        this.planEstudioService = planEstudioService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PlanEstudio>>> findAll(@RequestParam(required = false) Long carreraId) {
        List<PlanEstudio> list;
        if (carreraId != null) {
            list = planEstudioService.findByCarreraId(carreraId);
        } else {
            list = planEstudioService.findAll();
        }
        return ResponseEntity.ok(ApiResponse.ok("Planes de estudio listados con éxito", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlanEstudio>> findById(@PathVariable Long id) {
        PlanEstudio plan = planEstudioService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok("Plan de estudio encontrado", plan));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PlanEstudio>> save(@RequestBody PlanEstudio plan) {
        PlanEstudio creado = planEstudioService.save(plan);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Plan de estudio registrado exitosamente", creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PlanEstudio>> update(@PathVariable Long id, @RequestBody PlanEstudio plan) {
        PlanEstudio actualizado = planEstudioService.update(id, plan);
        return ResponseEntity.ok(ApiResponse.ok("Plan de estudio actualizado exitosamente", actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        planEstudioService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Plan de estudio eliminado exitosamente", null));
    }
}
