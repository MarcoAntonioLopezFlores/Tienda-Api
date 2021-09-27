package mx.edu.utez.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.*;

@Data
@Entity
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    private String apellidos;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(unique = true)
    private String correo;
    
    private String password;
    private boolean enabled;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "usuario"
    )
    private List<UsuarioRol> usuarioRoles;
    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "usuario"
    )
    private List<Telefono> telefonos;
    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "usuario"
    )
    private List<DireccionUsuario> direccionUsuarios;
    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "usuario"
    )
    private List<NumeroTarjeta> numeroTarjetas;
    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "usuario"
    )
    private List<Carrito> carritos;
    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "usuario"
    )
    private List<Sesion> sesiones;
    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "usuario"
    )
    private List<Bitacora> bitacoras;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "usuario"
    )
    private List<Compra> compras;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "usuario"
    )
    private List<TokenCorreo> tokenCorreos;

    @PrePersist
    public void prePersist(){
        fechaRegistro = new Date(System.currentTimeMillis());
    }

    public Usuario() {
        super();
    }

    public Usuario(@Size(max = 25, min = 5, message = "Verificar el tamaño del nombre") @NotBlank(message = "Nombre vacío") String nombre, @Size(max = 25, min = 4, message = "Verificar el tamaño del apellido") @NotBlank(message = "Apellido vacío") String apellidos, @Size(max = 25, min = 8, message = "Verificar el tamaño del correo") @NotBlank(message = "Correo vacío") String correo, @Size(max = 100, min = 8, message = "Verificar el tamaño de la contraseña") @NotBlank(message = "Contraseña vacía") String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.password = password;
        this.enabled = true;
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                ", correo='" + correo + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled;
    }
}
