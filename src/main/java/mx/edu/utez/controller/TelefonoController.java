package mx.edu.utez.controller;


import mx.edu.utez.dto.TelefonoDto;
import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.Telefono;
import mx.edu.utez.service.TelefonoService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("app/telefono/")
public class TelefonoController {

    @Autowired
    private TelefonoService telefonoService;
    @Autowired
	private ModelMapper modelMapper;

    @GetMapping("listar")
    public List<Telefono> listarTelefonos(HttpServletRequest httpServletRequest) throws ApiRequestException {

        return telefonoService.listarTelefonosUsuario(httpServletRequest);
    }

    @GetMapping("listar/{id}")
    public Telefono listarTelefono(@PathVariable long id) throws ApiRequestException {

        return telefonoService.telefonoUsuario(id);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping(value = "registrar")
    public boolean registrar(@Valid @RequestBody TelefonoDto telefono,HttpServletRequest httpServletRequest )throws ApiRequestException {
        return telefonoService.registrar(convertToEntity(telefono), httpServletRequest);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @PutMapping(value = "actualizar")
    public boolean actualizar(@Valid @RequestBody TelefonoDto telefono,HttpServletRequest httpServletRequest )throws ApiRequestException {
        return telefonoService.actualizar(convertToEntity(telefono), httpServletRequest);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @DeleteMapping("eliminar/{id}")
    public boolean eliminarTelefono(@PathVariable long id,HttpServletRequest httpServletRequest) throws ApiRequestException {

        return telefonoService.borrarTelefono(id, httpServletRequest);
    }
    
    private Telefono convertToEntity(TelefonoDto telefonoDto){
		return modelMapper.map(telefonoDto, Telefono.class);
	}

}
