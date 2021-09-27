package mx.edu.utez.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import mx.edu.utez.dto.RolDto;
import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.service.RolService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import mx.edu.utez.model.Rol;

@RestController
@RequestMapping("/app/rol/")
public class RolController {
	
	@Autowired
	private RolService rolService;
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping(value="listar")
	public List<Rol> obtenerRoles() throws ApiRequestException {
		return rolService.obtenerRoles();
	}

	@GetMapping(value = "{id}")
	public Rol obtenerRol(@PathVariable long id) throws ApiRequestException {
		return rolService.obtener(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value="registrar")
	public boolean guardar(@Valid @RequestBody RolDto rol, HttpServletRequest httpServletRequest) throws ApiRequestException {
		
		return rolService.registrar(convertToEntity(rol),httpServletRequest);
		
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value="actualizar")
	public boolean actualizar(@Valid @RequestBody RolDto rol,HttpServletRequest httpServletRequest) throws ApiRequestException {
		return rolService.actualizar(convertToEntity(rol),httpServletRequest);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value="eliminar/{id}")
	public boolean eliminar(@PathVariable long id,HttpServletRequest httpServletRequest) throws ApiRequestException {
		return rolService.borrar(id,httpServletRequest);
	}
	
	private Rol convertToEntity(RolDto rolDto){
		return modelMapper.map(rolDto, Rol.class);
	}

}
