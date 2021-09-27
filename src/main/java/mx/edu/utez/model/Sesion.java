package mx.edu.utez.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Sesion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @NotBlank
    @Size(max = 45)
    private String host;
    @ManyToOne(optional = false)
    private Usuario usuario;

    public Sesion(@NotBlank String host, Usuario usuario) {
        this.host = host;
        this.usuario = usuario;
    }

    public Sesion() {
        super();
    }

    @PrePersist
    public void prePersist(){
        fechaRegistro = new Date(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", fechaRegistro=" + fechaRegistro +
                ", host='" + host + '\'' +
                ", usuario=" + usuario.getId();
    }
}
