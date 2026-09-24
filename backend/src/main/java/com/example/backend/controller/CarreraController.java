package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.entity.Carrera;
import com.example.backend.service.ICarreraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carreras")
public class CarreraController {

    private final ICarreraService carreraService;

    public CarreraController(ICarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Carrera>>> findAll() {
        List<Carrera> list = carreraService.findAll();
        return ResponseEntity.ok(ApiResponse.ok("Carreras listadas con éxito", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Carrera>> findById(@PathVariable Long id) {
        Carrera carrera = carreraService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok("Carrera encontrada", carrera));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Carrera>> save(@RequestBody Carrera carrera) {
        Carrera creada = carreraService.save(carrera);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Carrera registrada exitosamente", creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Carrera>> update(@PathVariable Long id, @RequestBody Carrera carrera) {
        Carrera actualizada = carreraService.update(id, carrera);
        return ResponseEntity.ok(ApiResponse.ok("Carrera actualizada exitosamente", actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        carreraService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Carrera eliminada exitosamente", null));
    }
}
