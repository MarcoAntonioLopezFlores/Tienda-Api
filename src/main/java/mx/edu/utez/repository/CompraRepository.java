package mx.edu.utez.repository;

import mx.edu.utez.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra,Long> {


    List<Compra> findAllByUsuario(Usuario usuario);

}
