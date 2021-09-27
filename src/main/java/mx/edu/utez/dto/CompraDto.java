package mx.edu.utez.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import mx.edu.utez.model.Carrito;
import mx.edu.utez.model.CompaniaEnvio;
import mx.edu.utez.model.Direccion;
import mx.edu.utez.model.NumeroTarjeta;
import mx.edu.utez.model.Usuario;

@Data
public class CompraDto {
	private Long id;
	private String codigo;
    private Date fechaPago;
    @NotBlank
    private String estado;
    private NumeroTarjeta numeroTarjeta;
    private Direccion direccion;
    private Carrito carrito;
    private CompaniaEnvio companiaEnvio;
    private  Usuario usuario;

}
