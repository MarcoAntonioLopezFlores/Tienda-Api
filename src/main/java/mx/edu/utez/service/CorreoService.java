package mx.edu.utez.service;

import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.TokenCorreo;
import mx.edu.utez.model.Usuario;
import mx.edu.utez.repository.TokenCorreoRepository;
import mx.edu.utez.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
public class CorreoService {

    @Autowired
    private JavaMailSender sender;
    @Autowired
    private TokenCorreoRepository tokenCorreoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean enviarCorreo(Usuario usuario, String token) throws ApiRequestException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String url = "http://localhost:3000/recuperar/cambiar/" + token;

        try {
            helper.setTo(usuario.getCorreo());
            helper.setText("<h1>Estimado/a " + usuario.getNombre() + " " + usuario.getApellidos() + "</h1>\n" +
                    "    <h2>¿Ha olvidado su contraseña?</h2>\n" +
                    "    <h3>Ingrese al siguiente link para establecer una nueva contraseña </h3>\n" +
                    "    <h4> " + url + "</h4>\n" +
                    "    <h3>Si no desea cambiar su contraseña o no ha solicitado este cambio, ignore o elimine este mensaje</h3>", true);
            helper.setSubject("Restablecer contraseña");
            sender.send(message);
            return true;
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }

    public boolean enviar(String correo) throws ApiRequestException {
        try{
            Usuario usuario = usuarioRepository.findByCorreo(correo);
            if(usuario != null && usuario.isEnabled()){
                TokenCorreo tokenCorreo = tokenCorreoRepository.findByUsuarioAndEstadoIsTrue(usuario);
                if(tokenCorreo == null){
                    String token = UUID.randomUUID().toString();
                    if(enviarCorreo(usuario, token)){
                        tokenCorreo = new TokenCorreo(token, usuario);
                        return tokenCorreoRepository.existsById(tokenCorreoRepository.save(tokenCorreo).getId());
                    }else{
                        return false;
                    }
                }else{
                    tokenCorreo.setEstado(false);
                    tokenCorreoRepository.save(tokenCorreo);
                    String token = UUID.randomUUID().toString();
                    if(enviarCorreo(usuario, token)){
                        tokenCorreo = new TokenCorreo(token, usuario);
                        return tokenCorreoRepository.existsById(tokenCorreoRepository.save(tokenCorreo).getId());
                    }else{
                        return false;
                    }
                }
            }else{
                return false;
            }
        }catch (Exception e){
            throw new ApiRequestException(e);
        }
    }
}
