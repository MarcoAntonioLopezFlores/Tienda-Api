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


import mx.edu.utez.model.Rol;
import mx.edu.utez.repository.RolRepository;

@Service
@Transactional
public class RolService {
	
	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private BitacoraService bitacoraService;

	@Autowired
	private UsuarioRepository usuarioRepository;


	public boolean registrar(Rol rol, HttpServletRequest httpServletRequest) throws ApiRequestException {

		try{
			if(rolRepository.findByNombreAndDescripcion(rol.getNombre(), rol.getDescripcion())==null){
				Bitacora bitacora;
				rol = rolRepository.save(rol);
				bitacora = new Bitacora("rol", rol.toString(), "", "INSERT", httpServletRequest.getRemoteAddr(),
						usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
				bitacoraService.registrar(bitacora);
				return rolRepository.existsById(rol.getId()) && bitacoraService.verificarRegistro(bitacora.getId());
			}
			return false;
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}

	public boolean actualizar(Rol rol, HttpServletRequest httpServletRequest) throws ApiRequestException {

		try{
			if(rolRepository.findByNombreAndDescripcion(rol.getNombre(), rol.getDescripcion())==null){
				Bitacora bitacora;
				Optional<Rol> rolOptional = rolRepository.findById(rol.getId());
				Rol rolTemporal = rolOptional.orElseGet(Rol::new);
				bitacora = new Bitacora(
						"rol", rol.toString(), rolTemporal.toString(), "UPDATE", httpServletRequest.getRemoteAddr(),
						usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
				rolTemporal.setDescripcion(rol.getDescripcion());
				rolTemporal.setNombre(rol.getNombre());
				rolTemporal=rolRepository.save(rolTemporal);
				bitacoraService.registrar(bitacora);
				return rolRepository.existsById(rolTemporal.getId()) && bitacoraService.verificarRegistro(bitacora.getId());
			}
			return false;
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}


	public Rol obtener(long id) throws ApiRequestException {

		try{
			Optional<Rol> rolOptional = rolRepository.findById(id);
			return rolOptional.orElseGet(Rol::new);
		}catch (Exception e){
			throw new ApiRequestException(e);
		}

	}
	
	public boolean borrar (long id, HttpServletRequest httpServletRequest) throws ApiRequestException {
		try{
			Optional<Rol> rolOptional = rolRepository.findById(id);
			Rol rolTemporal = rolOptional.orElseGet(Rol::new);
			Bitacora bitacora = new Bitacora(
					"rol", "Sin información nueva", rolTemporal.toString(), "DELETE", httpServletRequest.getRemoteAddr(),
					usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
			bitacoraService.registrar(bitacora);
			rolRepository.deleteById(id);
			return !rolRepository.existsById(id) && bitacoraService.verificarRegistro(bitacora.getId());
		}catch (Exception e){
			throw new ApiRequestException(e);
		}
	}
	
	public List<Rol> obtenerRoles() throws ApiRequestException {
		try{
			return rolRepository.findByDescripcionNot("Administrador");
		}catch (Exception e){
			throw new ApiRequestException(e);
		}

	}

}
