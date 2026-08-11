package com.vircarmen.botica.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vircarmen.botica.dto.CajaSesionCierreRequest;
import com.vircarmen.botica.dto.CajaSesionDTO;
import com.vircarmen.botica.dto.CajaSesionRequest;
import com.vircarmen.botica.entity.CajaSesion;
import com.vircarmen.botica.entity.Usuario;
import com.vircarmen.botica.repository.CajaSesionRepository;
import com.vircarmen.botica.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CajaSesionService {

    private final CajaSesionRepository cajaSesionRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public CajaSesionDTO abrirCaja(Integer idUsuario, CajaSesionRequest request) {
        // Verificar que el usuario exista
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que el usuario no tenga ya una caja abierta
        boolean tieneCajaAbierta = cajaSesionRepository.findByUsuarioIdUsuarioAndEstado(idUsuario, CajaSesion.EstadoCaja.ABIERTA).isPresent();
        if (tieneCajaAbierta) {
            throw new RuntimeException("El usuario ya tiene una caja abierta");
        }

        CajaSesion caja = new CajaSesion();
        caja.setUsuario(usuario);
        caja.setFechaApertura(LocalDateTime.now());
        caja.setMontoInicial(request.montoInicial());
        caja.setEstado(CajaSesion.EstadoCaja.ABIERTA);

        return mapToDTO(cajaSesionRepository.save(caja));
    }

    @Transactional
    public CajaSesionDTO cerrarCaja(Integer idCaja, CajaSesionCierreRequest request) {
        CajaSesion caja = cajaSesionRepository.findById(idCaja)
                .orElseThrow(() -> new RuntimeException("Caja no encontrada"));

        if (caja.getEstado() == CajaSesion.EstadoCaja.CERRADA) {
            throw new RuntimeException("Esta caja ya se encuentra cerrada");
        }

        caja.setFechaCierre(LocalDateTime.now());
        caja.setMontoFinal(request.montoFinal());
        caja.setEstado(CajaSesion.EstadoCaja.CERRADA);

        return mapToDTO(cajaSesionRepository.save(caja));
    }

    public CajaSesionDTO obtenerCajaActual(Integer idUsuario) {

    return cajaSesionRepository
            .findByUsuarioIdUsuarioAndEstado(
                    idUsuario,
                    CajaSesion.EstadoCaja.ABIERTA
            )
            .map(this::mapToDTO)
            .orElse(null);
}

    private CajaSesionDTO mapToDTO(CajaSesion c) {
        return new CajaSesionDTO(
                c.getIdCajaSesion(),
                c.getUsuario().getUsername(), // asumiendo que Usuario tiene getUsername()
                c.getFechaApertura(),
                c.getFechaCierre(),
                c.getMontoInicial(),
                c.getMontoFinal(),
                c.getEstado().name()
        );
    }
}
