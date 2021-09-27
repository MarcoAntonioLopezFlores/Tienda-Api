package mx.edu.utez.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.Bitacora;
import mx.edu.utez.model.Categoria;
import mx.edu.utez.repository.CategoriaRepository;
import mx.edu.utez.repository.UsuarioRepository;

@Service
@Transactional
public class CategoriaService {
	@Autowired
	private final CategoriaRepository categoriaRepository;

	@Autowired
	private final UsuarioRepository usuarioRepository;

	@Autowired
	private final BitacoraService bitacoraService;

	private static final String TABLA_CATEGORIA = "categoria";

	public CategoriaService(CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository,
			BitacoraService bitacoraService) {
		this.categoriaRepository = categoriaRepository;
		this.bitacoraService = bitacoraService;
		this.usuarioRepository = usuarioRepository;
	}

	public List<Categoria> obtenerCategoriasActivas() throws ApiRequestException{
		try{
			return categoriaRepository.findAllByEstado(true);
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}

	public List<Categoria> obtenerCategorias() throws ApiRequestException{
		try {
			return categoriaRepository.findAll();
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}

	}

	public Categoria obtenerCategoria(long idCategoria) throws ApiRequestException{
		try {
			Optional<Categoria> categoriaOptional = categoriaRepository.findById(idCategoria);
			return categoriaOptional.orElseGet(Categoria::new);
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public boolean registrarCategoria(Categoria categoria, HttpServletRequest httpServletRequest) throws ApiRequestException{
		try {
			Categoria exist = categoriaRepository.save(categoria);
			Bitacora bitacora = new Bitacora(TABLA_CATEGORIA, exist.toString(), "", "INSERT",
					httpServletRequest.getRemoteAddr(),
					usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
			bitacoraService.registrar(bitacora);
			return true;
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public boolean actualizarCategoria(Categoria categoria, HttpServletRequest httpServletRequest) throws ApiRequestException{
		try {
			if (categoriaRepository.existsById(categoria.getId())) {
				Optional<Categoria> categoriaOptional = categoriaRepository.findById(categoria.getId());
				Categoria categoriaAntigua = categoriaOptional.orElseGet(Categoria::new);
				Bitacora bitacora = new Bitacora(TABLA_CATEGORIA, categoria.toString(), categoriaAntigua.toString(), "UPDATE",
						httpServletRequest.getRemoteAddr(),
						usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
				categoriaAntigua.setDescripcion(categoria.getDescripcion());
				categoriaAntigua.setNombre(categoria.getNombre());
				categoriaAntigua.setEstado(categoria.isEstado());
				bitacoraService.registrar(bitacora);
				categoriaRepository.save(categoriaAntigua);
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public boolean deleteCategoria(Long idCategoria, HttpServletRequest httpServletRequest) throws ApiRequestException{
		try {
			if (categoriaRepository.existsById(idCategoria)) {
				Optional<Categoria> categoriaOptional = categoriaRepository.findById(idCategoria);
				Categoria categoria = categoriaOptional.orElseGet(Categoria::new);
				Categoria categoria1 = categoriaOptional.orElseGet(Categoria::new);
				categoria.setEstado(!categoria.isEstado());
				Bitacora bitacora = new Bitacora(TABLA_CATEGORIA, categoria.toString(), categoria1.toString(), "DELETE",
						httpServletRequest.getRemoteAddr(),
						usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
				bitacoraService.registrar(bitacora);
				categoriaRepository.save(categoria);
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}
}
