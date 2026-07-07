package com.vircarmen.botica.repository;

import com.vircarmen.botica.entity.Producto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Búsqueda rápida por lector de código de barras
    Optional<Producto> findByCodigoBarras(String codigoBarras);
    
    // Para alertas en el dashboard: productos que se están agotando
    List<Producto> findByStockActualLessThanEqual(Integer stockMinimo);
}
