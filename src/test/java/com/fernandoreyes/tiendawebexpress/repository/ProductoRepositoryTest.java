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

    @Test
    void findByNombreContainingIgnoreCaseOrderByIdAscBuscaSinImportarMayusculas() {
        Categoria tecnologia = new Categoria();
        tecnologia.setNombre("Tecnología");
        tecnologia = categoriaRepository.save(tecnologia);

        Categoria ropa = new Categoria();
        ropa.setNombre("Ropa");
        ropa = categoriaRepository.save(ropa);

        productoRepository.save(producto("Mouse inalámbrico", tecnologia));
        productoRepository.save(producto("Camiseta básica", ropa));

        List<Producto> productos = productoRepository.findByNombreContainingIgnoreCaseOrderByIdAsc("mOuSe");

        assertThat(productos).hasSize(1);
        assertThat(productos.getFirst().getNombre()).isEqualTo("Mouse inalámbrico");
        assertThat(productos.getFirst().getCategoria().getNombre()).isEqualTo("Tecnología");
    }

    @Test
    void findByCategoriaIdAndNombreContainingIgnoreCaseOrderByIdAscCombinaFiltros() {
        Categoria tecnologia = new Categoria();
        tecnologia.setNombre("Tecnología");
        tecnologia = categoriaRepository.save(tecnologia);

        Categoria ropa = new Categoria();
        ropa.setNombre("Ropa");
        ropa = categoriaRepository.save(ropa);

        productoRepository.save(producto("Teclado mecánico", tecnologia));
        productoRepository.save(producto("Mouse inalámbrico", tecnologia));
        productoRepository.save(producto("Mouse deportivo", ropa));

        List<Producto> tecnologiaTeclado = productoRepository
            .findByCategoriaIdAndNombreContainingIgnoreCaseOrderByIdAsc(tecnologia.getId(), "teclado");
        List<Producto> ropaMouse = productoRepository
            .findByCategoriaIdAndNombreContainingIgnoreCaseOrderByIdAsc(ropa.getId(), "mouse");

        assertThat(tecnologiaTeclado).hasSize(1);
        assertThat(tecnologiaTeclado.getFirst().getNombre()).isEqualTo("Teclado mecánico");
        assertThat(tecnologiaTeclado.getFirst().getCategoria().getNombre()).isEqualTo("Tecnología");
        assertThat(ropaMouse).hasSize(1);
        assertThat(ropaMouse.getFirst().getNombre()).isEqualTo("Mouse deportivo");
    }

    @Test
    void findByCategoriaIdAndNombreContainingIgnoreCaseOrderByIdAscRetornaVacioSinCoincidencias() {
        Categoria tecnologia = new Categoria();
        tecnologia.setNombre("Tecnología");
        tecnologia = categoriaRepository.save(tecnologia);

        Categoria ropa = new Categoria();
        ropa.setNombre("Ropa");
        ropa = categoriaRepository.save(ropa);

        productoRepository.save(producto("Mouse inalámbrico", tecnologia));
        productoRepository.save(producto("Camiseta básica", ropa));

        List<Producto> productos = productoRepository
            .findByCategoriaIdAndNombreContainingIgnoreCaseOrderByIdAsc(ropa.getId(), "mouse");

        assertThat(productos).isEmpty();
    }

    private Producto producto(String nombre, Categoria categoria) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion("Producto de prueba");
        producto.setPrecio(new BigDecimal("12.99"));
        producto.setImagen("producto.jpg");
        producto.setStock(5);
        producto.setCategoria(categoria);
        return producto;
    }
}
