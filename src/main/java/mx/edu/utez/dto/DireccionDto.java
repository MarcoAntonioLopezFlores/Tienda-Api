package mx.edu.utez.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class DireccionDto {

	private Long id;
    @NotBlank
    private String entidadFederativa;
    @NotBlank
    private String municipio;
    @NotBlank
    private String codigoPostal;
    @NotBlank
    private String asentamiento;
    @NotBlank
    private String calle;
    @NotBlank
    private String numeroExterior;
    @NotBlank
    private String tipoAsentamiento;

}
