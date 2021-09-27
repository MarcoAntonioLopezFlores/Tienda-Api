package mx.edu.utez.repository;

import mx.edu.utez.model.Categoria;
import mx.edu.utez.model.Subcategoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcategoriaRepository extends JpaRepository<Subcategoria,Long> {

	List<Subcategoria> findAllByEstado(boolean estado);
    List<Subcategoria> findByCategoria(Categoria categoria);
}
