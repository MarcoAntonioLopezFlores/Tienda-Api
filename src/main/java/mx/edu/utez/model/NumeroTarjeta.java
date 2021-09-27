package mx.edu.utez.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class NumeroTarjeta implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private boolean estado;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "numeroTarjeta"
    )
    private List<Compra> compras;
    @ManyToOne(optional = false)
    private Usuario usuario;

    public NumeroTarjeta() {
        super();
    }

	@Override
	public String toString() {
		return  "id=" + id + '\'' +
                ", nombre=" + nombre + '\'' +
				", descripcion='" + descripcion + '\'' +
				", usuario=" + usuario.getId() + '\'' +
                ", estado= '" + estado;
	}
}
