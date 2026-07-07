package com.vircarmen.botica.controller;

import com.vircarmen.botica.dto.CajaSesionCierreRequest;
import com.vircarmen.botica.dto.CajaSesionDTO;
import com.vircarmen.botica.dto.CajaSesionRequest;
import com.vircarmen.botica.service.CajaSesionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/caja")
@RequiredArgsConstructor
public class CajaSesionController {

    private final CajaSesionService cajaSesionService;

    @PreAuthorize("hasAnyRole('ADMIN', 'CAJERO')")
    @PostMapping("/abrir/{idUsuario}")
    public ResponseEntity<CajaSesionDTO> abrirCaja(@PathVariable Integer idUsuario, @Valid @RequestBody CajaSesionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cajaSesionService.abrirCaja(idUsuario, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CAJERO')")
    @PostMapping("/cerrar/{idCaja}")
    public ResponseEntity<CajaSesionDTO> cerrarCaja(@PathVariable Integer idCaja, @Valid @RequestBody CajaSesionCierreRequest request) {
        return ResponseEntity.ok(cajaSesionService.cerrarCaja(idCaja, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CAJERO')")
    @GetMapping("/actual/{idUsuario}")
    public ResponseEntity<CajaSesionDTO> obtenerCajaActual(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(cajaSesionService.obtenerCajaActual(idUsuario));
    }
}
