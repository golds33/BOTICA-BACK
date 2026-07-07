package com.vircarmen.botica.dto;

import java.util.List;

public class VentaRequest {
    private Integer idCliente;
    private String tipoComprobante;
    private List<DetalleVentaDTO> items;
    private Integer usuarioId;

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public List<DetalleVentaDTO> getItems() {
        return items;
    }

    public void setItems(List<DetalleVentaDTO> items) {
        this.items = items;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}
