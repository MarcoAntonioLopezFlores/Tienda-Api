package mx.edu.utez.service;

import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.CarritoProducto;
import mx.edu.utez.repository.CarritoProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarritoProductoService {

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    public boolean eliminar(CarritoProducto carritoProducto) throws ApiRequestException {
        try{
            carritoProductoRepository.delete(carritoProducto);
            return !carritoProductoRepository.existsById(carritoProducto.getId());
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }
}
