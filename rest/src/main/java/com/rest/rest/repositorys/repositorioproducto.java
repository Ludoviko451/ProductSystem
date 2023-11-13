package com.rest.rest.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.rest.entitys.Producto;


public interface repositorioproducto extends JpaRepository<Producto, Integer> {

    List<Producto> findByNombreContaining(String nombre);
}