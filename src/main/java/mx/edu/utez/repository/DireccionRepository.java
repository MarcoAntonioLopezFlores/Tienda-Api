package mx.edu.utez.repository;

import mx.edu.utez.model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion,Long> {

    Direccion findByAsentamientoAndCalleAndCodigoPostalAndEntidadFederativaAndMunicipioAndNumeroExteriorAndTipoAsentamiento
            (String asentamiento, String calle, String cp, String entidadFederativa, String municipio, String numeroExterior, String tipoAsentamiento);
}
