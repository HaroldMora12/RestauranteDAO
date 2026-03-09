package modelo;

import java.util.Date;

public class Orden {

    private int id;
    private Date fecha;
    private double total;
    private String estado;
    private int idCliente;
    private int idEmpleado;
    private int idMesa;

    public Orden() {
    }

    public Orden(Date fecha, double total, String estado, int idCliente, int idEmpleado, int idMesa) {
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.idMesa = idMesa;
    }

    public Orden(int id, Date fecha, double total, String estado, int idCliente, int idEmpleado, int idMesa) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.idMesa = idMesa;
    }

    // Getters y Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public int getIdMesa() { return idMesa; }
    public void setIdMesa(int idMesa) { this.idMesa = idMesa; }
}

