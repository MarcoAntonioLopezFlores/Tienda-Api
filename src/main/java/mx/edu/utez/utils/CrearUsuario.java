package mx.edu.utez.utils;

import mx.edu.utez.model.Bitacora;
import mx.edu.utez.model.Rol;
import mx.edu.utez.model.Usuario;
import mx.edu.utez.model.UsuarioRol;
import mx.edu.utez.repository.RolRepository;
import mx.edu.utez.repository.UsuarioRepository;
import mx.edu.utez.repository.UsuarioRolRepository;
import mx.edu.utez.service.BitacoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CrearUsuario implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioRolRepository usuarioRolRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BitacoraService bitacoraService;

    @Override
    public void run(String... args) throws Exception {

        Rol rolAdministrador;
        Rol rolCliente;
        Usuario usuarioUno;
        Usuario usuarioDos;
        Usuario usuarioTres;
        UsuarioRol usuarioRolUno;
        UsuarioRol usuarioRolDos;
        UsuarioRol usuarioRolTres;
        String insertar = "INSERT";
        String host = "Sin host";
        String usuario = "usuario";
        String usuarioRol = "usuario_rol";

        if(usuarioRepository.findByCorreo("gustavo@gmail.com")==null){
            usuarioUno = usuarioRepository.save(new
                    Usuario("Gustavo Joel", "Flores", "gustavo@gmail.com",passwordEncoder.encode("@123$78Ab")));
            usuarioDos = usuarioRepository.save(new
                    Usuario("Saúl Eduardo", "Landa", "saul@gmail.com",passwordEncoder.encode("@123$78Abc")));
            usuarioTres = usuarioRepository.save(new
                    Usuario("Miguel Angel", "Paredes", "miguel@gmail.com",passwordEncoder.encode("@123$78AbcD")));

            bitacoraService.registrar(new Bitacora(usuario, usuarioUno.toString(), "", insertar, host, usuarioUno));
            bitacoraService.registrar(new Bitacora(usuario, usuarioDos.toString(), "", insertar, host, usuarioDos));
            bitacoraService.registrar(new Bitacora(usuario, usuarioTres.toString(), "", insertar, host, usuarioTres));

            if(rolRepository.findByNombre("ROLE_ADMIN")==null){

                rolAdministrador=rolRepository.save(new Rol("ROLE_ADMIN","Administrador"));
                bitacoraService.registrar(new Bitacora("rol", rolAdministrador.toString(), "", insertar, host, usuarioUno));

                rolCliente=rolRepository.save(new Rol("ROLE_CLIENTE","Cliente"));
                bitacoraService.registrar(new Bitacora("rol", rolCliente.toString(), "", insertar, host, usuarioUno));

                usuarioRolUno = usuarioRolRepository.save(new UsuarioRol(usuarioUno,rolAdministrador));
                usuarioRolDos = usuarioRolRepository.save(new UsuarioRol(usuarioDos,rolAdministrador));
                usuarioRolTres = usuarioRolRepository.save(new UsuarioRol(usuarioTres,rolAdministrador));

                bitacoraService.registrar(new Bitacora(usuarioRol, usuarioRolUno.toString(), "", insertar, host, usuarioUno));
                bitacoraService.registrar(new Bitacora(usuarioRol, usuarioRolDos.toString(), "", insertar, host, usuarioUno));
                bitacoraService.registrar(new Bitacora(usuarioRol, usuarioRolTres.toString(), "", insertar, host, usuarioUno));

            }

        }

    }
}
