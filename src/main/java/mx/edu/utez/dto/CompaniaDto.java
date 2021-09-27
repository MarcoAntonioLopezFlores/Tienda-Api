package mx.edu.utez.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CompaniaDto {

	private Long id;
    @Size(max = 100,min = 2,message = "Verificar el tamaño de la descripción de la compañía envío")
    @NotBlank(message = "Descripción vacía")
    private String descripcion;
    @NotBlank(message = "RFC vacío")
    @Size(max = 13,min = 12,message = "Verificar el tamaño del RFC")
    private String rfc;
    private boolean estado;
}
