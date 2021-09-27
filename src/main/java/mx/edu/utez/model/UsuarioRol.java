package mx.edu.utez.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class UsuarioRol implements Serializable {

	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Usuario usuario;
    @ManyToOne(optional = false)
    private Rol rol;

    public UsuarioRol() {
        super();
    }
    public UsuarioRol(Usuario usuario, Rol rol) {
        this.usuario = usuario;
        this.rol = rol;
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", usuario=" + usuario.getId() +
                ", rol=" + rol.getId();
    }
}
