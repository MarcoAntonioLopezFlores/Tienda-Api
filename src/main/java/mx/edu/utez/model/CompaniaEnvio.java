package mx.edu.utez.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class CompaniaEnvio implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(unique = true)
    private String descripcion;
    private String rfc;
    private boolean estado;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "companiaEnvio"
    )
    private List<Compra> compras;
    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "companiaEnvio"
    )
    private List<SucursalCompania> sucursalCompanias;

    public CompaniaEnvio() {
        super();
    }

	@Override
	public String toString() {
		return  "id=" + id +
				", descripcion='" + descripcion + '\'' +
				", rfc='" + rfc + '\'' +
                ", estado= '" + estado;
	}
}
