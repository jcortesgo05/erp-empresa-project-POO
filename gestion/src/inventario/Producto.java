package inventario;
// define que atributos o datos tiene cada articulo o producto de la tienda
public class Producto {

    private String id;
    private String nombre;
    private int stock;
    private double precio;

    public Producto(String id, String nombre, int stock, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.precio = precio;
    }


}
