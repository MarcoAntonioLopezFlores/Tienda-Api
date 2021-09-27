package mx.edu.utez.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.Bitacora;
import mx.edu.utez.model.Direccion;
import mx.edu.utez.model.DireccionUsuario;
import mx.edu.utez.model.Usuario;
import mx.edu.utez.repository.DireccionRepository;
import mx.edu.utez.repository.DireccionUsuarioRepository;
import mx.edu.utez.repository.UsuarioRepository;

@Service
@Transactional
public class DireccionUsuarioService {
	@Autowired
	private final DireccionUsuarioRepository direccionUsuarioRepository;

	@Autowired
	private final BitacoraService bitacoraService;

	@Autowired
	private final UsuarioRepository usuarioRepository;

	@Autowired
	private final DireccionRepository direccionRepository;

	private static final String TABLA_DIRECCION_USUARIO = "direccion_usuario";
	private static final String INSERTAR = "INSERT";

	public DireccionUsuarioService(DireccionUsuarioRepository direccionUsuarioRepository,
			UsuarioRepository usuarioRepository, BitacoraService bitacoraService,
			DireccionRepository direccionRepository) {
		this.direccionUsuarioRepository = direccionUsuarioRepository;
		this.bitacoraService = bitacoraService;
		this.usuarioRepository = usuarioRepository;
		this.direccionRepository = direccionRepository;
	}

	public List<DireccionUsuario> obtenerDireccionesByUsuario(HttpServletRequest httpServletRequest)
			throws ApiRequestException {
		try {
			return direccionUsuarioRepository
					.findAllByUsuario(usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public boolean borrar(long id, HttpServletRequest httpServletRequest) throws ApiRequestException {
		try {
			Usuario usuario = usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser());
			Optional<DireccionUsuario> direccionOptional = direccionUsuarioRepository.findById(id);
			DireccionUsuario direccionUsuario = direccionOptional.orElseGet(DireccionUsuario::new);
			Bitacora bitacoraDireccionUsuario = new Bitacora(TABLA_DIRECCION_USUARIO,"Sin información nueva", direccionUsuario.toString(), "DELETE", httpServletRequest.getRemoteAddr(), usuario);
			bitacoraService.registrar(bitacoraDireccionUsuario);
			direccionUsuarioRepository.delete(direccionUsuario);
			return !direccionUsuarioRepository.existsById(direccionUsuario.getId()) && bitacoraService.verificarRegistro(bitacoraDireccionUsuario.getId());
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}
	public boolean registrar(DireccionUsuario direccionUsuario, HttpServletRequest httpServletRequest) throws ApiRequestException {
		try {

			Usuario usuario = usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser());
			Direccion direccionTemporal =direccionRepository.findByAsentamientoAndCalleAndCodigoPostalAndEntidadFederativaAndMunicipioAndNumeroExteriorAndTipoAsentamiento(
					direccionUsuario.getDireccion().getAsentamiento(), direccionUsuario.getDireccion().getCalle(), direccionUsuario.getDireccion().getCodigoPostal(),
					direccionUsuario.getDireccion().getEntidadFederativa(), direccionUsuario.getDireccion().getMunicipio(), direccionUsuario.getDireccion().getNumeroExterior(),
					direccionUsuario.getDireccion().getTipoAsentamiento());
			if(direccionUsuarioRepository.findByDireccionAndUsuario(direccionTemporal, usuario)==null){
				if(direccionTemporal==null){
					direccionTemporal = direccionRepository.save(direccionUsuario.getDireccion());
					Bitacora bitacoraDireccion = new Bitacora("direccion",direccionTemporal.toString(), "", INSERTAR, httpServletRequest.getRemoteAddr(), usuario);
					bitacoraService.registrar(bitacoraDireccion);
					direccionUsuario = direccionUsuarioRepository.save(new DireccionUsuario(direccionTemporal, usuario));
					Bitacora bitacoraDireccionUsuario = new Bitacora(TABLA_DIRECCION_USUARIO,direccionUsuario.toString(), "", INSERTAR, httpServletRequest.getRemoteAddr(), usuario);
					bitacoraService.registrar(bitacoraDireccionUsuario);
					return  bitacoraService.verificarRegistro(bitacoraDireccion.getId()) && bitacoraService.verificarRegistro(bitacoraDireccionUsuario.getId())
							&& direccionRepository.existsById(direccionTemporal.getId()) && direccionUsuarioRepository.existsById(direccionUsuario.getId());
				}else{
					direccionUsuario = direccionUsuarioRepository.save(new DireccionUsuario(direccionTemporal, usuario));
					Bitacora bitacoraDireccionUsuario = new Bitacora(TABLA_DIRECCION_USUARIO,direccionUsuario.toString(), "", INSERTAR, httpServletRequest.getRemoteAddr(), usuario);
					bitacoraService.registrar(bitacoraDireccionUsuario);
					return bitacoraService.verificarRegistro(bitacoraDireccionUsuario.getId()) && direccionUsuarioRepository.existsById(direccionUsuario.getId());
				}
			}
			return false;

		} catch (Exception e) {
			throw new ApiRequestException(e);
		}

	}

	public boolean actualizar(DireccionUsuario direccionUsuario, HttpServletRequest httpServletRequest) throws ApiRequestException {
		try {
			Usuario usuario = usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser());
			Optional<DireccionUsuario> direccionOptional = direccionUsuarioRepository.findById(direccionUsuario.getId());
			DireccionUsuario direccionUsuarioTemporal = direccionOptional.orElseGet(DireccionUsuario::new);
			DireccionUsuario direccionUsuarioNueva= direccionUsuarioTemporal;
			Direccion direccionTemporal =direccionRepository.findByAsentamientoAndCalleAndCodigoPostalAndEntidadFederativaAndMunicipioAndNumeroExteriorAndTipoAsentamiento(
					direccionUsuario.getDireccion().getAsentamiento(), direccionUsuario.getDireccion().getCalle(), direccionUsuario.getDireccion().getCodigoPostal(),
					direccionUsuario.getDireccion().getEntidadFederativa(), direccionUsuario.getDireccion().getMunicipio(), direccionUsuario.getDireccion().getNumeroExterior(),
					direccionUsuario.getDireccion().getTipoAsentamiento());

			if(direccionTemporal==null){

				direccionTemporal = direccionRepository.save(direccionUsuario.getDireccion());
				Bitacora bitacoraDireccion = new Bitacora("direccion",direccionTemporal.toString(), "", INSERTAR, httpServletRequest.getRemoteAddr(), usuario);
				bitacoraService.registrar(bitacoraDireccion);
				direccionUsuarioNueva.setDireccion(direccionTemporal);
				direccionUsuarioNueva = direccionUsuarioRepository.save(direccionUsuarioNueva);
				Bitacora bitacoraDireccionUsuario = new Bitacora(TABLA_DIRECCION_USUARIO,direccionUsuarioNueva.toString(), direccionUsuarioTemporal.toString(), "UPDATE", httpServletRequest.getRemoteAddr(), usuario);
				bitacoraService.registrar(bitacoraDireccionUsuario);

				return  bitacoraService.verificarRegistro(bitacoraDireccion.getId()) && bitacoraService.verificarRegistro(bitacoraDireccionUsuario.getId())
						&& direccionRepository.existsById(direccionTemporal.getId()) && direccionUsuarioRepository.existsById(direccionUsuario.getId());
			}else{
				direccionUsuarioTemporal.setDireccion(direccionTemporal);
				direccionUsuarioNueva = direccionUsuarioRepository.save(direccionUsuarioNueva);
				Bitacora bitacoraDireccionUsuario = new Bitacora(TABLA_DIRECCION_USUARIO,direccionUsuarioNueva.toString(), direccionUsuarioTemporal.toString(), "UPDATE", httpServletRequest.getRemoteAddr(), usuario);
				bitacoraService.registrar(bitacoraDireccionUsuario);
				return bitacoraService.verificarRegistro(bitacoraDireccionUsuario.getId()) && direccionUsuarioRepository.existsById(direccionUsuario.getId());
			}
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}




}
