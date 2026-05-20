package com.fernandoreyes.tiendawebexpress.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fernandoreyes.tiendawebexpress.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Override
    @EntityGraph(attributePaths = "categoria")
    List<Producto> findAll();

    @EntityGraph(attributePaths = "categoria")
    List<Producto> findAllByOrderByIdAsc();

    @EntityGraph(attributePaths = "categoria")
    List<Producto> findAllByCategoriaIdOrderByIdAsc(Long categoriaId);

    @EntityGraph(attributePaths = "categoria")
    List<Producto> findByNombreContainingIgnoreCaseOrderByIdAsc(String nombre);

    @EntityGraph(attributePaths = "categoria")
    List<Producto> findByCategoriaIdAndNombreContainingIgnoreCaseOrderByIdAsc(Long categoriaId, String nombre);

    @EntityGraph(attributePaths = "categoria")
    List<Producto> findTop4ByOrderByIdAsc();

    @EntityGraph(attributePaths = "categoria")
    List<Producto> findByStockGreaterThanOrderByNombreAsc(Integer stock);

    @EntityGraph(attributePaths = "categoria")
    Optional<Producto> findById(Long id);
}
