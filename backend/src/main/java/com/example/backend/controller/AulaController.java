package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.entity.Aula;
import com.example.backend.service.IAulaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aulas")
public class AulaController {

    private final IAulaService aulaService;

    public AulaController(IAulaService aulaService) {
        this.aulaService = aulaService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Aula>>> findAll() {
        List<Aula> list = aulaService.findAll();
        return ResponseEntity.ok(ApiResponse.ok("Aulas listadas con éxito", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Aula>> findById(@PathVariable Long id) {
        Aula aula = aulaService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok("Aula encontrada", aula));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Aula>> save(@RequestBody Aula aula) {
        Aula creada = aulaService.save(aula);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Aula registrada exitosamente", creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Aula>> update(@PathVariable Long id, @RequestBody Aula aula) {
        Aula actualizada = aulaService.update(id, aula);
        return ResponseEntity.ok(ApiResponse.ok("Aula actualizada exitosamente", actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        aulaService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Aula eliminada exitosamente", null));
    }
}
