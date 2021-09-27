package mx.edu.utez.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UsuarioDto {
	private Long id;
    @Size(max = 25,min = 4,message = "Verificar el tamaño del nombre")
    @NotBlank(message = "Nombre vacío")
    private String nombre;
    @Size(max = 25,min = 4,message = "Verificar el tamaño del apellido")
    @NotBlank(message = "Apellido vacío")
    private String apellidos;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Size(max = 25,min = 8,message = "Verificar el tamaño del correo")
    @NotBlank(message = "Correo vacío")
    private String correo;
    @Size(max = 100,min = 8,message = "Verificar el tamaño de la contraseña")
    @NotBlank(message = "Contraseña vacía")
    private String password;
    private boolean enabled;
}
