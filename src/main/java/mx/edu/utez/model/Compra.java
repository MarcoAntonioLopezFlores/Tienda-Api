package mx.edu.utez.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Compra implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String codigo;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;
    
    private String estado;
    @ManyToOne(optional = false)
    private NumeroTarjeta numeroTarjeta;
    @ManyToOne(optional = false)
    private Direccion direccion;
    @ManyToOne(optional = false)
    private Carrito carrito;
    @ManyToOne(optional = false)
    private CompaniaEnvio companiaEnvio;
    @ManyToOne(optional = false)
    private  Usuario usuario;

    @PrePersist
    public void prePersist(){
        fechaPago = new Date(System.currentTimeMillis());
    }

    public Compra() {
        super();
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", fechaPago=" + fechaPago +
                ", estado='" + estado +
                ", numeroTarjeta=" + numeroTarjeta.getId() +
                ", direccion=" + direccion.getId() +
                ", carrito=" + carrito.getId() +
                ", usuario=" + usuario.getId() +
                ", companiaEnvio=" + companiaEnvio.getId();
    }
}
