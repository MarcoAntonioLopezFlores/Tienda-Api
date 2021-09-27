package mx.edu.utez.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Telefono implements Serializable {

	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;

    @ManyToOne(optional = false)
    private Usuario usuario;

    public Telefono() {
        super();
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", usuario=" + usuario.getId();
    }
}
