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
import mx.edu.utez.repository.MarcaRepository;
import mx.edu.utez.repository.UsuarioRepository;

@Service
@Transactional
public class MarcaService {

	@Autowired
	private final MarcaRepository marcaRepository;

	@Autowired
	private final BitacoraService bitacoraService;

	@Autowired
	private final UsuarioRepository usuarioRepository;

	public MarcaService(MarcaRepository marcaRepository, UsuarioRepository usuarioRepository,
			BitacoraService bitacoraService) {
		this.marcaRepository = marcaRepository;
		this.bitacoraService = bitacoraService;
		this.usuarioRepository = usuarioRepository;
	}

	public List<Marca> obtenerMarcasActivas() throws ApiRequestException{
		try{
			return marcaRepository.findAllByEstado(true);
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}

	public List<Marca> obtenerMarcas() throws ApiRequestException{
		try {
			return marcaRepository.findAll();
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public Marca obtenerMarca(Long idMarca) throws ApiRequestException{
		try {
			Optional<Marca> marcaOptional = marcaRepository.findById(idMarca);
			return marcaOptional.orElseGet(Marca::new);
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public boolean registrarMarca(Marca marca, HttpServletRequest httpServletRequest) throws ApiRequestException{
		try {
			Marca exist = marcaRepository.save(marca);
			Bitacora bitacora = new Bitacora("marca", exist.toString(), "", "INSERT",
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

	public boolean actualizarMarca(Marca marca, HttpServletRequest httpServletRequest) throws ApiRequestException{
		try {
			if (marcaRepository.existsById(marca.getId())) {
				Optional<Marca> marcaOptional = marcaRepository.findById(marca.getId());
				Marca marcaAntigua = marcaOptional.orElseGet(Marca::new);
				Bitacora bitacora = new Bitacora("marca", marca.toString(), marcaAntigua.toString(), "UPDATE",
						httpServletRequest.getRemoteAddr(),
						usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
				bitacoraService.registrar(bitacora);
				marcaAntigua.setDescripcion(marca.getDescripcion());
				marcaAntigua.setNombre(marca.getNombre());
				marcaAntigua.setEstado(marca.isEstado());
				marcaRepository.save(marcaAntigua);
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public boolean eliminarMarca(Long idMarca, HttpServletRequest httpServletRequest) throws ApiRequestException{
		try {
			Optional<Marca> marcaOptional = marcaRepository.findById(idMarca);
			Marca marca = marcaOptional.orElseGet(Marca::new);
			marca.setEstado(!marca.isEstado());
			Bitacora bitacora = new Bitacora("categoria", marca.toString(), marcaRepository.save(marca).toString(),
					"DELETE", httpServletRequest.getRemoteAddr(),
					usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
			bitacoraService.registrar(bitacora);
			return true;
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}
}
