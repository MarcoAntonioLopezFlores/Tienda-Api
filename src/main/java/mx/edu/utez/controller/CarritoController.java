package mx.edu.utez.controller;


import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.CarritoProducto;
import mx.edu.utez.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/app/carrito/")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping(value = "agregar/{id}")
    public boolean agregarProducto(@PathVariable long id, HttpServletRequest httpServletRequest) throws ApiRequestException {
        return carritoService.agregarProductoCarrito(id, httpServletRequest);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping(value = "eliminar/{id}")
    public boolean eliminarProducto(@PathVariable long id, HttpServletRequest httpServletRequest) throws ApiRequestException {
        return carritoService.eliminarProducto(id,  httpServletRequest);
    }

    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN')"  )
    @GetMapping(value = "listar")
    public List<CarritoProducto> listarCarrito(HttpServletRequest httpServletRequest) throws ApiRequestException {
        return carritoService.listarCarrito(httpServletRequest);
    }

    @GetMapping("/total")
    public int totalCarrito(HttpServletRequest httpServletRequest) throws ApiRequestException {
        return carritoService.totalCarrito(httpServletRequest);

    }
    
    @GetMapping(value = "compra/{id}")
    public  List<CarritoProducto> compraListarCarrito(@PathVariable long id) throws ApiRequestException {
        return carritoService.compraListarCarrito(id);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @DeleteMapping(value = "vaciar/{id}")
    public boolean limpiarCarrito(@PathVariable long id,HttpServletRequest httpServletRequest) throws ApiRequestException {
        return carritoService.limpiarCarrito(id, httpServletRequest);
    }
}
