package mx.edu.utez.dto;

import lombok.Data;
import mx.edu.utez.model.Direccion;
import mx.edu.utez.model.Usuario;

@Data
public class DireccionUsuarioDto {

	private Long id;

    private Usuario usuario;
    private Direccion direccion;
}
