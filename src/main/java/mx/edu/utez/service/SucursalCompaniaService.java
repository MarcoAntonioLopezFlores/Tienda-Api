package mx.edu.utez.service;

import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.Bitacora;
import mx.edu.utez.model.CompaniaEnvio;
import mx.edu.utez.model.Direccion;
import mx.edu.utez.model.SucursalCompania;
import mx.edu.utez.model.Usuario;
import mx.edu.utez.repository.CompaniaEnvioRepository;
import mx.edu.utez.repository.DireccionRepository;
import mx.edu.utez.repository.SucursalCompaniaRepository;
import mx.edu.utez.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SucursalCompaniaService {

	@Autowired
	private SucursalCompaniaRepository sucursalCompaniaRepository;

	@Autowired
	private BitacoraService bitacoraService;

	@Autowired
	private CompaniaEnvioRepository companiaEnvioRepository;

	@Autowired
	private DireccionRepository direccionRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	private static final String INSERT = "INSERT";
	private static final String TABLA_SUCURSAL_COMPANIA = "sucursal_compania";

	public List<SucursalCompania> obtenerSucursalesByCompania(long id) throws ApiRequestException {
		try {
			Optional<CompaniaEnvio> companiaEnvioOptional = companiaEnvioRepository.findById(id);
			return sucursalCompaniaRepository.findAllByCompaniaEnvio(companiaEnvioOptional.orElseGet(CompaniaEnvio::new));
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public boolean registrar(SucursalCompania sucursalCompania, HttpServletRequest httpServletRequest)
			throws ApiRequestException {
		try {
			Optional<CompaniaEnvio> companiaEnvioOptional = companiaEnvioRepository.findById(sucursalCompania.getCompaniaEnvio().getId());
			Usuario usuario = usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser());
			CompaniaEnvio companiaEnvioTemporal = companiaEnvioOptional.orElseGet(CompaniaEnvio::new);
			Direccion direccionTemporal = direccionRepository.findByAsentamientoAndCalleAndCodigoPostalAndEntidadFederativaAndMunicipioAndNumeroExteriorAndTipoAsentamiento(
							sucursalCompania.getDireccion().getAsentamiento(),
							sucursalCompania.getDireccion().getCalle(),
							sucursalCompania.getDireccion().getCodigoPostal(),
							sucursalCompania.getDireccion().getEntidadFederativa(),
							sucursalCompania.getDireccion().getMunicipio(),
							sucursalCompania.getDireccion().getNumeroExterior(),
							sucursalCompania.getDireccion().getTipoAsentamiento());
			if (sucursalCompaniaRepository.findByCompaniaEnvioAndDireccion(companiaEnvioTemporal, direccionTemporal) == null) {

				if (direccionTemporal == null) {

					direccionTemporal = direccionRepository.save(sucursalCompania.getDireccion());
					Bitacora bitacoraDireccion = new Bitacora("direccion", direccionTemporal.toString(), "", INSERT,
							httpServletRequest.getRemoteAddr(), usuario);

					bitacoraService.registrar(bitacoraDireccion);
					sucursalCompania = sucursalCompaniaRepository
							.save(new SucursalCompania(sucursalCompania.getCompaniaEnvio(), direccionTemporal));
					Bitacora bitacoraDireccionSucursal = new Bitacora(TABLA_SUCURSAL_COMPANIA, sucursalCompania.toString(),
							"", INSERT, httpServletRequest.getRemoteAddr(), usuario);
					bitacoraService.registrar(bitacoraDireccionSucursal);
					return bitacoraService.verificarRegistro(bitacoraDireccion.getId())
							&& bitacoraService.verificarRegistro(bitacoraDireccionSucursal.getId())
							&& direccionRepository.existsById(direccionTemporal.getId())
							&& sucursalCompaniaRepository.existsById(sucursalCompania.getId());
				} else {
					sucursalCompania = sucursalCompaniaRepository.save(new SucursalCompania(companiaEnvioTemporal, direccionTemporal));

					Bitacora bitacoraDireccionUsuario = new Bitacora(TABLA_SUCURSAL_COMPANIA, sucursalCompania.toString(),
							"", INSERT, httpServletRequest.getRemoteAddr(), usuario);
					bitacoraService.registrar(bitacoraDireccionUsuario);

					return bitacoraService.verificarRegistro(bitacoraDireccionUsuario.getId())
							&& sucursalCompaniaRepository.existsById(sucursalCompania.getId());
				}
			}
			return false;
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public boolean borrar(long id, HttpServletRequest httpServletRequest) throws ApiRequestException {
		try {
			Optional<SucursalCompania> sucursalCompaniaOptional = sucursalCompaniaRepository.findById(id);
			Usuario usuario = usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser());
			SucursalCompania sucursalCompania = sucursalCompaniaOptional.orElseGet(SucursalCompania::new);
			Bitacora bitacoraSucursalUsuario = new Bitacora(TABLA_SUCURSAL_COMPANIA, "Sin información nueva",
					sucursalCompania.toString(), "DELETE", httpServletRequest.getRemoteAddr(), usuario);
			bitacoraService.registrar(bitacoraSucursalUsuario);
			sucursalCompaniaRepository.delete(sucursalCompania);
			return !sucursalCompaniaRepository.existsById(sucursalCompania.getId())
					&& bitacoraService.verificarRegistro(bitacoraSucursalUsuario.getId());
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}

	public boolean actualizar(SucursalCompania sucursalCompania, HttpServletRequest httpServletRequest)
			throws ApiRequestException {
		try {
			Optional<SucursalCompania> sucursalCompaniaOptional = sucursalCompaniaRepository.findById(sucursalCompania.getId());
			Optional<CompaniaEnvio> companiaEnvioOptional = companiaEnvioRepository.findById(sucursalCompania.getCompaniaEnvio().getId());
			Usuario usuario = usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser());
			SucursalCompania sucursalCompaniaTemporal = sucursalCompaniaOptional.orElseGet(SucursalCompania::new);
			Direccion direccionTemporal = direccionRepository
					.findByAsentamientoAndCalleAndCodigoPostalAndEntidadFederativaAndMunicipioAndNumeroExteriorAndTipoAsentamiento(
							sucursalCompania.getDireccion().getAsentamiento(),
							sucursalCompania.getDireccion().getCalle(),
							sucursalCompania.getDireccion().getCodigoPostal(),
							sucursalCompania.getDireccion().getEntidadFederativa(),
							sucursalCompania.getDireccion().getMunicipio(),
							sucursalCompania.getDireccion().getNumeroExterior(),
							sucursalCompania.getDireccion().getTipoAsentamiento());
			if (direccionTemporal == null) {
				direccionTemporal = direccionRepository.save(sucursalCompania.getDireccion());
				Bitacora bitacoraDireccion = new Bitacora("direccion", direccionTemporal.toString(), "", INSERT,
						httpServletRequest.getRemoteAddr(), usuario);
				bitacoraService.registrar(bitacoraDireccion);
				sucursalCompaniaTemporal.setDireccion(direccionTemporal);
				sucursalCompaniaTemporal.setCompaniaEnvio(companiaEnvioOptional.orElseGet(CompaniaEnvio::new));
				sucursalCompaniaTemporal = sucursalCompaniaRepository.save(sucursalCompaniaTemporal);
				Bitacora bitacoraDireccionSucursal = new Bitacora(TABLA_SUCURSAL_COMPANIA, sucursalCompaniaTemporal.toString(),
						sucursalCompania.toString(), "UPDATE", httpServletRequest.getRemoteAddr(), usuario);
				bitacoraService.registrar(bitacoraDireccionSucursal);
				
				return bitacoraService.verificarRegistro(bitacoraDireccion.getId())
						&& bitacoraService.verificarRegistro(bitacoraDireccionSucursal.getId())
						&& direccionRepository.existsById(direccionTemporal.getId())
						&& sucursalCompaniaRepository.existsById(sucursalCompania.getId());
			} else {
				sucursalCompania.setDireccion(direccionTemporal);
				Bitacora bitacoraDireccionUsuario = new Bitacora(TABLA_SUCURSAL_COMPANIA, sucursalCompania.toString(),
						sucursalCompaniaTemporal.toString(), "UPDATE", httpServletRequest.getRemoteAddr(), usuario);
				bitacoraService.registrar(bitacoraDireccionUsuario);
				Optional<Direccion> direccionOptional = direccionRepository.findById(direccionTemporal.getId());
				companiaEnvioOptional = companiaEnvioRepository.findById(sucursalCompania.getCompaniaEnvio().getId());
				sucursalCompaniaTemporal.setDireccion(direccionOptional.orElseGet(Direccion::new));
				sucursalCompaniaTemporal.setCompaniaEnvio(companiaEnvioOptional.orElseGet(CompaniaEnvio::new));
				sucursalCompaniaRepository.save(sucursalCompaniaTemporal);
				return bitacoraService.verificarRegistro(bitacoraDireccionUsuario.getId())
						&& sucursalCompaniaRepository.existsById(sucursalCompania.getId());
			}
		} catch (Exception e) {
			throw new ApiRequestException(e);
		}
	}
}
