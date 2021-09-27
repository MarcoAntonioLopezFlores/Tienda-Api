package mx.edu.utez.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.Bitacora;
import mx.edu.utez.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.edu.utez.model.CompaniaEnvio;
import mx.edu.utez.repository.CompaniaEnvioRepository;

@Service
@Transactional

public class CompaniaEnvioService {

	@Autowired
	private CompaniaEnvioRepository companiaEnvioRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BitacoraService bitacoraService;

	private static final String TABLA_COMPANIA = "compania_envio";

	public List<CompaniaEnvio> obtenerDisponibles() throws ApiRequestException {
		try {
			return companiaEnvioRepository.findAllByEstadoIsTrue();
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}

	public List<CompaniaEnvio> obtenerCompanias() throws ApiRequestException {
		try{
			return companiaEnvioRepository.findAll();
		}catch (Exception e){
			throw new ApiRequestException(e);
		}

	}

	public boolean registrar(CompaniaEnvio compania, HttpServletRequest httpServletRequest) throws ApiRequestException {
		try {
			if(companiaEnvioRepository.findByRfcAndDescripcion(compania.getRfc(), compania.getDescripcion())==null){
				Bitacora bitacora;
				compania = companiaEnvioRepository.save(compania);
				bitacora = new Bitacora(
						TABLA_COMPANIA, compania.toString(), "", "INSERT", httpServletRequest.getRemoteAddr(),
						usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
				bitacoraService.registrar(bitacora);
				return companiaEnvioRepository.existsById(compania.getId()) && bitacoraService.verificarRegistro(bitacora.getId());
			}
			return false;
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}
	public boolean actualizar(CompaniaEnvio compania, HttpServletRequest httpServletRequest) throws ApiRequestException {
		try {

			if(companiaEnvioRepository.existsById(compania.getId())){
				Bitacora bitacora;
				Optional<CompaniaEnvio> companiaEnvioOptional = companiaEnvioRepository.findById(compania.getId());
				CompaniaEnvio companiaEnvioTemporal = companiaEnvioOptional.orElseGet(CompaniaEnvio::new);
				bitacora = new Bitacora(
						TABLA_COMPANIA, compania.toString(), companiaEnvioTemporal.toString(), "UPDATE", httpServletRequest.getRemoteAddr(),
						usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
				bitacoraService.registrar(bitacora);
				companiaEnvioTemporal.setDescripcion(compania.getDescripcion());
				companiaEnvioTemporal.setRfc(compania.getRfc());
				companiaEnvioTemporal.setEstado(compania.isEstado());
				companiaEnvioTemporal= companiaEnvioRepository.save(companiaEnvioTemporal);

				return companiaEnvioRepository.existsById(companiaEnvioTemporal.getId()) && bitacoraService.verificarRegistro(bitacora.getId());
			}
			return false;
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}
	
	public CompaniaEnvio obtener(long id) {
		Optional<CompaniaEnvio> companiaEnvioOptional = companiaEnvioRepository.findById(id);
		return companiaEnvioOptional.orElseGet(CompaniaEnvio::new);
	}
	
	public boolean borrar(long id,HttpServletRequest httpServletRequest) throws ApiRequestException {
		try{
			if(companiaEnvioRepository.existsById(id)){
				Optional<CompaniaEnvio> companiaEnvioOptional = companiaEnvioRepository.findById(id);
				Bitacora bitacora = new Bitacora(
						TABLA_COMPANIA, "Sin información nueva", companiaEnvioOptional.isPresent() ? companiaEnvioOptional.get().toString() : "",
						"DELETE", httpServletRequest.getRemoteAddr(), usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
				bitacoraService.registrar(bitacora);
				companiaEnvioRepository.deleteById(id);
				return !companiaEnvioRepository.existsById(id) && bitacoraService.verificarRegistro(bitacora.getId());
			}
			return false;
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}
	public boolean bajaLogica(long id,HttpServletRequest httpServletRequest) throws ApiRequestException {
		try{
			if(companiaEnvioRepository.existsById(id)){
				Optional<CompaniaEnvio> companiaEnvioOptional = companiaEnvioRepository.findById(id);
				CompaniaEnvio companiaEnvio = companiaEnvioOptional.orElseGet(CompaniaEnvio::new);
				companiaEnvio.setEstado(!companiaEnvio.isEstado());
				Bitacora bitacora = new Bitacora(
						TABLA_COMPANIA, companiaEnvio.toString(), companiaEnvioOptional.isPresent() ? companiaEnvioOptional.get().toString() : "", "DELETE", httpServletRequest.getRemoteAddr(),
						usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
				bitacoraService.registrar(bitacora);

				return companiaEnvioRepository.existsById(companiaEnvioRepository.save(companiaEnvio).getId()) && bitacoraService.verificarRegistro(bitacora.getId());
			}
			return false;
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}

	
	
}
