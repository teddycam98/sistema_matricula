package com.example.backend.service.impl;

import com.example.backend.entity.Matricula;
import com.example.backend.entity.Pago;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.MatriculaRepository;
import com.example.backend.repository.PagoRepository;
import com.example.backend.service.IPagoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PagoServiceImpl implements IPagoService {

    private final PagoRepository pagoRepository;
    private final MatriculaRepository matriculaRepository;

    public PagoServiceImpl(PagoRepository pagoRepository, MatriculaRepository matriculaRepository) {
        this.pagoRepository = pagoRepository;
        this.matriculaRepository = matriculaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Pago findById(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> findByMatriculaId(Long matriculaId) {
        return pagoRepository.findByMatriculaId(matriculaId);
    }

    @Override
    public Pago registrarPago(Long matriculaId, String metodoPago) {
        Matricula matricula = matriculaRepository.findById(matriculaId)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada con ID: " + matriculaId));

        if ("ANULADA".equalsIgnoreCase(matricula.getEstado())) {
            throw new BadRequestException("No se puede registrar pago para una matrícula ANULADA.");
        }
        if ("PAGADA".equalsIgnoreCase(matricula.getEstado())) {
            throw new BadRequestException("La matrícula ya ha sido PAGADA en su totalidad.");
        }

        Pago pago = new Pago();
        pago.setMatricula(matricula);
        pago.setNumeroOperacion("OP-" + System.currentTimeMillis());
        pago.setMonto(matricula.getCostoTotal());
        pago.setMetodoPago(metodoPago != null ? metodoPago.toUpperCase() : "EFECTIVO");
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("PAGADO");

        matricula.setEstado("PAGADA");
        matriculaRepository.save(matricula);

        return pagoRepository.save(pago);
    }
}
