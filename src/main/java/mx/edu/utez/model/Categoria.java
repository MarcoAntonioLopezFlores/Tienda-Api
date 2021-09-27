package mx.edu.utez.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class Categoria implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(unique = true)
    private String nombre;
    
    private String descripcion;
    private boolean estado;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "categoria"
    )
    private List<Subcategoria> subcategorias;

    public Categoria() {
        super();
    }

    @Override
    public String toString() {
        return  "id=" + id +'\'' +
                ", nombre= '" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estado= '" + estado + '\'';
    }
}
