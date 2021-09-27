package mx.edu.utez.repository;

import mx.edu.utez.model.Direccion;
import mx.edu.utez.model.DireccionUsuario;
import mx.edu.utez.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DireccionUsuarioRepository extends JpaRepository<DireccionUsuario,Long> {

    DireccionUsuario findByDireccionAndUsuario(Direccion direccion,Usuario usuario);
    
    List<DireccionUsuario> findAllByUsuario(Usuario usuario);


}
