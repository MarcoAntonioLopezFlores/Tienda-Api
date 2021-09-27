package mx.edu.utez.repository;

import mx.edu.utez.model.Telefono;
import mx.edu.utez.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelefonoRepository extends JpaRepository<Telefono,Long> {

    List<Telefono> findAllByUsuario(Usuario usuario);

    Telefono findByDescripcion(String telefono);
}
