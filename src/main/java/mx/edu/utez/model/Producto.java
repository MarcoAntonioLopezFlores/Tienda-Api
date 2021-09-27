package mx.edu.utez.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String nombre;
	
	
	private String descripcion;
	
	@Lob
	private String imagen;
	@Column(unique = true)
	private String modelo;
	private double precio;

	private int existencia;
	private String color;

	private boolean estado;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "producto"
    )
    private List<CarritoProducto> carritoProductos;
    @ManyToOne(optional = false)
    private Marca marca;
    @ManyToOne(optional = false)
    private Subcategoria subcategoria;

    @PrePersist
    public void prePersist(){
        fechaRegistro = new Date(System.currentTimeMillis());
    }

    public Producto() {
        super();
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", modelo='" + modelo + '\'' +
                ", precio=" + precio +
                ", existencia=" + existencia +
                ", color='" + color + '\'' +
                ", estado=" + estado +
                ", marca=" + marca.getId() +
                ", subcategoria=" + subcategoria.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Producto product = (Producto) o;

        return id.equals(product.id);
    }


    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
