package mx.edu.utez.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import mx.edu.utez.model.Usuario;
@Data
public class TelefonoDto {

	private Long id;
    @NotBlank
    private String descripcion;
    private Usuario usuario;
}
