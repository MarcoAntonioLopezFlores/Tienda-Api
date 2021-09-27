package mx.edu.utez.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import mx.edu.utez.dto.MarcaDto;
import mx.edu.utez.exception.ApiRequestException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.edu.utez.model.Marca;
import mx.edu.utez.service.MarcaService;

@RestController
@RequestMapping("/app/marca/")
public class MarcaController {

	@Autowired
	private final MarcaService marcaService;
	@Autowired
	private ModelMapper modelMapper;

	public MarcaController(MarcaService marcaService) {
		this.marcaService = marcaService;
	}

	@GetMapping(value = "listar")
	public List<Marca> obtenerMarcas() throws ApiRequestException {
		return marcaService.obtenerMarcas();
	}

	@GetMapping(value = "listarDisponibles")
	public List<Marca> obtenerMarcasDisponibles() throws ApiRequestException {
		return marcaService.obtenerMarcasActivas();
	}

	@GetMapping(value = "{id}")
	public Marca obtenerMarca(@PathVariable("id") long id) throws ApiRequestException{
		return marcaService.obtenerMarca(id);
	}

	@PostMapping(value = "registrar")
	public boolean guardar(@Valid @RequestBody MarcaDto marca, HttpServletRequest httpServletRequest) throws ApiRequestException{
		return marcaService.registrarMarca(convertToEntity(marca), httpServletRequest);
	}

	@PutMapping(value = "actualizar")
	public boolean actualizar(@Valid @RequestBody MarcaDto marca, HttpServletRequest httpServletRequest) throws ApiRequestException{
		
		return marcaService.actualizarMarca(convertToEntity(marca), httpServletRequest);
	}

	@DeleteMapping(value = "eliminar/{id}")
	public boolean aliminar(@PathVariable long id, HttpServletRequest httpServletRequest) throws ApiRequestException{
		return marcaService.eliminarMarca(id, httpServletRequest);

	}

	private Marca convertToEntity(MarcaDto marcaDto){
		return modelMapper.map(marcaDto, Marca.class);
	}
}
