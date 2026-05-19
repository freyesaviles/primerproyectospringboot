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

    public List<Producto> getCatalogo(Long categoriaId) {
        if (categoriaId == null) {
            return productoRepository.findAllByOrderByIdAsc();
        }
        return productoRepository.findAllByCategoriaIdOrderByIdAsc(categoriaId);
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
