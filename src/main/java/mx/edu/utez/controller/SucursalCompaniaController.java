package mx.edu.utez.controller;

import mx.edu.utez.dto.SucursalCompaniaDto;
import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.SucursalCompania;
import mx.edu.utez.service.SucursalCompaniaService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/app/sucursalcompania/")
public class SucursalCompaniaController {

	@Autowired
	private SucursalCompaniaService sucursalCompaniaService;
	@Autowired
	private ModelMapper modelMapper;

	@GetMapping(value = "{id}")
	public List<SucursalCompania> obtenerSucursalCompania(@PathVariable("id") long id) throws ApiRequestException {
		return sucursalCompaniaService.obtenerSucursalesByCompania(id);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "registrar")
	public boolean guardarDireccionSucursal(@Valid @RequestBody SucursalCompaniaDto sucursalCompania,
			HttpServletRequest httpServletRequest) throws ApiRequestException {
		return sucursalCompaniaService.registrar(convertToEntity(sucursalCompania), httpServletRequest);

	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "eliminar/{id}")
	public boolean eliminar(@PathVariable long id, HttpServletRequest httpServletRequest) throws ApiRequestException {
		return sucursalCompaniaService.borrar(id, httpServletRequest);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "actualizar")
	public boolean actualizar(@Valid @RequestBody SucursalCompaniaDto sucursalCompania, HttpServletRequest httpServletRequest)
			throws ApiRequestException {
		return sucursalCompaniaService.actualizar(convertToEntity(sucursalCompania), httpServletRequest);
	}
	
	private SucursalCompania convertToEntity(SucursalCompaniaDto sucursalCompaniaDto){
		return modelMapper.map(sucursalCompaniaDto, SucursalCompania.class);
	}
}