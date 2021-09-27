package mx.edu.utez.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import mx.edu.utez.dto.CategoriaDto;
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

import mx.edu.utez.model.Categoria;
import mx.edu.utez.service.CategoriaService;

@RestController
@RequestMapping("/app/categoria/")
public class CategoriaController {

	@Autowired
	private final CategoriaService categoriaService;
	@Autowired
	private ModelMapper modelMapper;
	public CategoriaController(CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
	}

	@GetMapping(value = "listar")
	public List<Categoria> obtenerCategorias() throws ApiRequestException {
		return categoriaService.obtenerCategorias();
	}

	@GetMapping(value = "listarDisponibles")
	public List<Categoria> obtenerCategoriasDisponibles() throws ApiRequestException {
		return categoriaService.obtenerCategoriasActivas();
	}

	@GetMapping(value = "{id}")
	public Categoria obtenerCategoria(@PathVariable("id") long id ) throws ApiRequestException{
		return categoriaService.obtenerCategoria(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "registrar")
	public boolean guardar(@Valid @RequestBody CategoriaDto categoria, HttpServletRequest httpServletRequest) throws ApiRequestException{
		return categoriaService.registrarCategoria(convertToEntity(categoria), httpServletRequest);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "actualizar")
	public boolean actualizar(@Valid @RequestBody CategoriaDto categoria, HttpServletRequest httpServletRequest) throws ApiRequestException{
		return categoriaService.actualizarCategoria(convertToEntity(categoria), httpServletRequest);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "eliminar/{id}")
	public boolean eliminar(@PathVariable long id, HttpServletRequest httpServletRequest) throws ApiRequestException{
		return categoriaService.deleteCategoria(id, httpServletRequest);
	}
	
	private Categoria convertToEntity(CategoriaDto categoriaDto) { 
		return modelMapper.map(categoriaDto, Categoria.class);
	}
}

