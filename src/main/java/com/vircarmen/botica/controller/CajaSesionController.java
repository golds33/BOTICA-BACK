package com.vircarmen.botica.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vircarmen.botica.dto.CajaSesionCierreRequest;
import com.vircarmen.botica.dto.CajaSesionDTO;
import com.vircarmen.botica.dto.CajaSesionRequest;
import com.vircarmen.botica.service.CajaSesionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/caja")
@RequiredArgsConstructor
public class CajaSesionController {

    private final CajaSesionService cajaSesionService;

   // Usamos hasAnyRole (Spring automáticamente buscará "ROLE_ADMIN" y "ROLE_CAJERO")
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
public ResponseEntity<CajaSesionDTO> obtenerCajaActual(
        @PathVariable Integer idUsuario
) {

    CajaSesionDTO caja =
            cajaSesionService.obtenerCajaActual(idUsuario);

    if (caja == null) {
        return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(caja);
}
}