package mx.edu.utez.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Data
public class TokenCorreo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String descripcion;
    private boolean estado;

    @ManyToOne(optional = false)
    private Usuario usuario;

    public TokenCorreo(@NotBlank String descripcion, Usuario usuario) {
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.estado = true;
    }

    public TokenCorreo() {
        super();
    }

    @Override
    public String toString() {
        return  "id=" + id + '\'' +
                ", estado=" + estado +
                ", descripcion='" + descripcion + '\'' +
                ", usuario=" + usuario.getId();
    }

}
