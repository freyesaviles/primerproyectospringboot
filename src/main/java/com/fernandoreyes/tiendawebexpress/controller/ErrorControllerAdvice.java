package com.fernandoreyes.tiendawebexpress.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fernandoreyes.tiendawebexpress.exception.ProductNotFoundException;
import com.fernandoreyes.tiendawebexpress.service.ProductoService;

@ControllerAdvice
public class ErrorControllerAdvice {

    private final ProductoService productoService;

    public ErrorControllerAdvice(ProductoService productoService) {
        this.productoService = productoService;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProductNotFound(ProductNotFoundException exception, Model model) {
        model.addAttribute("currentPage", "");
        model.addAttribute("categorias", productoService.getCategorias());
        model.addAttribute("message", exception.getMessage());
        return "error/404";
    }
}
