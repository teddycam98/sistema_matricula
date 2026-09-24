package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.entity.Docente;
import com.example.backend.service.IDocenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/docentes")
public class DocenteController {

    private final IDocenteService docenteService;

    public DocenteController(IDocenteService docenteService) {
        this.docenteService = docenteService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Docente>>> findAll() {
        List<Docente> list = docenteService.findAll();
        return ResponseEntity.ok(ApiResponse.ok("Docentes listados con éxito", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Docente>> findById(@PathVariable Long id) {
        Docente docente = docenteService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok("Docente encontrado", docente));
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<ApiResponse<Docente>> findByDni(@PathVariable String dni) {
        Docente docente = docenteService.findByDni(dni);
        return ResponseEntity.ok(ApiResponse.ok("Docente encontrado", docente));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Docente>> save(@RequestBody Docente docente) {
        Docente creado = docenteService.save(docente);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Docente registrado exitosamente", creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Docente>> update(@PathVariable Long id, @RequestBody Docente docente) {
        Docente actualizado = docenteService.update(id, docente);
        return ResponseEntity.ok(ApiResponse.ok("Docente actualizado exitosamente", actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        docenteService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Docente eliminado exitosamente", null));
    }
}
