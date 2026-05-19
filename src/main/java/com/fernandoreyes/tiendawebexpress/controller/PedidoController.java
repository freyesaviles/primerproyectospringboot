package com.fernandoreyes.tiendawebexpress.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fernandoreyes.tiendawebexpress.dto.PedidoForm;
import com.fernandoreyes.tiendawebexpress.entity.Pedido;
import com.fernandoreyes.tiendawebexpress.exception.InsufficientStockException;
import com.fernandoreyes.tiendawebexpress.exception.ProductNotFoundException;
import com.fernandoreyes.tiendawebexpress.service.PedidoService;
import com.fernandoreyes.tiendawebexpress.service.ProductoService;

import jakarta.validation.Valid;

@Controller
public class PedidoController {

    private final PedidoService pedidoService;
    private final ProductoService productoService;

    public PedidoController(PedidoService pedidoService, ProductoService productoService) {
        this.pedidoService = pedidoService;
        this.productoService = productoService;
    }

    @GetMapping("/pedido")
    public String pedido(@RequestParam(required = false) Long productoId, Model model) {
        PedidoForm pedidoForm = new PedidoForm();
        if (productoId != null) {
            productoService.getProducto(productoId);
            pedidoForm.setProductoId(productoId);
        }
        if (pedidoForm.getProductoId() == null && !productoService.getProductosDisponibles().isEmpty()) {
            pedidoForm.setProductoId(productoService.getProductosDisponibles().getFirst().getId());
        }
        populatePedidoModel(model, pedidoForm);
        return "pedido";
    }

    @PostMapping("/pedido")
    public String crearPedido(@Valid @ModelAttribute("pedidoForm") PedidoForm pedidoForm,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            populatePedidoModel(model, pedidoForm);
            return "pedido";
        }

        try {
            Pedido pedido = pedidoService.crearPedido(pedidoForm);
            return "redirect:/pedido/confirmacion/" + pedido.getId();
        } catch (ProductNotFoundException exception) {
            bindingResult.rejectValue("productoId", "notFound", "Selecciona un producto valido.");
        } catch (InsufficientStockException exception) {
            bindingResult.rejectValue("cantidad", "stock", exception.getMessage());
        }

        populatePedidoModel(model, pedidoForm);
        return "pedido";
    }

    @GetMapping("/pedido/confirmacion/{id}")
    public String confirmacion(@PathVariable Long id, Model model) {
        model.addAttribute("currentPage", "pedido");
        model.addAttribute("categorias", productoService.getCategorias());
        model.addAttribute("pedido", pedidoService.getPedido(id));
        return "pedido-confirmacion";
    }

    private void populatePedidoModel(Model model, PedidoForm pedidoForm) {
        model.addAttribute("currentPage", "pedido");
        model.addAttribute("categorias", productoService.getCategorias());
        model.addAttribute("productosDisponibles", productoService.getProductosDisponibles());
        model.addAttribute("pedidoForm", pedidoForm);
    }
}
