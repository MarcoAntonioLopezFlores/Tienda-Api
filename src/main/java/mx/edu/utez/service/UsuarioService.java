package mx.edu.utez.service;

import mx.edu.utez.configuration.JwtResponse;
import mx.edu.utez.configuration.JwtTokenUtil;
import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.*;
import mx.edu.utez.repository.RolRepository;
import mx.edu.utez.repository.TokenCorreoRepository;
import mx.edu.utez.repository.UsuarioRepository;
import mx.edu.utez.repository.UsuarioRolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Transactional
public class UsuarioService implements UserDetailsService {

    private UserDetails userDetails;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioRolRepository usuarioRolRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private BitacoraService bitacoraService;
    @Autowired
    private SesionService sesionService;
    @Autowired
    private TokenCorreoRepository tokenCorreoRepository;
    private static final String TABLA_USUARIO = "usuario";

    public boolean registrar(Usuario usuario, String descripcion, HttpServletRequest httpServletRequest) throws ApiRequestException {

        UsuarioRol usuarioRol;

        try {
            if(usuarioRepository.findByCorreo(usuario.getCorreo()) == null){
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
                Rol rol = rolRepository.findByNombre(descripcion);
                usuario = usuarioRepository.save(usuario);


                if(rol != null){
                    usuarioRol = new UsuarioRol();
                    usuarioRol.setUsuario(usuario);
                    usuarioRol.setRol(rol);
                    usuarioRol = usuarioRolRepository.save(usuarioRol);
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    String tablaRolUsuario = "usuario_rol";
                    String insertar = "INSERT";
                    if(authentication.getPrincipal() != "anonymousUser"){
                        userDetails = (UserDetails) authentication.getPrincipal();
                        bitacoraService.registrar(new Bitacora(TABLA_USUARIO, usuario.toString(), "", insertar, httpServletRequest.getRemoteAddr(), usuarioRepository.findByCorreo(userDetails.getUsername())));
                        bitacoraService.registrar(new Bitacora(tablaRolUsuario, usuarioRol.toString(), "", insertar, httpServletRequest.getRemoteAddr(), usuarioRepository.findByCorreo(userDetails.getUsername())));
                    }else{
                        bitacoraService.registrar(new Bitacora(TABLA_USUARIO, usuario.toString(), "", insertar, httpServletRequest.getRemoteAddr(), usuario));
                        bitacoraService.registrar(new Bitacora(tablaRolUsuario, usuarioRol.toString(), "", insertar, httpServletRequest.getRemoteAddr(), usuario));
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }catch(DataIntegrityViolationException e) {
			return false;
		}catch (Exception e) {
			throw new ApiRequestException(e);
		}

        return usuarioRolRepository.existsById(usuarioRol.getId()) && usuarioRepository.existsById(usuario.getId());
    }

    public List<Usuario> listar() throws ApiRequestException {
        try{
            return usuarioRepository.findAll();
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

    public JwtResponse crearAutenticacionToken(@RequestBody Usuario usuario, HttpServletRequest httpServletRequest) throws ApiRequestException{
        if(autenticacion(usuario.getCorreo(), usuario.getPassword())){
            String token;

            try{
                userDetails = loadUserByUsername(usuario.getCorreo());

                token = jwtTokenUtil.generateToken(userDetails);

                if(usuarioRepository.findByCorreo(usuario.getCorreo()) != null){
                    usuario = usuarioRepository.findByCorreo(usuario.getCorreo());
                    sesionService.registrarSesion(new Sesion(httpServletRequest.getRemoteAddr(), usuario));
                    if(!usuario.isEnabled()){
                        return new JwtResponse("false");
                    }
                }
            }catch (Exception e){
                throw new ApiRequestException(e);
            }

            return new JwtResponse(token);
        }else{
            return new JwtResponse("false");
        }
    }

    public boolean autenticacion(String username, String password) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return true;
        }catch (DisabledException | BadCredentialsException e){
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(username);

        if(usuario != null){
            List<GrantedAuthority> authorities = getUserAuthority(usuarioRolRepository.findByUsuario(usuario));
            return buildUserForAuthentication(usuario, authorities);
        }else{
            throw new UsernameNotFoundException("No se encontró");
        }
    }

    private List<GrantedAuthority> getUserAuthority(List<UsuarioRol> usuarioRoles){
        Set<GrantedAuthority> roles = new HashSet<>();

        usuarioRoles.forEach(rolUsuario -> roles.add(new SimpleGrantedAuthority(rolUsuario.getRol().getNombre())));

        return new ArrayList<>(roles);
    }

    private UserDetails buildUserForAuthentication(Usuario usuario, List<GrantedAuthority> authorities){
        return new User(usuario.getCorreo(), usuario.getPassword(), authorities);
    }

    public boolean cambiarContrasenia(String contrasenia, HttpServletRequest httpServletRequest) throws ApiRequestException {
        try{
            Usuario usuario = usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser());
            usuario.setPassword(passwordEncoder.encode(contrasenia));
            usuario = usuarioRepository.save(usuario);
            return usuarioRepository.existsById(usuario.getId());
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

    public Usuario actualizar(Usuario objeto, HttpServletRequest httpServletRequest) throws ApiRequestException{
        try{
            Optional<Usuario> usuarioOptional = usuarioRepository.findById(objeto.getId());
            Usuario usuario = usuarioOptional.orElseGet(Usuario::new);
            usuario.setNombre(objeto.getNombre());
            usuario.setApellidos(objeto.getApellidos());
            usuario.setCorreo(objeto.getCorreo());
            usuario.setEnabled(objeto.isEnabled());
            usuario = usuarioRepository.save(usuario);
            Bitacora bitacora = new Bitacora(TABLA_USUARIO, usuario.toString(), objeto.toString(), "UPDATE", httpServletRequest.getRemoteAddr(), usuario);
            bitacoraService.registrar(bitacora);
            return usuario;
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

    public boolean restablecerContrasenia(Usuario objeto, String token, HttpServletRequest httpServletRequest) throws ApiRequestException {
        try{
            TokenCorreo tokenCorreo = tokenCorreoRepository.findByDescripcionAndEstadoIsTrue(token);
            if(tokenCorreo != null){
                Usuario usuario = tokenCorreo.getUsuario();
                usuario.setPassword(passwordEncoder.encode(objeto.getPassword()));
                Bitacora bitacora = new Bitacora(TABLA_USUARIO, usuario.toString(), tokenCorreo.getUsuario().toString(), "UPDATE", httpServletRequest.getRemoteAddr(), usuario);
                bitacoraService.registrar(bitacora);
                tokenCorreo.setEstado(false);
                return usuarioRepository.existsById(usuarioRepository.save(usuario).getId()) && tokenCorreoRepository.existsById(tokenCorreoRepository.save(tokenCorreo).getId());
            }else{
                return false;
            }
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }
}
