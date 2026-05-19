package com.fernandoreyes.tiendawebexpress.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fernandoreyes.tiendawebexpress.service.ProductoService;

@Controller
public class HomeController {

    private final ProductoService productoService;

    public HomeController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("currentPage", "inicio");
        model.addAttribute("categorias", productoService.getCategorias());
        model.addAttribute("destacados", productoService.getDestacados());
        return "index";
    }
}
