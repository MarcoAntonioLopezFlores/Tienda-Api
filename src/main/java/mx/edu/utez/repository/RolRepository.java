package mx.edu.utez.repository;

import mx.edu.utez.model.Rol;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolRepository extends JpaRepository<Rol,Long> {

    List<Rol> findByDescripcionNot(String descripcion);
    Rol findByNombre(String nombre);
    Rol findByNombreAndDescripcion(String nombre, String descripcion);
	
}
