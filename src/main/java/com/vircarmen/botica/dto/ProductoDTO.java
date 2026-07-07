package com.vircarmen.botica.dto;
import java.math.BigDecimal;

public record ProductoDTO(
        Integer idProducto,
        String nombre,
        String codigoBarras,
        BigDecimal precioVenta,
        Integer stockActual,
        String nombreCategoria
) {}