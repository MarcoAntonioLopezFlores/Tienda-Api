package mx.edu.utez.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class CarritoProducto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidad;

    private double precio;

    @ManyToOne(optional = false)
    private Carrito carrito;
    @ManyToOne(optional = false)
    private Producto producto;

    public CarritoProducto() {
        super();
    }

    public CarritoProducto(int cantidad, Carrito carrito, Producto producto, double precio) {
        this.cantidad=cantidad;
        this.carrito=carrito;
        this.producto=producto;
        this.precio=precio;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                ", carrito=" + carrito.getId() +
                ", producto=" + producto.getId();
    }

}
