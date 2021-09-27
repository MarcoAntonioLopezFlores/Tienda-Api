package mx.edu.utez.repository;

import mx.edu.utez.model.Marca;
import mx.edu.utez.model.Producto;
import mx.edu.utez.model.Subcategoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {

	List<Producto> findByMarcaAndEstadoIsTrue (Marca marca);
	
	List<Producto> findBySubcategoriaAndEstadoIsTrue (Subcategoria subcategoria);

	List<Producto> findTop10ByOrderByFechaRegistroDesc();

	List<Producto> findAllByEstadoIsTrue();

	List<Producto> findAllByEstadoIsTrueOrderByPrecioAsc();

	List<Producto> findAllByEstadoIsTrueOrderByPrecioDesc();
}
