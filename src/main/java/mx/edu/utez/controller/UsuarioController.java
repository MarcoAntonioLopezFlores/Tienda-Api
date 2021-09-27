package mx.edu.utez.controller;

import mx.edu.utez.configuration.JwtResponse;
import mx.edu.utez.dto.UsuarioDto;
import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.Usuario;
import mx.edu.utez.service.CorreoService;
import mx.edu.utez.service.UsuarioService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("app/usuario/")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private CorreoService correoService;
    @Autowired
	private ModelMapper modelMapper;

    @PostMapping("login")
    public ResponseEntity<JwtResponse> crearAutenticacionToken(@RequestBody UsuarioDto usuario, HttpServletRequest httpServletRequest) throws ApiRequestException{
        return ResponseEntity.ok(usuarioService.crearAutenticacionToken(convertToEntity(usuario), httpServletRequest));
    }

    @PostMapping("registrar")
    public boolean registrar(@RequestBody UsuarioDto usuario, @RequestParam String rol, HttpServletRequest httpServletRequest) throws ApiRequestException{
        return usuarioService.registrar(convertToEntity(usuario), rol, httpServletRequest);
    }

    @GetMapping("listar")
    public List<Usuario> listar() throws ApiRequestException {
        return usuarioService.listar();
    }

    @GetMapping("enviar/{correo}")
    public boolean enviar(@PathVariable String correo) throws ApiRequestException{
        return correoService.enviar(correo);
    }

    @GetMapping("cambiarContrasenia/{contrasenia}")
    public boolean cambiarContrasenia(@PathVariable String contrasenia, HttpServletRequest httpServletRequest) throws ApiRequestException {
        return usuarioService.cambiarContrasenia(contrasenia, httpServletRequest);
    }

    @PutMapping("actualizar")
    public Usuario actualizar(@RequestBody UsuarioDto usuario, HttpServletRequest httpServletRequest) throws ApiRequestException{
        return usuarioService.actualizar(convertToEntity(usuario), httpServletRequest);
    }

    @PostMapping("restablecer/{token}")
    public boolean restablecer(@RequestBody UsuarioDto usuario, @PathVariable String token, HttpServletRequest httpServletRequest) throws ApiRequestException {
        return usuarioService.restablecerContrasenia(convertToEntity(usuario), token, httpServletRequest);
    }
    
    private Usuario convertToEntity(UsuarioDto usuarioDto){
		return modelMapper.map(usuarioDto, Usuario.class);
	}
}
