package com.fernandoreyes.tiendawebexpress.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fernandoreyes.tiendawebexpress.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findAllByOrderByNombreAsc();
}
