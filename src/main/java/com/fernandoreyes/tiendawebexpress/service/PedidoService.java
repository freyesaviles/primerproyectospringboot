package com.fernandoreyes.tiendawebexpress.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fernandoreyes.tiendawebexpress.dto.CarritoItem;
import com.fernandoreyes.tiendawebexpress.dto.CheckoutForm;
import com.fernandoreyes.tiendawebexpress.dto.PedidoForm;
import com.fernandoreyes.tiendawebexpress.entity.DetallePedido;
import com.fernandoreyes.tiendawebexpress.entity.Pedido;
import com.fernandoreyes.tiendawebexpress.entity.Producto;
import com.fernandoreyes.tiendawebexpress.exception.InsufficientStockException;
import com.fernandoreyes.tiendawebexpress.exception.ProductNotFoundException;
import com.fernandoreyes.tiendawebexpress.repository.PedidoRepository;
import com.fernandoreyes.tiendawebexpress.repository.ProductoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Pedido crearPedido(PedidoForm pedidoForm) {
        Producto producto = productoRepository.findById(pedidoForm.getProductoId())
            .orElseThrow(() -> new ProductNotFoundException(pedidoForm.getProductoId()));

        int stockDisponible = producto.getStock();
        if (stockDisponible < pedidoForm.getCantidad()) {
            throw new InsufficientStockException(producto.getNombre(), stockDisponible);
        }

        BigDecimal precioUnitario = producto.getPrecio();
        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(pedidoForm.getCantidad()));

        Pedido pedido = new Pedido();
        pedido.setNombreCliente(pedidoForm.getNombreCliente().trim());
        pedido.setCorreo(pedidoForm.getCorreo().trim());
        pedido.setComentario(normalizeComentario(pedidoForm.getComentario()));
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setTotal(subtotal);

        DetallePedido detallePedido = new DetallePedido();
        detallePedido.setProducto(producto);
        detallePedido.setCantidad(pedidoForm.getCantidad());
        detallePedido.setPrecioUnitario(precioUnitario);
        detallePedido.setSubtotal(subtotal);

        pedido.addDetalle(detallePedido);

        producto.setStock(stockDisponible - pedidoForm.getCantidad());

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido crearPedidoDesdeCarrito(CheckoutForm checkoutForm, List<CarritoItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío.");
        }

        Pedido pedido = new Pedido();
        pedido.setNombreCliente(checkoutForm.getNombreCliente().trim());
        pedido.setCorreo(checkoutForm.getCorreo().trim());
        pedido.setComentario(normalizeComentario(checkoutForm.getComentario()));
        pedido.setFechaPedido(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (CarritoItem item : items) {
            Producto producto = productoRepository.findById(item.getProductoId())
                .orElseThrow(() -> new ProductNotFoundException(item.getProductoId()));

            int stockDisponible = producto.getStock();
            if (stockDisponible < item.getCantidad()) {
                throw new InsufficientStockException(producto.getNombre(), stockDisponible);
            }

            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad()));

            DetallePedido detallePedido = new DetallePedido();
            detallePedido.setProducto(producto);
            detallePedido.setCantidad(item.getCantidad());
            detallePedido.setPrecioUnitario(precioUnitario);
            detallePedido.setSubtotal(subtotal);
            pedido.addDetalle(detallePedido);

            producto.setStock(stockDisponible - item.getCantidad());
            total = total.add(subtotal);
        }

        pedido.setTotal(total);
        return pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public Pedido getPedido(Long id) {
        return pedidoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No se encontro el pedido " + id + "."));
    }

    private String normalizeComentario(String comentario) {
        if (comentario == null || comentario.isBlank()) {
            return null;
        }
        return comentario.trim();
    }
}
