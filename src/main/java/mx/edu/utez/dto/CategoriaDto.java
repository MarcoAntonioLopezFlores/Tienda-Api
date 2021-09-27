package mx.edu.utez.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CategoriaDto {
	private Long id;
    @Size(max = 30, message = "Verificar el tamaño del nombre de la categoría")
    @NotBlank(message = "Nombre vacío")
    private String nombre;
    @Size(max = 70, message = "Verificar el tamaño de la descripción de la categoría")
    @NotBlank(message = "Descripcion vacía")
    private String descripcion;
    private boolean estado;
}
