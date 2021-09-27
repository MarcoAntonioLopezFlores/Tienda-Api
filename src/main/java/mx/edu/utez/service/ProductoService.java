package mx.edu.utez.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.Bitacora;
import mx.edu.utez.model.Marca;
import mx.edu.utez.model.Producto;
import mx.edu.utez.model.Subcategoria;
import mx.edu.utez.repository.MarcaRepository;
import mx.edu.utez.repository.ProductoRepository;
import mx.edu.utez.repository.SubcategoriaRepository;
import mx.edu.utez.repository.UsuarioRepository;

@Service
@Transactional
public class ProductoService {

	@Autowired
	private final ProductoRepository productoRepository;
	
	@Autowired
	private final BitacoraService bitacoraService;

	@Autowired
	private final UsuarioRepository usuarioRepository;
		
	@Autowired
	private final MarcaRepository marcaRepository;
	
	@Autowired 
	private final SubcategoriaRepository subcategoriaRepository;

	private static final String TABLA_PRODUCTO = "producto";

	public ProductoService(ProductoRepository productoRepository , UsuarioRepository usuarioRepository, 
			BitacoraService bitacoraService, MarcaRepository marcaRepository
		, SubcategoriaRepository subcategoriaRepository) {
		this.productoRepository = productoRepository;
		this.bitacoraService = bitacoraService;
		this.usuarioRepository = usuarioRepository;
		this.subcategoriaRepository = subcategoriaRepository;
		this.marcaRepository = marcaRepository;
	}

	public List<Producto> obtenerProductos() throws ApiRequestException{
		try {
			return productoRepository.findAll();
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public List<Producto> obtenerProductosDisponibles() throws ApiRequestException{
		try {
			return productoRepository.findAllByEstadoIsTrue();
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public Producto obtenerProducto(Long idProducto) throws ApiRequestException{
		try {
			Optional<Producto> productoOptional = productoRepository.findById(idProducto);
			return productoOptional.orElseGet(Producto::new);
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public boolean registrarProducto(Producto producto, HttpServletRequest httpServletRequest) throws ApiRequestException{
		try {
			Producto exist = productoRepository.save(producto);
			Bitacora bitacora = new Bitacora(TABLA_PRODUCTO, exist.toString(), "", "INSERT",
					httpServletRequest.getRemoteAddr(),
					usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
			bitacoraService.registrar(bitacora);
			return true;
		}catch(DataIntegrityViolationException e) {
			return false;
		}catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public boolean actualizarProducto(Producto producto,  HttpServletRequest httpServletRequest) throws ApiRequestException{
		try {
			if (productoRepository.existsById(producto.getId())) {
				Optional<Producto> productoOptional = productoRepository.findById(producto.getId());
				Producto productoAntiguo = productoOptional.orElseGet(Producto::new);
				Optional<Marca> marcaOptional = marcaRepository.findById(producto.getMarca().getId());
				Optional<Subcategoria> subcategoriaOptional = subcategoriaRepository.findById(producto.getSubcategoria().getId());
				Bitacora bitacora = new Bitacora(TABLA_PRODUCTO, producto.toString(), productoAntiguo.toString(), "UPDATE",
						httpServletRequest.getRemoteAddr(),
						usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
				productoAntiguo.setColor(producto.getColor());
				productoAntiguo.setDescripcion(producto.getDescripcion());
				productoAntiguo.setEstado(producto.isEstado());
				productoAntiguo.setExistencia(producto.getExistencia());
				productoAntiguo.setImagen(producto.getImagen());
				productoAntiguo.setMarca(marcaOptional.orElseGet(Marca::new));
				productoAntiguo.setModelo(producto.getModelo());
				productoAntiguo.setNombre(producto.getNombre());
				productoAntiguo.setPrecio(producto.getPrecio());
				productoAntiguo.setSubcategoria(subcategoriaOptional.orElseGet(Subcategoria::new));
				bitacoraService.registrar(bitacora);
				productoRepository.save(productoAntiguo);
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public boolean borrarProducto(Long idProducto, HttpServletRequest httpServletRequest) throws ApiRequestException{
		try {
			Optional<Producto> productoOptional = productoRepository.findById(idProducto);
			Producto producto = productoOptional.orElseGet(Producto::new);
			producto.setEstado(!producto.isEstado());
			Bitacora bitacora = new Bitacora(TABLA_PRODUCTO, "Sin información nueva", productoRepository.save(producto).toString(),
					"DELETE", httpServletRequest.getRemoteAddr(),
					usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
			bitacoraService.registrar(bitacora);
			return true;
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public List<Producto> findBySubcategoria(Subcategoria subcategoria) throws ApiRequestException{
		try {
			return productoRepository.findBySubcategoriaAndEstadoIsTrue(subcategoria);
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public List<Producto> findByMarca(Marca marca) throws ApiRequestException{
		try {
			return productoRepository.findByMarcaAndEstadoIsTrue(marca);
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public List<Producto> findByPrecioMayor() throws ApiRequestException{
		try {
			return productoRepository.findAllByEstadoIsTrueOrderByPrecioDesc();
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public List<Producto> findByPrecioMenor() throws ApiRequestException{
		try {
			return productoRepository.findAllByEstadoIsTrueOrderByPrecioAsc();
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public List<Producto> buscarNuevos() throws ApiRequestException{
		try{
			return productoRepository.findTop10ByOrderByFechaRegistroDesc();
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}
}

