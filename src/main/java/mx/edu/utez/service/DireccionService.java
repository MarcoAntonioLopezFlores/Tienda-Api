package mx.edu.utez.service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.Bitacora;
import mx.edu.utez.model.Usuario;
import mx.edu.utez.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.edu.utez.model.Direccion;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DireccionService {
	@Autowired
	private DireccionRepository direccionRepository;

	@Autowired
	private BitacoraService bitacoraService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	private static final String TABLA_DIRECCION = "direccion";

	public List<Direccion> obtenerDirecciones() throws ApiRequestException {
		try {
			return direccionRepository.findAll();
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}

	}



	public boolean registrar(Direccion direccion, HttpServletRequest httpServletRequest) throws ApiRequestException {
		try {
			if (direccionRepository
					.findByAsentamientoAndCalleAndCodigoPostalAndEntidadFederativaAndMunicipioAndNumeroExteriorAndTipoAsentamiento(
							direccion.getAsentamiento(), direccion.getCalle(), direccion.getCodigoPostal(),
							direccion.getEntidadFederativa(), direccion.getMunicipio(), direccion.getNumeroExterior(),
							direccion.getTipoAsentamiento()) == null) {
				Usuario usuario = usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser());
				Direccion direccionRegistrada = direccionRepository.save(direccion);
				Bitacora bitacoraDireccion = new Bitacora(TABLA_DIRECCION, direccionRegistrada.toString(), "", "INSERT",
						httpServletRequest.getRemoteAddr(), usuario);
				bitacoraService.registrar(bitacoraDireccion);
				return direccionRepository.existsById(direccionRegistrada.getId())
						&& bitacoraService.verificarRegistro(bitacoraDireccion.getId());
			}
			return false;
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}

	}

	public boolean actualizar(Direccion direccion, HttpServletRequest httpServletRequest) throws ApiRequestException {
		try {
			if (direccionRepository
					.findByAsentamientoAndCalleAndCodigoPostalAndEntidadFederativaAndMunicipioAndNumeroExteriorAndTipoAsentamiento(
							direccion.getAsentamiento(), direccion.getCalle(), direccion.getCodigoPostal(),
							direccion.getEntidadFederativa(), direccion.getMunicipio(), direccion.getNumeroExterior(),
							direccion.getTipoAsentamiento()) == null) {
				Bitacora bitacora;
				Optional<Direccion> direccionOptional = direccionRepository.findById(direccion.getId());
				Direccion direccionTemporal = direccionOptional.orElseGet(Direccion::new);
				bitacora = new Bitacora(TABLA_DIRECCION, direccion.toString(), direccionTemporal.toString(), "UPDATE",
						httpServletRequest.getRemoteAddr(),
						usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
				bitacoraService.registrar(bitacora);
				direccionTemporal.setAsentamiento(direccion.getAsentamiento());
				direccionTemporal.setCalle(direccion.getCalle());
				direccionTemporal.setCodigoPostal(direccion.getCodigoPostal());
				direccionTemporal.setEntidadFederativa(direccion.getEntidadFederativa());
				direccionTemporal.setMunicipio(direccion.getMunicipio());
				direccionTemporal.setTipoAsentamiento(direccion.getTipoAsentamiento());
				direccionTemporal.setNumeroExterior(direccion.getNumeroExterior());
				direccionTemporal=direccionRepository.save(direccionTemporal);

				return direccionRepository.existsById(direccionTemporal.getId())
						&& bitacoraService.verificarRegistro(bitacora.getId());
			}
			return false;
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}

	}

	public Direccion obtener(long id) throws ApiRequestException {
		try {
			Optional<Direccion> direccionOptional = direccionRepository.findById(id);
			return direccionOptional.orElseGet(Direccion::new);
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}

	}

	public boolean borrar(long id, HttpServletRequest httpServletRequest) throws ApiRequestException {
		try {
			Usuario usuario = usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser());
			Optional<Direccion> direccionOptional = direccionRepository.findById(id);
			Direccion direccion = direccionOptional.orElseGet(Direccion::new);
			Bitacora bitacora = new Bitacora(TABLA_DIRECCION, "Sin información nueva",
					direccion.toString(), "DELETE", httpServletRequest.getRemoteAddr(),
					usuario);
			bitacoraService.registrar(bitacora);
			direccionRepository.deleteById(id);
			return !direccionRepository.existsById(id) && bitacoraService.verificarRegistro(bitacora.getId());
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}

	}
}

