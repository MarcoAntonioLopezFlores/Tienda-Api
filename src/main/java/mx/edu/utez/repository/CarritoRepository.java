package mx.edu.utez.repository;

import mx.edu.utez.model.Carrito;
import mx.edu.utez.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito,Long> {

    Carrito findByUsuarioAndEstadoIsTrue(Usuario usuario);
}
