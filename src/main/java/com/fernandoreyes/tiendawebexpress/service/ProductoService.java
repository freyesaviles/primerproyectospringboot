package com.fernandoreyes.tiendawebexpress.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fernandoreyes.tiendawebexpress.entity.Categoria;
import com.fernandoreyes.tiendawebexpress.entity.Producto;
import com.fernandoreyes.tiendawebexpress.exception.ProductNotFoundException;
import com.fernandoreyes.tiendawebexpress.repository.CategoriaRepository;
import com.fernandoreyes.tiendawebexpress.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Producto> getDestacados() {
        return productoRepository.findTop4ByOrderByIdAsc();
    }

    public List<Producto> getCatalogo(String nombre, Long categoriaId) {
        String normalizedNombre = nombre == null ? "" : nombre.trim();

        if (normalizedNombre.isEmpty() && categoriaId == null) {
            return productoRepository.findAllByOrderByIdAsc();
        }

        if (normalizedNombre.isEmpty()) {
            return productoRepository.findAllByCategoriaIdOrderByIdAsc(categoriaId);
        }

        if (categoriaId == null) {
            return productoRepository.findByNombreContainingIgnoreCaseOrderByIdAsc(normalizedNombre);
        }

        return productoRepository.findByCategoriaIdAndNombreContainingIgnoreCaseOrderByIdAsc(categoriaId, normalizedNombre);
    }

    public Producto getProducto(Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Categoria> getCategorias() {
        return categoriaRepository.findAllByOrderByNombreAsc();
    }

    public List<Producto> getProductosDisponibles() {
        return productoRepository.findByStockGreaterThanOrderByNombreAsc(0);
    }
}
