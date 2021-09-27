package mx.edu.utez.dto;

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import mx.edu.utez.model.Marca;
import mx.edu.utez.model.Subcategoria;

@Data
public class ProductoDto {

	private Long id;
	@Size(max = 100, min = 2, message = "Verificar el tamaño del nombre del producto")
	@NotBlank(message = "Nombre vacío")
	private String nombre;
	@Size(max = 1000, min = 2, message = "Verificar el tamaño de la descripción de producto")
	@NotBlank(message = "Descripcion vacía")
	private String descripcion;
	@NotBlank
	private String imagen;
	@Size(max = 45, min = 2, message = "Verificar el tamaño del modelo")
	@NotBlank(message = "Modelo vacío")
	private String modelo;
	@Digits(integer=20, fraction=0)
	private double precio;

	private int existencia;
	@NotBlank
	private String color;

	private boolean estado;
    private Date fechaRegistro;
    
    private Marca marca;
    private Subcategoria subcategoria;
}
