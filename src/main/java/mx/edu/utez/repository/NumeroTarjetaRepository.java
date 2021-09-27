package mx.edu.utez.repository;

import mx.edu.utez.model.NumeroTarjeta;
import mx.edu.utez.model.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NumeroTarjetaRepository extends JpaRepository<NumeroTarjeta,Long> {
	
	List<NumeroTarjeta> findAllByUsuarioAndEstadoIsTrue(Usuario usuario);

	NumeroTarjeta findByDescripcion(String numero);
	
}
