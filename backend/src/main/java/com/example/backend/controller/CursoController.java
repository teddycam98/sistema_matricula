package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.entity.Curso;
import com.example.backend.service.ICursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final ICursoService cursoService;

    public CursoController(ICursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Curso>>> findAll(
            @RequestParam(required = false) Long planEstudioId,
            @RequestParam(required = false) Integer ciclo) {
        List<Curso> list;
        if (planEstudioId != null) {
            list = cursoService.findByPlanEstudioId(planEstudioId);
        } else if (ciclo != null) {
            list = cursoService.findByCiclo(ciclo);
        } else {
            list = cursoService.findAll();
        }
        return ResponseEntity.ok(ApiResponse.ok("Cursos listados con éxito", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Curso>> findById(@PathVariable Long id) {
        Curso curso = cursoService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok("Curso encontrado", curso));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Curso>> save(@RequestBody Curso curso) {
        Curso creado = cursoService.save(curso);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Curso registrado exitosamente", creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Curso>> update(@PathVariable Long id, @RequestBody Curso curso) {
        Curso actualizado = cursoService.update(id, curso);
        return ResponseEntity.ok(ApiResponse.ok("Curso actualizado exitosamente", actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        cursoService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Curso eliminado exitosamente", null));
    }
}
