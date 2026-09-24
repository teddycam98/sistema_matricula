package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.entity.Seccion;
import com.example.backend.service.ISeccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/secciones")
public class SeccionController {

    private final ISeccionService seccionService;

    public SeccionController(ISeccionService seccionService) {
        this.seccionService = seccionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Seccion>>> findAll(@RequestParam(required = false) Long periodoId) {
        List<Seccion> list;
        if (periodoId != null) {
            list = seccionService.findByPeriodoId(periodoId);
        } else {
            list = seccionService.findAll();
        }
        return ResponseEntity.ok(ApiResponse.ok("Secciones listadas con éxito", list));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<ApiResponse<List<Seccion>>> findDisponibles(@RequestParam Long periodoId) {
        List<Seccion> list = seccionService.findDisponiblesByPeriodo(periodoId);
        return ResponseEntity.ok(ApiResponse.ok("Secciones disponibles listadas con éxito", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Seccion>> findById(@PathVariable Long id) {
        Seccion seccion = seccionService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok("Sección encontrada", seccion));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Seccion>> save(@RequestBody Seccion seccion) {
        Seccion creada = seccionService.save(seccion);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Sección registrada exitosamente", creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Seccion>> update(@PathVariable Long id, @RequestBody Seccion seccion) {
        Seccion actualizada = seccionService.update(id, seccion);
        return ResponseEntity.ok(ApiResponse.ok("Sección actualizada exitosamente", actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        seccionService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Sección eliminada exitosamente", null));
    }
}
