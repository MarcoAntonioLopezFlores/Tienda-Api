package mx.edu.utez.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Bitacora implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 45)
    private String tabla;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @NotBlank
    @Size(max = 1000)
    private String nuevaInformacion;
    @Size(max = 1000)
    private String antiguaInformacion;
    @NotBlank
    @Size(max = 45)
    private String accion;
    @NotBlank
    @Size(max = 45)
    private String host;
    @ManyToOne(optional = false)
    private Usuario usuario;

    public Bitacora(@NotBlank @Size(max = 45) String tabla,@NotBlank  @Size(max = 1000) String nuevaInformacion, @Size(max = 1000) String antiguaInformacion, @NotBlank @Size(max = 45) String accion, @NotBlank @Size(max = 45) String host, Usuario usuario) {
        this.tabla = tabla;
        this.nuevaInformacion = nuevaInformacion;
        this.antiguaInformacion = antiguaInformacion;
        this.accion = accion;
        this.host = host;
        this.usuario = usuario;
    }

    public Bitacora() {
        super();
    }

    @PrePersist
    public void prePersist(){
        fechaRegistro = new Date(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", tabla='" + tabla + '\'' +
                ", nuevaInformacion='" + nuevaInformacion + '\'' +
                ", antiguaInformacion='" + antiguaInformacion + '\'' +
                ", accion='" + accion + '\'' +
                ", usuario=" + usuario.getId();
    }
}
