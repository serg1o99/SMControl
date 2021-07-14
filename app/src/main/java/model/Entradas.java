package model;

public class Entradas {
    private String cod_entrada;
    private String cod_prod;
    private String nombre_prod;
    private String proveedor;
    private int cantidad_entrante;
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


    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public int getCantidad_entrante() {
        return cantidad_entrante;
    }

    public void setCantidad_entrante(int cantidad_entrante) {
        this.cantidad_entrante = cantidad_entrante;
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
