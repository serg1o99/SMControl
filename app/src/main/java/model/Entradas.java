package model;

public class Entradas {
    private String cod_entrada;
    private String cod_prod;
    private String nombre_prod;
    private int stock;
    private String nom_proveedor;
    private String cantidad_entrada;
    private String fecha_ingreso;
    private String dni;

    public String getCod_entrada() {
        return cod_entrada;
    }

    public void setCod_entrada(String cod_entrada) {
        this.cod_entrada = cod_entrada;
    }

    public String getCod_prod() {
        return cod_prod;
    }

    public void setCod_prod(String cod_prod) {
        this.cod_prod = cod_prod;
    }

    public String getNombre_prod() {
        return nombre_prod;
    }

    public void setNombre_prod(String nombre_prod) {
        this.nombre_prod = nombre_prod;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getNom_proveedor() {
        return nom_proveedor;
    }

    public void setNom_proveedor(String nom_proveedor) {
        this.nom_proveedor = nom_proveedor;
    }

    public String getCantidad_entrada() {
        return cantidad_entrada;
    }

    public void setCantidad_entrada(String cantidad_entrada) {
        this.cantidad_entrada = cantidad_entrada;
    }

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
