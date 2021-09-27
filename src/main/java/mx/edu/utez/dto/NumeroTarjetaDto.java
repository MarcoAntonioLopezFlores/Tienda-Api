package mx.edu.utez.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import mx.edu.utez.model.Usuario;

@Data
public class NumeroTarjetaDto {

	private Long id;
	@NotBlank
    private String nombre;
    @Size(max = 100,min = 16,message = "Verificar el tamaño del número de tarjeta")
    @NotBlank(message = "Descripción vacía")
    private String descripcion;
    private boolean estado;
    private Usuario usuario;
}
