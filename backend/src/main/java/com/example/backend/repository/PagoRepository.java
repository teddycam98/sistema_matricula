package com.example.backend.repository;

import com.example.backend.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByNumeroOperacion(String numeroOperacion);
    List<Pago> findByMatriculaId(Long matriculaId);
    List<Pago> findByEstado(String estado);

    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p WHERE p.estado = 'PAGADO'")
    BigDecimal sumTotalRecaudado();
}
