package mx.edu.utez.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import mx.edu.utez.dto.SubcategoriaDto;
import mx.edu.utez.exception.ApiRequestException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.edu.utez.model.Subcategoria;
import mx.edu.utez.service.SubcategoriaService;

@RestController
@RequestMapping("/app/subcategoria/")
public class SubcategoriaController {
	@Autowired
	private final SubcategoriaService subcategoriaService;
	@Autowired
	private ModelMapper modelMapper;

	public SubcategoriaController(SubcategoriaService subcategoriaService) {
		this.subcategoriaService = subcategoriaService;
	}

	@GetMapping(value = "listar")
	public List<Subcategoria> obtenerSubcategorias() throws ApiRequestException {
		return subcategoriaService.obtenerSubcategorias();
	}

	@GetMapping("/listarDisponibles")
	public List<Subcategoria> obtenerSubcategoriasDisponibles() throws ApiRequestException {
		return subcategoriaService.obtenerSubcategoriasActivas();
	}

	@GetMapping(value = "{id}")
	public Subcategoria obtenerSubcategoria(@PathVariable("id") long id) throws ApiRequestException{
		return subcategoriaService.obtenerSubcategoria(id);
	}

	@GetMapping(value = "categoria/{id}")
	public List<Subcategoria> obtenerSubcategoriasCategoria(@PathVariable long id) throws ApiRequestException{
		return subcategoriaService.obtenerSubcategoriasByCategoria(id);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "registrar")
	public boolean registrar(@Valid @RequestBody SubcategoriaDto subcategoria, HttpServletRequest httpServletRequest) throws ApiRequestException{
		return subcategoriaService.registarSubcategoria(convertToEntity(subcategoria), httpServletRequest);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "actualizar")
	public boolean actualizar(@Valid @RequestBody SubcategoriaDto subcategoria,HttpServletRequest httpServletRequest) throws ApiRequestException{
		return subcategoriaService.actualizarSubCategoria(convertToEntity(subcategoria), httpServletRequest);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "eliminar/{id}")
	public boolean eliminar(@PathVariable long id, HttpServletRequest httpServletRequest) throws ApiRequestException{
		return subcategoriaService.borrarSubcategoria(id, httpServletRequest);
	}
	
	private Subcategoria convertToEntity(SubcategoriaDto subcategoriaDto){
		return modelMapper.map(subcategoriaDto, Subcategoria.class);
	}
}

