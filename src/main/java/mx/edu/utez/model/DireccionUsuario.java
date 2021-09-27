package mx.edu.utez.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class DireccionUsuario implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Usuario usuario;
    @ManyToOne(optional = false)
    private Direccion direccion;

    public DireccionUsuario(Direccion direccionRegistrada, Usuario usuario) {
        this.direccion=direccionRegistrada;
        this.usuario=usuario;
    }

    public DireccionUsuario() {
        super();
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", usuario=" + usuario.getId() +
                ", direccion=" + direccion.getId();
    }
}
