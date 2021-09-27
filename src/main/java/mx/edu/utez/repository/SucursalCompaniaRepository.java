package mx.edu.utez.repository;

import mx.edu.utez.model.CompaniaEnvio;
import mx.edu.utez.model.Direccion;
import mx.edu.utez.model.SucursalCompania;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalCompaniaRepository extends JpaRepository<SucursalCompania,Long> {

    SucursalCompania findByCompaniaEnvioAndDireccion(CompaniaEnvio companiaEnvio, Direccion direccion);
    
    List<SucursalCompania> findAllByCompaniaEnvio(CompaniaEnvio companiaEnvio);
}
