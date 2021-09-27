package mx.edu.utez.service;

import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.Bitacora;
import mx.edu.utez.model.Telefono;
import mx.edu.utez.model.Usuario;
import mx.edu.utez.repository.TelefonoRepository;
import mx.edu.utez.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TelefonoService {

    @Autowired
    private TelefonoRepository telefonoRepository;

    @Autowired
    private BitacoraService bitacoraService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final String TABLA_TELEFONO = "telefono";

    public List<Telefono> listarTelefonosUsuario(HttpServletRequest httpServletRequest) throws ApiRequestException{
        try{
           return  telefonoRepository.findAllByUsuario(usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

    public Telefono telefonoUsuario(long id) throws ApiRequestException{
        try{
            Optional<Telefono> telefonoOptional = telefonoRepository.findById(id);
            return telefonoOptional.orElseGet(Telefono::new);
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

    public boolean registrar(Telefono telefono,HttpServletRequest httpServletRequest)throws ApiRequestException{
        try{
            Usuario usuario= usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser());
            if(telefonoRepository.findByDescripcion(telefono.getDescripcion())==null){
                telefono.setUsuario(usuario);
                telefono=telefonoRepository.save(telefono);
                Bitacora bitacora = new Bitacora(TABLA_TELEFONO, telefono.toString(), "", "INSERT", httpServletRequest.getRemoteAddr(),
                       usuario);
                bitacoraService.registrar(bitacora);
                return telefonoRepository.existsById(telefono.getId()) && bitacoraService.verificarRegistro(bitacora.getId());
            }
           return false;
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

    public boolean actualizar(Telefono telefono,HttpServletRequest httpServletRequest)throws ApiRequestException{
        try{
            Usuario usuario= usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser());
            telefono.setUsuario(usuario);
            Optional<Telefono> telefonoOptional = telefonoRepository.findById(telefono.getId());
            Telefono telefonoTemporal = telefonoOptional.orElseGet(Telefono::new);
            if(telefonoRepository.findByDescripcion(telefono.getDescripcion())==null){
                Bitacora bitacora = new Bitacora(TABLA_TELEFONO, telefono.toString(), telefonoTemporal.toString(), "UPDATE", httpServletRequest.getRemoteAddr(),
                        usuario);
                bitacoraService.registrar(bitacora);
                telefonoTemporal.setDescripcion(telefono.getDescripcion());
                telefonoTemporal=telefonoRepository.save(telefonoTemporal);

                return telefonoRepository.existsById(telefonoTemporal.getId()) && bitacoraService.verificarRegistro(bitacora.getId());
            }
            return false;
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

    public boolean borrarTelefono(long id,HttpServletRequest httpServletRequest) throws ApiRequestException{
        try{
            Optional<Telefono> telefonoOptional = telefonoRepository.findById(id);
            Telefono telefono = telefonoOptional.orElseGet(Telefono::new);
            Bitacora bitacora = new Bitacora(TABLA_TELEFONO, "Sin información nueva", telefono.toString(), "DELETE", httpServletRequest.getRemoteAddr(),
                    usuarioRepository.findByCorreo(httpServletRequest.getRemoteUser()));
            bitacoraService.registrar(bitacora);
            telefonoRepository.delete(telefono);

           return bitacoraService.verificarRegistro(bitacora.getId()) && !telefonoRepository.existsById(telefono.getId());
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }
}
