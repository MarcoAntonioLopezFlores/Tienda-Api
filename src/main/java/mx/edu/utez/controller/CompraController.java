package mx.edu.utez.controller;


import mx.edu.utez.dto.CompraDto;
import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.Compra;
import mx.edu.utez.service.CompraService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/app/compra/")
public class CompraController {

    @Autowired
    private CompraService compraService;
    @Autowired
	private ModelMapper modelMapper;
    
    @PostMapping(value = "registrar")
    public Compra registrar(@RequestBody CompraDto compra, HttpServletRequest httpServletRequest) throws ApiRequestException {
        return compraService.registrar(convertToEntity(compra), httpServletRequest);
    }
    
    @PutMapping("actualizar")
    public boolean actualizar(@RequestBody CompraDto compra, HttpServletRequest httpServletRequest) throws ApiRequestException{
        return compraService.actualizar(convertToEntity(compra), httpServletRequest);
    }

    @GetMapping(value = "listar")
    public List<Compra> listarCompras(HttpServletRequest httpServletRequest) throws ApiRequestException {
        return compraService.listarComprasUsuario(httpServletRequest);
    }

    @GetMapping(value = "listar/{id}")
    public Compra obtenerCompra(@PathVariable long id) throws ApiRequestException {
        return compraService.obtenerCompra(id);
    }

    @GetMapping(value = "administrador/listar")
    public List<Compra> listar() throws ApiRequestException {
        return compraService.listar();
    }
    
    private Compra convertToEntity(CompraDto compraDto){
		return modelMapper.map(compraDto, Compra.class);
	}
}
