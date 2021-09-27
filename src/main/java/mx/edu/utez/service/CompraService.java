package mx.edu.utez.service;

import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.*;
import mx.edu.utez.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CompraService {

    @Autowired
    private BitacoraService bitacoraService;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private NumeroTarjetaRepository numeroTarjetaRepository;

    @Autowired
    private CompaniaEnvioRepository companiaEnvioRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoRepository carritoRepository;


    public Compra registrar(Compra compra, HttpServletRequest httpServletRequest) throws ApiRequestException {
        try {
            Carrito carrito= carritoRepository.findByUsuarioAndEstadoIsTrue(usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
            Optional<CompaniaEnvio> companiaEnvioOptional = companiaEnvioRepository.findById(compra.getCompaniaEnvio().getId());
            Optional<Direccion> direccionOptional = direccionRepository.findById(compra.getDireccion().getId());
            Optional<NumeroTarjeta> numeroTarjetaOptional = numeroTarjetaRepository.findById(compra.getNumeroTarjeta().getId());
            Optional<Usuario> usuarioOptional = usuarioRepository.findById(compra.getUsuario().getId());
            compra.setCarrito(carrito);
            compra.setCodigo(UUID.randomUUID().toString().substring(0, 23));
            compra.setCompaniaEnvio(companiaEnvioOptional.orElseGet(CompaniaEnvio::new));
            compra.setDireccion(direccionOptional.orElseGet(Direccion::new));
            compra.setNumeroTarjeta(numeroTarjetaOptional.orElseGet(NumeroTarjeta::new));
            compra.setUsuario(usuarioOptional.orElseGet(Usuario::new));
            Bitacora bitacora = new Bitacora(
                    "compra", compra.toString(), "", "INSERT", httpServletRequest.getRemoteAddr(),
                    usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
            bitacoraService.registrar(bitacora);
            compra = compraRepository.save(compra);
            if(compraRepository.existsById(compra.getId())){
                carrito.setEstado(false);
                carritoRepository.save(carrito);
                return compra;
            }
            return new Compra();
        } catch (Exception e) {
            throw new ApiRequestException(e);
        }
    }

    public boolean actualizar(Compra objeto, HttpServletRequest httpServletRequest) throws ApiRequestException{
        try{
            Optional<Compra> compraOptional = compraRepository.findById(objeto.getId());
            Compra compra = compraOptional.orElseGet(Compra::new);
            objeto.setCarrito(compra.getCarrito());
            objeto.setDireccion(compra.getDireccion());
            objeto.setUsuario(compra.getUsuario());
            objeto.setCompaniaEnvio(compra.getCompaniaEnvio());
            objeto.setNumeroTarjeta(compra.getNumeroTarjeta());
            Bitacora bitacora = new Bitacora(
                    "compra", objeto.toString(), compra.toString(), "UPDATE", httpServletRequest.getRemoteAddr(),
                    usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
            bitacoraService.registrar(bitacora);
            compra.setEstado(objeto.getEstado());
            compra = compraRepository.save(compra);
            return compraRepository.existsById(compra.getId());
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

    public List<Compra> listarComprasUsuario(HttpServletRequest httpServletRequest) throws ApiRequestException{
        try{
            return compraRepository.findAllByUsuario(usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

    public List<Compra> listar() throws ApiRequestException{
        try{
            return compraRepository.findAll();
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

    public Compra obtenerCompra(long id)throws ApiRequestException{
        try{
            Optional<Compra> compraOptional = compraRepository.findById(id);
            return compraOptional.orElseGet(Compra::new);
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }


}
