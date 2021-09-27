package mx.edu.utez.controller;

import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.model.Bitacora;
import mx.edu.utez.model.Sesion;
import mx.edu.utez.service.BitacoraService;
import mx.edu.utez.service.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/bitacora/")
public class BitacoraController {
    @Autowired
    private SesionService sesionService;

    @Autowired
    private BitacoraService bitacoraService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("listar")
    public List<Bitacora> listarBitacora() throws ApiRequestException {
        return bitacoraService.listarBitacora();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("sesion/listar")
    public List<Sesion> listarSesion() throws ApiRequestException {
        return sesionService.obtenerSesiones();
    }

}
