package mx.edu.utez.repository;

import mx.edu.utez.model.CompaniaEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompaniaEnvioRepository extends JpaRepository<CompaniaEnvio,Long> {

    CompaniaEnvio findByRfcAndDescripcion(String rfc, String descripcion);
    List<CompaniaEnvio> findAllByEstadoIsTrue();
}
