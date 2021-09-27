package mx.edu.utez.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.edu.utez.dto.DireccionUsuarioDto;
import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.DireccionUsuario;
import mx.edu.utez.service.DireccionUsuarioService;

@RestController
@RequestMapping("/app/direccionusuario/")
public class DireccionUsuarioController {
	@Autowired
	private final DireccionUsuarioService direccionUsuarioService;
	@Autowired
	private ModelMapper modelMapper;
	public DireccionUsuarioController(DireccionUsuarioService direccionUsuarioService) {
		this.direccionUsuarioService = direccionUsuarioService;
	}

	@GetMapping(value = "listar")
	public List<DireccionUsuario> obtenerDireccionesByUsuario(HttpServletRequest httpServletRequest)
			throws ApiRequestException {
		return direccionUsuarioService.obtenerDireccionesByUsuario(httpServletRequest);
	}

    @PostMapping(value="registrar")
	public boolean registrarDireccionSucursal(@Valid @RequestBody DireccionUsuarioDto direccionUsuario ,HttpServletRequest httpServletRequest) throws ApiRequestException {
		return direccionUsuarioService.registrar(convertToEntity(direccionUsuario), httpServletRequest);

	}

	@DeleteMapping(value="eliminar/{id}")
	public boolean eliminar(@PathVariable long id,HttpServletRequest httpServletRequest) throws ApiRequestException {
		return direccionUsuarioService.borrar(id,httpServletRequest);
	}

	@PutMapping(value="actualizar")
	public boolean actualizar(@Valid @RequestBody DireccionUsuarioDto direccionUsuario,HttpServletRequest httpServletRequest) throws ApiRequestException {
		return direccionUsuarioService.actualizar(convertToEntity(direccionUsuario),httpServletRequest);
	}
	
	private DireccionUsuario convertToEntity(DireccionUsuarioDto direccionUsuarioDto){
		return modelMapper.map(direccionUsuarioDto, DireccionUsuario.class);
	}
}

