package com.fernandoreyes.tiendawebexpress.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PedidoForm {

    @NotNull(message = "Selecciona un producto.")
    private Long productoId;

    @NotNull(message = "Ingresa una cantidad.")
    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    private Integer cantidad = 1;

    @NotBlank(message = "Ingresa tu nombre.")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres.")
    private String nombreCliente;

    @NotBlank(message = "Ingresa tu correo.")
    @Email(message = "Ingresa un correo valido.")
    @Size(max = 150, message = "El correo no puede exceder 150 caracteres.")
    private String correo;

    @Size(max = 500, message = "El comentario no puede exceder 500 caracteres.")
    private String comentario;

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
