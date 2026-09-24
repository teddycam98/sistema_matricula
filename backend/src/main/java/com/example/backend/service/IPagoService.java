package com.example.backend.service;

import com.example.backend.entity.Pago;
import java.util.List;

public interface IPagoService {
    List<Pago> findAll();
    Pago findById(Long id);
    List<Pago> findByMatriculaId(Long matriculaId);
    Pago registrarPago(Long matriculaId, String metodoPago);
}
