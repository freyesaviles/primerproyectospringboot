package com.fernandoreyes.tiendawebexpress.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.fernandoreyes.tiendawebexpress.service.ProductoService;

@Controller
public class CatalogoController {

    private final ProductoService productoService;

    public CatalogoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(required = false) String nombre,
                           @RequestParam(required = false) Long categoria,
                           Model model) {
        String normalizedNombre = nombre == null ? "" : nombre.trim();
        model.addAttribute("currentPage", "catalogo");
        model.addAttribute("categorias", productoService.getCategorias());
        model.addAttribute("productos", productoService.getCatalogo(normalizedNombre, categoria));
        model.asMap().put("selectedCategoryId", categoria);
        model.addAttribute("searchTerm", normalizedNombre);
        return "catalogo";
    }

    @GetMapping("/productos/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("currentPage", "catalogo");
        model.addAttribute("categorias", productoService.getCategorias());
        model.addAttribute("producto", productoService.getProducto(id));
        return "producto-detalle";
    }
}
