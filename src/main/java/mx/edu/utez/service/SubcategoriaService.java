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
import mx.edu.utez.model.Subcategoria;
import mx.edu.utez.repository.CategoriaRepository;
import mx.edu.utez.repository.SubcategoriaRepository;
import mx.edu.utez.repository.UsuarioRepository;

@Service
@Transactional
public class SubcategoriaService {

	@Autowired
	private final SubcategoriaRepository subcategoriaRepository;

	@Autowired
	private final CategoriaRepository categoriaRepository;

	@Autowired
	private final BitacoraService bitacoraService;

	@Autowired
	private final UsuarioRepository usuarioRepository;

	private static final String TABLA_SUBCATEGORIA = "subcategoria";

	public SubcategoriaService(SubcategoriaRepository subcategoriaRepository, CategoriaRepository categoriaRepository,
			UsuarioRepository usuarioRepository, BitacoraService bitacoraService) throws ApiRequestException {
		try {
			this.subcategoriaRepository = subcategoriaRepository;
			this.categoriaRepository = categoriaRepository;
			this.bitacoraService = bitacoraService;
			this.usuarioRepository = usuarioRepository;
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}

	}

	public List<Subcategoria> obtenerSubcategoriasActivas() throws ApiRequestException{
		try{
			return subcategoriaRepository.findAllByEstado(true);
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}

	public List<Subcategoria> obtenerSubcategorias() throws ApiRequestException{
		try {
			return subcategoriaRepository.findAll();
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}

	}

	public List<Subcategoria> obtenerSubcategoriasByCategoria(long id) throws ApiRequestException{
		try {
			Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
			Categoria categoria = categoriaOptional.orElseGet(Categoria::new);
			return subcategoriaRepository.findByCategoria(categoria);
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}

	}

	public Subcategoria obtenerSubcategoria(Long idSubcategoria) throws ApiRequestException{
		try {
			Optional<Subcategoria> subcategoriaOptional = subcategoriaRepository.findById(idSubcategoria);
			return subcategoriaOptional.orElseGet(Subcategoria::new);
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public boolean registarSubcategoria(Subcategoria subcategoria, HttpServletRequest httpServletRequest) throws ApiRequestException{
		try {
			Optional<Categoria> categoriaOptional = categoriaRepository.findById(subcategoria.getCategoria().getId());
			subcategoria.setCategoria(categoriaOptional.orElseGet(Categoria::new));
			Subcategoria exist = subcategoriaRepository.save(subcategoria);
			Bitacora bitacora = new Bitacora(TABLA_SUBCATEGORIA, exist.toString(), "", "INSERT",
					httpServletRequest.getRemoteAddr(),
					usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
			bitacoraService.registrar(bitacora);
			return true;
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}

	}

	public boolean actualizarSubCategoria(Subcategoria subcategoria, HttpServletRequest httpServletRequest) throws ApiRequestException{
		try {
			if (subcategoriaRepository.existsById(subcategoria.getId())) {
				Optional<Subcategoria> subcategoriaOptional = subcategoriaRepository.findById(subcategoria.getId());
				Optional<Categoria> categoriaOptional = categoriaRepository.findById(subcategoria.getCategoria().getId());
				Subcategoria subcategoriaAntigua = subcategoriaOptional.orElseGet(Subcategoria::new);
				Bitacora bitacora = new Bitacora(TABLA_SUBCATEGORIA, subcategoria.toString(),
						subcategoriaAntigua.toString(), "UPDATE", httpServletRequest.getRemoteAddr(),
						usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
				subcategoriaAntigua.setNombre(subcategoria.getNombre());
				subcategoriaAntigua.setDescripcion(subcategoria.getDescripcion());
				subcategoriaAntigua.setImagen(subcategoria.getImagen());
				subcategoriaAntigua.setCategoria(categoriaOptional.orElseGet(Categoria::new));
				subcategoriaAntigua.setEstado(subcategoria.isEstado());
				bitacoraService.registrar(bitacora);
				subcategoriaRepository.save(subcategoriaAntigua);
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public boolean borrarSubcategoria(Long idSubcategoria, HttpServletRequest httpServletRequest) throws ApiRequestException{
		try {
			Optional<Subcategoria> subcategoriaOptional = subcategoriaRepository.findById(idSubcategoria);
			Subcategoria subcategoria = subcategoriaOptional.orElseGet(Subcategoria::new);
			subcategoria.setEstado(!subcategoria.isEstado());
			Bitacora bitacora = new Bitacora(TABLA_SUBCATEGORIA, "Sin información nueva", subcategoriaRepository.save(subcategoria).toString(),
					"DELETE", httpServletRequest.getRemoteAddr(),
					usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
			bitacoraService.registrar(bitacora);
			return true;
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}
}
