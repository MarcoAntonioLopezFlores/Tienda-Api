package mx.edu.utez.service;

import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.Bitacora;
import mx.edu.utez.repository.BitacoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BitacoraService {

    @Autowired
    private BitacoraRepository bitacoraRepository;


    public void registrar(Bitacora bitacora) throws ApiRequestException {
        try{
            bitacoraRepository.save(bitacora);
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

    public boolean verificarRegistro(long id) throws ApiRequestException {
        try {
            return bitacoraRepository.existsById(id);
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

    public List<Bitacora> listarBitacora() throws ApiRequestException {
        try {
            return bitacoraRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaRegistro"));
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }
}
