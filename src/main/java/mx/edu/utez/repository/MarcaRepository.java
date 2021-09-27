package mx.edu.utez.repository;

import mx.edu.utez.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarcaRepository extends JpaRepository<Marca,Long> {

    List<Marca> findAllByEstado(boolean estado);
}
