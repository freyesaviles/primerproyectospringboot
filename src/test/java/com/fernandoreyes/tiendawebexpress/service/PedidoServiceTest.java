package com.fernandoreyes.tiendawebexpress.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fernandoreyes.tiendawebexpress.dto.PedidoForm;
import com.fernandoreyes.tiendawebexpress.entity.Categoria;
import com.fernandoreyes.tiendawebexpress.entity.Pedido;
import com.fernandoreyes.tiendawebexpress.entity.Producto;
import com.fernandoreyes.tiendawebexpress.exception.InsufficientStockException;
import com.fernandoreyes.tiendawebexpress.repository.PedidoRepository;
import com.fernandoreyes.tiendawebexpress.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void crearPedidoCalculaSubtotalYDescuentaStock() {
        Producto producto = producto("Mouse inalámbrico", new BigDecimal("12.99"), 10);
        PedidoForm form = pedidoForm(producto.getId(), 2);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pedido resultado = pedidoService.crearPedido(form);

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(pedidoCaptor.capture());

        Pedido pedidoGuardado = pedidoCaptor.getValue();
        assertThat(resultado.getTotal()).isEqualByComparingTo("25.98");
        assertThat(pedidoGuardado.getDetalles()).hasSize(1);
        assertThat(pedidoGuardado.getDetalles().getFirst().getSubtotal()).isEqualByComparingTo("25.98");
        assertThat(producto.getStock()).isEqualTo(8);
    }

    @Test
    void crearPedidoFallaCuandoNoHayStockSuficiente() {
        Producto producto = producto("Teclado mecánico", new BigDecimal("39.99"), 1);
        PedidoForm form = pedidoForm(producto.getId(), 3);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertThatThrownBy(() -> pedidoService.crearPedido(form))
            .isInstanceOf(InsufficientStockException.class)
            .hasMessageContaining("No hay stock suficiente");
    }

    private Producto producto(String nombre, BigDecimal precio, int stock) {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Tecnología");

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre(nombre);
        producto.setCategoria(categoria);
        producto.setPrecio(precio);
        producto.setStock(stock);
        return producto;
    }

    private PedidoForm pedidoForm(Long productoId, int cantidad) {
        PedidoForm form = new PedidoForm();
        form.setProductoId(productoId);
        form.setCantidad(cantidad);
        form.setNombreCliente("Fernando Reyes");
        form.setCorreo("fernando@correo.com");
        form.setComentario("Prueba");
        return form;
    }
}
