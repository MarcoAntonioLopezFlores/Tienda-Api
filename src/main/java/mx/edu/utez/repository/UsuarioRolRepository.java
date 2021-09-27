package mx.edu.utez.repository;

import mx.edu.utez.model.Usuario;
import mx.edu.utez.model.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol,Long> {

    List<UsuarioRol> findByUsuario(Usuario usuario);
}
