package mx.edu.utez.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class Carrito implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private double total;
    private boolean estado;

    @ManyToOne(optional = false)
    private Usuario usuario;
    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "carrito"
    )
    private List<Compra> compras;
    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "carrito"
    )
    private List<CarritoProducto> carritoProductos;

    public Carrito() {
        super();
    }

    public Carrito(double total, Usuario usuario) {
        this.total = total;
        this.usuario = usuario;
        this.estado = true;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", total=" + total +'\'' +
                ", estado=" + estado +
                ", usuario=" + usuario.getId();
    }
}
