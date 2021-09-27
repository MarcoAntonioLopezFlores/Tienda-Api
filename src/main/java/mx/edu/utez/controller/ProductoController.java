package mx.edu.utez.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import mx.edu.utez.dto.ProductoDto;
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

import mx.edu.utez.model.Producto;
import mx.edu.utez.service.MarcaService;
import mx.edu.utez.service.ProductoService;
import mx.edu.utez.service.SubcategoriaService;

@RestController
@RequestMapping("/app/producto/")
public class ProductoController {

	@Autowired
	private final ProductoService productoService;

	@Autowired
	private final MarcaService marcaService;

	@Autowired
	private final SubcategoriaService subcategoriaService;
	@Autowired
	private ModelMapper modelMapper;

	public ProductoController(ProductoService productoService, MarcaService marcaService,
			SubcategoriaService subcategoriaService) {
		this.productoService = productoService;
		this.marcaService = marcaService;
		this.subcategoriaService = subcategoriaService;
	}

	@GetMapping(value = "listar")
	public List<Producto> obtenerProductos() throws ApiRequestException {
		return productoService.obtenerProductos();
	}

	@GetMapping(value = "listarNuevos")
	public List<Producto> listarNuevos() throws ApiRequestException {
		return productoService.buscarNuevos();
	}

	@GetMapping(value = "listarDisponibles")
	public List<Producto> listarDisponibles() throws ApiRequestException {
		return productoService.obtenerProductosDisponibles();
	}

	@GetMapping(value = "listarPrecioAsc")
	public List<Producto> listarPrecioAsc() throws ApiRequestException {
		return productoService.findByPrecioMenor();
	}

	@GetMapping(value = "listarPrecioDesc")
	public List<Producto> listarPrecioDesc() throws ApiRequestException {
		return productoService.findByPrecioMayor();
	}

	@GetMapping(value = "marca/{id}")
	public List<Producto> obtenerProductosMarca(@PathVariable("id") long id) throws ApiRequestException{
		return productoService.findByMarca(marcaService.obtenerMarca(id));
	}

	@GetMapping(value = "subcategoria/{id}")
	public List<Producto> obtenerProductosSubcategoria(@PathVariable("id") long id) throws ApiRequestException{
		return productoService.findBySubcategoria(subcategoriaService.obtenerSubcategoria(id));
	}

	@GetMapping(value = "{id}")
	public Producto obtenerProducto(@PathVariable("id") long id) throws ApiRequestException{
		return productoService.obtenerProducto(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "registrar")
	public boolean registar(@Valid @RequestBody ProductoDto producto, HttpServletRequest httpServletRequest) throws ApiRequestException{
		return productoService.registrarProducto(convertToEntity(producto), httpServletRequest);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "actualizar")
	public boolean guardar(@Valid @RequestBody ProductoDto producto, HttpServletRequest httpServletRequest) throws ApiRequestException{
		return productoService.actualizarProducto(convertToEntity(producto), httpServletRequest);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "eliminar/{id}")
	public boolean eliminar(@PathVariable long id, HttpServletRequest httpServletRequest) throws ApiRequestException{
		return productoService.borrarProducto(id, httpServletRequest);
	}
	
	private Producto convertToEntity(ProductoDto productoDto){
		return modelMapper.map(productoDto, Producto.class);
	}
}

