package mx.edu.utez.repository;

import mx.edu.utez.model.TokenCorreo;
import mx.edu.utez.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenCorreoRepository extends JpaRepository<TokenCorreo, Long> {

    TokenCorreo findByUsuarioAndEstadoIsTrue(Usuario usuario);
    TokenCorreo findByDescripcionAndEstadoIsTrue(String token);
}
