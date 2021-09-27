package mx.edu.utez.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RolDto {

	private Long id;
    @Size(max = 25,min = 1,message = "Verificar el tamaño del nombre del rol")
    @NotBlank(message = "Nombre vacío")
    private String nombre;
    @Size(max = 25,message = "Verificar el tamaño de la descripción del rol")
    @NotBlank(message = "Descripción vacía")
    private String descripcion;
}
