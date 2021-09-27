package mx.edu.utez.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import mx.edu.utez.dto.DireccionDto;
import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.service.DireccionService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import mx.edu.utez.model.Direccion;

@RestController
@RequestMapping("/app/direccion/")
public class DireccionController {
	
	@Autowired
	private DireccionService direccionService;
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping(value="listar")
	public List<Direccion> obtenerDirecciones() throws ApiRequestException {

		return direccionService.obtenerDirecciones();
	}

	@GetMapping(value="{id}")
	public Direccion obtenerDireccion(@PathVariable long id) throws ApiRequestException {

		return direccionService.obtener(id);
	}

	@PostMapping(value="registrar")
	public boolean guardar(@Valid @RequestBody DireccionDto direccion, HttpServletRequest httpServletRequest) throws ApiRequestException {
		return direccionService.registrar(convertToEntity(direccion),httpServletRequest);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value="actualizar")
	public boolean actualizar(@Valid @RequestBody DireccionDto direccion,HttpServletRequest httpServletRequest) throws ApiRequestException {
		return direccionService.actualizar(convertToEntity(direccion),httpServletRequest);
	}

	@DeleteMapping(value="eliminar/{id}")
	public boolean eliminar(@PathVariable long id,HttpServletRequest httpServletRequest) throws ApiRequestException {
		return direccionService.borrar(id,httpServletRequest);
	}
	
	private Direccion convertToEntity(DireccionDto direccionDto){
		return modelMapper.map(direccionDto, Direccion.class);
	}
}
