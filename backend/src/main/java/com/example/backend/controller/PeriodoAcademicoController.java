package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.entity.PeriodoAcademico;
import com.example.backend.service.IPeriodoAcademicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/periodos")
public class PeriodoAcademicoController {

    private final IPeriodoAcademicoService periodoService;

    public PeriodoAcademicoController(IPeriodoAcademicoService periodoService) {
        this.periodoService = periodoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PeriodoAcademico>>> findAll() {
        List<PeriodoAcademico> list = periodoService.findAll();
        return ResponseEntity.ok(ApiResponse.ok("Periodos académicos listados con éxito", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PeriodoAcademico>> findById(@PathVariable Long id) {
        PeriodoAcademico periodo = periodoService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok("Periodo académico encontrado", periodo));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PeriodoAcademico>> save(@RequestBody PeriodoAcademico periodo) {
        PeriodoAcademico creado = periodoService.save(periodo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Periodo académico registrado exitosamente", creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PeriodoAcademico>> update(@PathVariable Long id, @RequestBody PeriodoAcademico periodo) {
        PeriodoAcademico actualizado = periodoService.update(id, periodo);
        return ResponseEntity.ok(ApiResponse.ok("Periodo académico actualizado exitosamente", actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        periodoService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Periodo académico eliminado exitosamente", null));
    }
}
