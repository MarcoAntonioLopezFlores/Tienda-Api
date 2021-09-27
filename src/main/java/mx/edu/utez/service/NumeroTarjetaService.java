package mx.edu.utez.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.Bitacora;
import mx.edu.utez.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mx.edu.utez.model.NumeroTarjeta;
import mx.edu.utez.repository.NumeroTarjetaRepository;

@Service
@Transactional
public class NumeroTarjetaService {

	
	@Autowired
	private NumeroTarjetaRepository numeroTarjetaRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private BitacoraService bitacoraService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private static final String TABLA_TARJETA = "numero_tarjeta";

	public boolean registrar(NumeroTarjeta numeroTarjeta,  HttpServletRequest httpServletRequest ) throws ApiRequestException {

		try{
			if(numeroTarjetaRepository.findByDescripcion(numeroTarjeta.getDescripcion())==null){
				numeroTarjeta.setDescripcion((passwordEncoder.encode(numeroTarjeta.getDescripcion().substring(0,12)) + numeroTarjeta.getDescripcion().substring(12, 16)));
				numeroTarjeta.setUsuario(usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
				numeroTarjeta= numeroTarjetaRepository.save(numeroTarjeta);
				Bitacora bitacora = new Bitacora(TABLA_TARJETA,numeroTarjeta.toString(), "", "INSERT", httpServletRequest.getRemoteAddr(), usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
				bitacoraService.registrar(bitacora);
				return numeroTarjetaRepository.existsById(numeroTarjeta.getId()) && bitacoraService.verificarRegistro(bitacora.getId());
			}
			return false;
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}
	public boolean actualizar(NumeroTarjeta numeroTarjeta,  HttpServletRequest httpServletRequest ) throws ApiRequestException {
		try{
			Bitacora bitacora;
			Optional<NumeroTarjeta> numeroTarjetaOptional = numeroTarjetaRepository.findById(numeroTarjeta.getId());
			NumeroTarjeta numeroTarjetaTemporal = numeroTarjetaOptional.orElseGet(NumeroTarjeta::new);
			numeroTarjeta.setUsuario(usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
			bitacora = new Bitacora(TABLA_TARJETA,numeroTarjeta.toString(), numeroTarjetaTemporal.toString(), "UPDATE", httpServletRequest.getRemoteAddr(), usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
			numeroTarjetaTemporal.setDescripcion((passwordEncoder.encode(numeroTarjeta.getDescripcion().substring(0,12)) + numeroTarjeta.getDescripcion().substring(12, 16)));
			numeroTarjetaTemporal.setNombre(numeroTarjeta.getNombre());
			numeroTarjetaTemporal.setDescripcion(numeroTarjeta.getDescripcion());
			numeroTarjetaTemporal.setEstado(numeroTarjeta.isEstado());
			bitacoraService.registrar(bitacora);
			numeroTarjetaTemporal= numeroTarjetaRepository.save(numeroTarjetaTemporal);


			return numeroTarjetaRepository.existsById(numeroTarjetaTemporal.getId()) && bitacoraService.verificarRegistro(bitacora.getId());
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}

	public List<NumeroTarjeta> obtenerTarjetasCliente(HttpServletRequest httpServletRequest) throws ApiRequestException {
		try{
			return numeroTarjetaRepository.findAllByUsuarioAndEstadoIsTrue(usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
		}catch (Exception e){
			throw new ApiRequestException(e);
		}

	}
	
	public NumeroTarjeta obtenerTarjeta(long id) throws ApiRequestException {
		try{
			Optional<NumeroTarjeta> numeroTarjetaOptional = numeroTarjetaRepository.findById(id);
			return numeroTarjetaOptional.orElseGet(NumeroTarjeta::new);
		}catch (Exception e){
			throw new ApiRequestException(e);
		}

	}
	
	public boolean borrar (long id,HttpServletRequest httpServletRequest) throws ApiRequestException {
		try{
			Optional<NumeroTarjeta> numeroTarjetaOptional = numeroTarjetaRepository.findById(id);
			NumeroTarjeta numeroTarjeta = numeroTarjetaOptional.orElseGet(NumeroTarjeta::new);
			Bitacora bitacora = new Bitacora(
					TABLA_TARJETA, "Sin información nueva", numeroTarjeta.toString(), "DELETE", httpServletRequest.getRemoteAddr(),
					usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
			bitacoraService.registrar(bitacora);
			numeroTarjetaRepository.deleteById(id);
			return !numeroTarjetaRepository.existsById(id) && bitacoraService.verificarRegistro(bitacora.getId());
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}
	public boolean bajaLogica (long id,HttpServletRequest httpServletRequest) throws ApiRequestException {
		try{
			Optional<NumeroTarjeta> numeroTarjetaOptional = numeroTarjetaRepository.findById(id);
			NumeroTarjeta numeroTarjeta = numeroTarjetaOptional.orElseGet(NumeroTarjeta::new);
			numeroTarjeta.setEstado(!numeroTarjeta.isEstado());
			Bitacora bitacora = new Bitacora(
					TABLA_TARJETA, numeroTarjeta.toString(), numeroTarjetaOptional.isPresent() ? numeroTarjetaOptional.get().toString() : "", "DELETE", httpServletRequest.getRemoteAddr(),
					usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
			bitacoraService.registrar(bitacora);
			return numeroTarjetaRepository.existsById(numeroTarjetaRepository.save(numeroTarjeta).getId()) && bitacoraService.verificarRegistro(bitacora.getId());
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}

}
