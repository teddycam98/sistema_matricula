package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.entity.Estudiante;
import com.example.backend.service.IEstudianteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final IEstudianteService estudianteService;

    public EstudianteController(IEstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Estudiante>>> findAll(@RequestParam(required = false) Long carreraId) {
        List<Estudiante> list;
        if (carreraId != null) {
            list = estudianteService.findByCarreraId(carreraId);
        } else {
            list = estudianteService.findAll();
        }
        return ResponseEntity.ok(ApiResponse.ok("Estudiantes listados con éxito", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Estudiante>> findById(@PathVariable Long id) {
        Estudiante estudiante = estudianteService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok("Estudiante encontrado", estudiante));
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<ApiResponse<Estudiante>> findByDni(@PathVariable String dni) {
        Estudiante estudiante = estudianteService.findByDni(dni);
        return ResponseEntity.ok(ApiResponse.ok("Estudiante encontrado", estudiante));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ApiResponse<Estudiante>> findByCodigo(@PathVariable String codigo) {
        Estudiante estudiante = estudianteService.findByCodigoEstudiante(codigo);
        return ResponseEntity.ok(ApiResponse.ok("Estudiante encontrado", estudiante));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Estudiante>> save(@RequestBody Estudiante estudiante) {
        Estudiante creado = estudianteService.save(estudiante);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Estudiante registrado exitosamente", creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Estudiante>> update(@PathVariable Long id, @RequestBody Estudiante estudiante) {
        Estudiante actualizado = estudianteService.update(id, estudiante);
        return ResponseEntity.ok(ApiResponse.ok("Estudiante actualizado exitosamente", actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        estudianteService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Estudiante eliminado exitosamente", null));
    }
}
