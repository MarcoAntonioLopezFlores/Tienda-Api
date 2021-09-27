package mx.edu.utez.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class Direccion implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String entidadFederativa;
    
    private String municipio;
    
    private String codigoPostal;
    
    private String asentamiento;
    
    private String calle;
    
    private String numeroExterior;
    
    private String tipoAsentamiento;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "direccion"
    )
    private List<SucursalCompania> sucursalCompanias;
    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "direccion"
    )
    private List<Compra> compras;
    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "direccion"
    )
    private List<DireccionUsuario> direccionUsuarios;

    public Direccion() {
        super();
    }

	@Override
	public String toString() {
		return  "id=" + id +
				", entidadFederativa='" + entidadFederativa + '\'' +
				", municipio='" + municipio + '\'' +
				", codigoPostal='" + codigoPostal + '\'' +
				", asentamiento='" + asentamiento + '\'' +
				", calle='" + calle + '\'' +
				", numeroExterior='" + numeroExterior + '\'' +
				", tipoAsentamiento='" + tipoAsentamiento;
	}
}
