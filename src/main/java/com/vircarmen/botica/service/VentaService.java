package com.vircarmen.botica.service;

import com.vircarmen.botica.dto.DetalleVentaDTO;
import com.vircarmen.botica.dto.VentaRequest;
import com.vircarmen.botica.entity.*;
import com.vircarmen.botica.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final LoteRepository loteRepository;

    @Transactional
    public Venta registrarVenta(VentaRequest request) {
        // 1. Validar Cliente
        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Error: Cliente no encontrado en la base de datos."));

        // 2. Crear cabecera de la Venta
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setTipoComprobante(request.getTipoComprobante());
        venta.setFechaEmision(LocalDateTime.now());
        
        // Simulación de Serie y Correlativo (En producción, esto se consulta de una tabla de Series)
        venta.setSerie(request.getTipoComprobante().equalsIgnoreCase("FACTURA") ? "F001" : "B001");
        venta.setCorrelativo(String.format("%08d", ventaRepository.count() + 1));
        
        // Guardamos temporalmente para obtener el ID de la Venta
        Venta ventaGuardada = ventaRepository.save(venta);

        BigDecimal totalVenta = BigDecimal.ZERO;
        List<DetalleVenta> detallesParaGuardar = new ArrayList<>();

        // 3. Procesar los productos (Lector óptico / Carrito)
        for (DetalleVentaDTO item : request.getItems()) {
            Producto producto = productoRepository.findById(item.idProducto())
                    .orElseThrow(() -> new RuntimeException("Error: Producto no encontrado con ID " + item.idProducto()));

            // 3.1. Validar Stock General
            if (producto.getStockActual() < item.cantidad()) {
                throw new RuntimeException("Error: Stock insuficiente para el producto " + producto.getNombre() + 
                                           ". Stock actual: " + producto.getStockActual());
            }

            // 3.2. Obtener lotes disponibles ordenados por vencimiento (FEFO)
            List<Lote> lotesDisponibles = loteRepository.findLotesDisponiblesByProductoOrderByFechaVencimientoAsc(producto.getIdProducto());
            
            int cantidadFaltante = item.cantidad();

            for (Lote lote : lotesDisponibles) {
                if (cantidadFaltante <= 0) break;

                int cantidadATomar = Math.min(lote.getCantidadLote(), cantidadFaltante);
                
                // Descontar del lote
                lote.setCantidadLote(lote.getCantidadLote() - cantidadATomar);
                loteRepository.save(lote);

                cantidadFaltante -= cantidadATomar;

                // Calcular subtotal de esta fracción
                BigDecimal subtotalLinea = producto.getPrecioVenta().multiply(new BigDecimal(cantidadATomar));
                totalVenta = totalVenta.add(subtotalLinea);

                // Crear detalle referenciando al LOTE específico
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(ventaGuardada);
                detalle.setLote(lote);
                detalle.setCantidad(cantidadATomar);
                detalle.setPrecioUnitario(producto.getPrecioVenta());
                detalle.setSubtotal(subtotalLinea);
                
                detallesParaGuardar.add(detalle);
            }

            if (cantidadFaltante > 0) {
                 throw new RuntimeException("Error: Inconsistencia de stock en lotes para el producto " + producto.getNombre());
            }

            // 3.3. Descontar el stock general y guardar el producto
            producto.setStockActual(producto.getStockActual() - item.cantidad());
            productoRepository.save(producto);
        }

        // 4. Guardar todos los detalles en bloque (Optimizado)
        detalleVentaRepository.saveAll(detallesParaGuardar);

        // 5. Calcular Impuestos (IGV Perú: 18%)
        // Formula: Subtotal = Total / 1.18 || IGV = Total - Subtotal
        BigDecimal divisorIgv = new BigDecimal("1.18");
        BigDecimal subtotalNeto = totalVenta.divide(divisorIgv, 2, RoundingMode.HALF_UP);
        BigDecimal igv = totalVenta.subtract(subtotalNeto);

        // 6. Actualizar la cabecera final
        ventaGuardada.setSubtotal(subtotalNeto);
        ventaGuardada.setIgv(igv);
        ventaGuardada.setTotal(totalVenta);
        
        return ventaRepository.save(ventaGuardada);
    }
}