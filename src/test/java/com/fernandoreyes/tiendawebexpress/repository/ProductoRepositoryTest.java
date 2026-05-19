package com.fernandoreyes.tiendawebexpress.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.fernandoreyes.tiendawebexpress.entity.Categoria;
import com.fernandoreyes.tiendawebexpress.entity.Producto;

@DataJpaTest
class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void findAllByOrderByIdAscTraeCategoriaRelacionada() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Tecnología");
        categoria = categoriaRepository.save(categoria);

        Producto producto = new Producto();
        producto.setNombre("Mouse inalámbrico");
        producto.setDescripcion("Mouse de prueba");
        producto.setPrecio(new BigDecimal("12.99"));
        producto.setImagen("mouse.jpg");
        producto.setStock(5);
        producto.setCategoria(categoria);
        productoRepository.save(producto);

        List<Producto> productos = productoRepository.findAllByOrderByIdAsc();

        assertThat(productos).hasSize(1);
        assertThat(productos.getFirst().getCategoria().getNombre()).isEqualTo("Tecnología");
    }
}
