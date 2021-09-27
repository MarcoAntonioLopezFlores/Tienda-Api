package mx.edu.utez.dto;

import lombok.Data;
import mx.edu.utez.model.CompaniaEnvio;
import mx.edu.utez.model.Direccion;

@Data
public class SucursalCompaniaDto {

	private Long id;
    private CompaniaEnvio companiaEnvio;
    private Direccion direccion;
}
