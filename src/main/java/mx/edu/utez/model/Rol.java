package mx.edu.utez.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class Rol implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "rol"
    )
    private List<UsuarioRol> usuarioRoles;

    public Rol() {
        super();
    }

    public Rol(@Size(max = 25, min = 5, message = "Verificar el tamaño del nombre") @NotBlank(message = "Nombre vacío") String nombre, @Size(max = 25, min = 5, message = "Verificar el tamaño de la descripción") @NotBlank(message = "Descripción vacía") String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion;
    }
}
