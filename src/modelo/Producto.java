package modelo;

public class Producto {

    private int id;
    private String nombre;
    private String categoria;
    private double precio;
    private int disponibilidad;

    public Producto(int id, String nombre, String categoria, double precio, int disponibilidad) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.disponibilidad = disponibilidad;
    }

    public Producto(String nombre, String categoria, double precio, int disponibilidad) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.disponibilidad = disponibilidad;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public double getPrecio() { return precio; }
    public int getDisponibilidad() { return disponibilidad; }
    @Override
    public String toString() {
        return nombre;
    }

}

