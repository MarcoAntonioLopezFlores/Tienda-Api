package mx.edu.utez.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class Subcategoria implements Serializable {

	private static final long serialVersionUID = 1L;
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @Column(unique = true)
    private String nombre;
    
    private String descripcion;
    
    @Lob
    private String imagen;
    private boolean estado;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "subcategoria"
    )
    private List<Producto> productos;

    @ManyToOne(optional = false)
    private Categoria categoria;

    public Subcategoria() {
        super();
    }

    @Override
    public String toString() {
        return  "id=" + id + '\'' +
                ", nombre= '" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estado=" + estado +
                ", categoria=" + categoria.getId();
    }
}
