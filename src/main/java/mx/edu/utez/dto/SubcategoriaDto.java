package mx.edu.utez.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import mx.edu.utez.model.Categoria;

@Data
public class SubcategoriaDto {

	Long id;
    @Size(max = 45,min = 2,message = "Verificar el tamaño del nombre de la subcategoría")
    @NotBlank(message = "Nombre vacío")
    private String nombre;
    @Size(max = 45,min = 6,message = "Verificar el tamaño de la descripción de la subcategoría")
    @NotBlank(message = "Descripcion vacía")
    private String descripcion;
    @NotBlank
    private String imagen;
    private boolean estado;
    private Categoria categoria;

}
