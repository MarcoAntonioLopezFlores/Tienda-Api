package mx.edu.utez.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class SucursalCompania implements Serializable {

	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private CompaniaEnvio companiaEnvio;
    @ManyToOne(optional = false)
    private Direccion direccion;

    @Override
    public String toString() {
        return  "id=" + id +
                ", companiaEnvio=" + companiaEnvio.getId() +
                ", direccion=" + direccion.getId();
    }

	public SucursalCompania(CompaniaEnvio companiaEnvio, Direccion direccion) {
		this.companiaEnvio = companiaEnvio;
		this.direccion = direccion;
	}
	public SucursalCompania() {
        super();
	}
    
}
