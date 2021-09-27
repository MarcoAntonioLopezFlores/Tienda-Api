package mx.edu.utez.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class MarcaDto {

    private Long id;
    @Size(max = 45, message = "Verificar el tamaño del nombre de la marca")
    @NotBlank(message = "Nombre vacío")
    private String nombre;
    @Size(max = 45,min = 3,message = "Verificar el tamaño de la descripción de la marca")
	@NotBlank(message = "Descripcion vacía")
    private String descripcion;
    private boolean estado;
}
