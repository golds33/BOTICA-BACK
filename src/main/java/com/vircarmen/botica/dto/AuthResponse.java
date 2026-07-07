package com.vircarmen.botica.dto;

public record AuthResponse(
        String token,
        String username,
        String nombreCompleto,
        String rol // Enviamos el rol en texto (ej. "ADMIN" o "CAJERO")
) {}
