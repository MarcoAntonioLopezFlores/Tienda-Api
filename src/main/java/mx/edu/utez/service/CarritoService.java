package mx.edu.utez.service;

import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.Bitacora;
import mx.edu.utez.model.Carrito;
import mx.edu.utez.model.CarritoProducto;
import mx.edu.utez.model.Producto;
import mx.edu.utez.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BitacoraService bitacoraService;

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;


    public boolean agregarProductoCarrito(long id, HttpServletRequest httpServletRequest) throws ApiRequestException {
        try{
            Optional<Producto> productoOptional = productoRepository.findById(id);
            Producto producto = new Producto();
            if(productoOptional.isPresent()){
                producto = productoOptional.get();
            }
            if(producto.getExistencia()!=0){
                Carrito carrito= carritoRepository.findByUsuarioAndEstadoIsTrue(usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
                Bitacora bitacora;
                if(carrito==null){
                    carrito = new Carrito(0,usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
                    carrito=carritoRepository.save(carrito);
                    bitacora = new Bitacora(
                            "carrito", carrito.toString(), "", "INSERT", httpServletRequest.getRemoteAddr(),
                            usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
                    bitacoraService.registrar(bitacora);
                }
                CarritoProducto carritoProducto = carritoProductoRepository.findByCarritoAndProducto(carrito, producto);
                if(carritoProducto!=null){
                    carritoProducto.setCantidad(carritoProducto.getCantidad()+1);
                    carritoProducto.setPrecio((carritoProducto.getCantidad()*producto.getPrecio()));
                    carritoProducto= carritoProductoRepository.save(carritoProducto);
                    producto.setExistencia(producto.getExistencia()-1);
                    producto =productoRepository.save(producto);
                    if(producto.getExistencia()==0){
                        producto.setEstado(false);
                        productoRepository.save(producto);
                    }
                }else{
                    carritoProducto = carritoProductoRepository.save(new CarritoProducto(1,carrito, producto,producto.getPrecio()));
                    producto.setExistencia(producto.getExistencia()-1);
                    producto=productoRepository.save(producto);
                }
                carrito.setTotal(carrito.getTotal()+(producto.getPrecio()));
                carritoRepository.save(carrito);
                return  productoRepository.existsById(producto.getId()) && carritoProductoRepository.existsById(carritoProducto.getId());
            }
            return false;

        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }
    public boolean eliminarProducto(long id, HttpServletRequest httpServletRequest) throws ApiRequestException {
        try{
            Optional<Producto> productoOptional = productoRepository.findById(id);
            Producto producto = new Producto();
            if(productoOptional.isPresent()){
                producto = productoOptional.get();
            }
            Carrito carrito= carritoRepository.findByUsuarioAndEstadoIsTrue(usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
            CarritoProducto carritoProducto = carritoProductoRepository.findByCarritoAndProducto(carrito, producto);
            if(carritoProducto!=null){
                producto.setExistencia(producto.getExistencia()+carritoProducto.getCantidad());
                carrito.setTotal(carrito.getTotal() - carritoProducto.getPrecio());
                producto= productoRepository.save(producto);
                carritoRepository.save(carrito);
                carritoProductoRepository.delete(carritoProducto);

                return !carritoProductoRepository.existsById(carritoProducto.getId()) &&productoRepository.existsById(producto.getId());
            }
            return false;
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }
    public List<CarritoProducto> listarCarrito(HttpServletRequest httpServletRequest) throws ApiRequestException {
        try{
            Carrito carrito= carritoRepository.findByUsuarioAndEstadoIsTrue(usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
            return carritoProductoRepository.findAllByCarrito(carrito);
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

    public int totalCarrito(HttpServletRequest httpServletRequest) throws ApiRequestException {
        try{
            Carrito carrito= carritoRepository.findByUsuarioAndEstadoIsTrue(usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
            return carritoProductoRepository.findAllByCarrito(carrito).stream().mapToInt(CarritoProducto::getCantidad).sum();
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }
    
    public List<CarritoProducto> compraListarCarrito(long id ) throws ApiRequestException {
        try{
            Optional<Carrito> carritoOptional = carritoRepository.findById(id);
            Carrito carrito = new Carrito();
            if(carritoOptional.isPresent()) {
                carrito = carritoOptional.get();
            }
            return carritoProductoRepository.findAllByCarrito(carrito);
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

    public boolean limpiarCarrito(long id, HttpServletRequest httpServletRequest)throws ApiRequestException {
        try{
            Optional<Carrito> carritoOptional = carritoRepository.findById(id);
            Carrito carrito = new Carrito();
            if(carritoOptional.isPresent()) {
                carrito = carritoOptional.get();
            }
            List<CarritoProducto> carritoProductos = carritoProductoRepository.findAllByCarrito(carrito);
            Bitacora bitacora = new Bitacora(
                    "carrito", "Sin información nueva", carrito.toString(), "DELETE", httpServletRequest.getRemoteAddr(),
                    usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
            bitacoraService.registrar(bitacora);
            carritoProductos.forEach(carritoProducto -> {
                Optional<Producto> productoOptional = productoRepository.findById(carritoProducto.getProducto().getId());
                Producto productoTemporal = new Producto();
                if(productoOptional.isPresent()) {
                    productoTemporal = productoOptional.get();
                }
                productoTemporal.setExistencia(productoTemporal.getExistencia()+carritoProducto.getCantidad());
                productoRepository.save(productoTemporal);
                carritoProductoRepository.delete(carritoProducto);
            });
            carritoRepository.delete(carrito);
            return !carritoRepository.existsById(carrito.getId());
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }




}
