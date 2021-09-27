package mx.edu.utez.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import mx.edu.utez.dto.CompaniaDto;
import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.service.CompaniaEnvioService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import mx.edu.utez.model.CompaniaEnvio;

@RestController
@RequestMapping("/app/compania/")
public class CompaniaEnvioController {

	@Autowired
	private CompaniaEnvioService companiaEnvioService;
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping(value="listar")
	public List<CompaniaEnvio> obtenerCompanias() throws ApiRequestException {
		return companiaEnvioService.obtenerCompanias();
	}

	@GetMapping("listarDisponibles")
	public List<CompaniaEnvio> obtenerCompaniasDisponibles() throws ApiRequestException {
		return companiaEnvioService.obtenerDisponibles();
	}

	@GetMapping(value="{id}")
	public CompaniaEnvio obtenerCompania(@PathVariable long id){
		return companiaEnvioService.obtener(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value="registrar")
	public boolean guardar(@Valid @RequestBody CompaniaDto compania,HttpServletRequest httpServletRequest) throws ApiRequestException {
		return companiaEnvioService.registrar(convertToEntity(compania),httpServletRequest);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value="actualizar")
	public boolean actualizar(@Valid @RequestBody CompaniaDto compania, HttpServletRequest httpServletRequest) throws ApiRequestException {
		return companiaEnvioService.actualizar(convertToEntity(compania),httpServletRequest);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value="eliminar/{id}")
	public boolean eliminar(@PathVariable long id,HttpServletRequest httpServletRequest) throws ApiRequestException {
		return companiaEnvioService.borrar(id,httpServletRequest);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "bajalogica/{id}")
	public boolean bajaLogica(@PathVariable long id,HttpServletRequest httpServletRequest) throws ApiRequestException {
		return companiaEnvioService.bajaLogica(id,httpServletRequest);
	}
	
	private CompaniaEnvio convertToEntity(CompaniaDto companiaDto){
		return modelMapper.map(companiaDto, CompaniaEnvio.class);
	}
}
