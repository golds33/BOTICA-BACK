package com.vircarmen.botica.service;

import com.vircarmen.botica.dto.CompraRequest;
import com.vircarmen.botica.dto.VentaRequest;
import com.vircarmen.botica.entity.*;
import com.vircarmen.botica.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;
    private final MovimientoRepository movimientoRepository;
    private final DetalleMovimientoRepository detalleMovimientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProveedorRepository proveedorRepository;

    @Transactional
    public Movimiento registrarIngreso(CompraRequest request) {
        Usuario usuario = usuarioRepository.findById(Integer.valueOf(request.getUsuarioId())).orElseThrow();
        Proveedor proveedor = proveedorRepository.findById(Integer.valueOf(request.getProveedorId())).orElseThrow();
        Producto producto = productoRepository.findById(Integer.valueOf(request.getProductoId())).orElseThrow();

        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento(TipoMovimiento.INGRESO);
        movimiento.setUsuario(usuario);
        movimiento.setProveedor(proveedor);
        movimiento.setMotivo(request.getMotivo());
        movimientoRepository.save(movimiento);

        Lote lote = new Lote();
        lote.setCodigoLote(request.getCodigoLote());
        lote.setFechaIngreso(LocalDate.now());
        lote.setFechaVencimiento(LocalDate.parse(request.getFechaVencimiento()));
        lote.setCantidadLote(request.getCantidad());
        lote.setProducto(producto);
        loteRepository.save(lote);

        DetalleMovimiento detalle = new DetalleMovimiento();
        detalle.setMovimiento(movimiento);
        detalle.setLote(lote);
        detalle.setCantidad(request.getCantidad());
        detalle.setPrecioUnitario(BigDecimal.valueOf(request.getPrecioUnitario()));
        detalleMovimientoRepository.save(detalle);

        producto.setStockActual(producto.getStockActual() + request.getCantidad());
        productoRepository.save(producto);

        return movimiento;
    }

    @Transactional
    public Movimiento registrarVenta(VentaRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId()).orElseThrow();

        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento(TipoMovimiento.SALIDA);
        movimiento.setUsuario(usuario);
        movimiento.setMotivo("Venta directa");
        movimientoRepository.save(movimiento);

        for (var item : request.getItems()) {
            Producto producto = productoRepository.findById(Integer.valueOf(item.idProducto())).orElseThrow();
            if (producto.getStockActual() < item.cantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto " + producto.getNombre());
            }

            producto.setStockActual(producto.getStockActual() - item.cantidad());
            productoRepository.save(producto);

            DetalleMovimiento detalle = new DetalleMovimiento();
            detalle.setMovimiento(movimiento);
            detalle.setCantidad(item.cantidad());
            detalle.setPrecioUnitario(producto.getPrecioVenta());
            detalleMovimientoRepository.save(detalle);
        }

        return movimiento;
    }

    @Transactional
    public Movimiento registrarAnulacion(Integer movimientoId, String motivo, Integer usuarioId) {
        movimientoRepository.findById(Integer.valueOf(movimientoId)).orElseThrow();
        Usuario usuario = usuarioRepository.findById(Integer.valueOf(usuarioId)).orElseThrow();

        Movimiento anulacion = new Movimiento();
        anulacion.setTipoMovimiento(TipoMovimiento.ANULACION);
        anulacion.setUsuario(usuario);
        anulacion.setMotivo(motivo);
        anulacion.setReferenciaAnulacion(movimientoId);
        movimientoRepository.save(anulacion);

        var detalles = detalleMovimientoRepository.findByMovimientoIdMovimiento(movimientoId);
        for (DetalleMovimiento detalle : detalles) {
            Producto producto = detalle.getLote().getProducto();
            producto.setStockActual(producto.getStockActual() + detalle.getCantidad());
            productoRepository.save(producto);
        }

        return anulacion;
    }
}
