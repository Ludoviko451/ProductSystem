package com.rest.rest.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.rest.rest.entitys.Producto;
import com.rest.rest.repositorys.repositorioproducto;

@RestController // Su funcion principal es mapear los datos solicitados por los metodos y convertirlos en respuestas xml o json
@RequestMapping("/api") // Esta anotacion relaciona un metodo con una peticion http
public class controladorProducto {

    @Autowired // Permite traer los metodos de la interfaz
    repositorioproducto crud;

    // ResponseEntity: representa la respuesta http completa de una peticion

    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable("id") Integer id){ // Representa un valor incluido en la url localhost:8080/api/1
        // opcional: alternatiza al try catch
        Optional<Producto> datoProducto = crud.findById(id);

        if (datoProducto.isPresent()){
            return new ResponseEntity<>(datoProducto.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> obtenerTodos(){
        try {
            List<Producto> productos = new ArrayList<Producto>();
            crud.findAll().forEach(productos::add);
            if (productos.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productos, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscarnombre")
    public ResponseEntity<List<Producto>> buscarNombre (@RequestParam(required = false) String nombre){
        try{
            List<Producto> productos = new ArrayList<Producto>();
            crud.findByNombreContaining(nombre).forEach(productos::add);
            if (productos.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productos, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/crearproducto") // Se usa para guardar en la base de datos
    public ResponseEntity<Producto> crearProducto (@RequestBody Producto nuevoProducto){
        try {
            Producto _producto = crud.save(new Producto(nuevoProducto.getNombre(), nuevoProducto.getPrecio(), nuevoProducto.getDescripcion()));

            return new ResponseEntity<>(_producto, HttpStatus.CREATED);

            
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } 

    @PutMapping("/actualizarproducto/{id}") //Permite hacer modificacion de los registros
    public ResponseEntity<Producto> actualizarProducto(@PathVariable("id") Integer id, @RequestBody Producto productoActualizar){
        try {
            Optional<Producto> datoProducto = crud.findById(id);
            if (datoProducto.isPresent()){
                Producto _producto = datoProducto.get();
                _producto.setNombre(productoActualizar.getNombre());
                _producto.setPrecio(productoActualizar.getPrecio());
                _producto.setDescripcion(productoActualizar.getDescripcion());
                return new ResponseEntity<>(crud.save(_producto), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/eliminarproducto/{id}")
    public ResponseEntity<HttpStatus> eliminarProducto(@PathVariable("id") Integer id){
        try {
            crud.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/eliminarproductos")
    public ResponseEntity<HttpStatus> eliminarTodo(){
        try {
            crud.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}