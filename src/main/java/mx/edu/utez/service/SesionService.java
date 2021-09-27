package mx.edu.utez.service;

import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.Sesion;
import mx.edu.utez.repository.SesionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SesionService {
    @Autowired
    private SesionRepository sesionRepository;

    public List<Sesion> obtenerSesiones()
            throws ApiRequestException {
        try {
            return sesionRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaRegistro"));
        } catch (Exception e) {
            throw new ApiRequestException(e);
        }
    }

    public void registrarSesion(Sesion sesion) throws ApiRequestException {
        try {
            sesionRepository.save(sesion);
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

}
