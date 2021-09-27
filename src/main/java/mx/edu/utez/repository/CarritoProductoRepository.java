package mx.edu.utez.repository;

import mx.edu.utez.model.Carrito;
import mx.edu.utez.model.CarritoProducto;
import mx.edu.utez.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoProductoRepository extends JpaRepository<CarritoProducto,Long> {

    CarritoProducto findByCarritoAndProducto(Carrito carrito, Producto producto);

    List<CarritoProducto> findAllByCarrito(Carrito carrito);

}
