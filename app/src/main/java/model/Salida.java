package model;

public class Salida {

    private String dni;
    private String cod_salida;
    private String cod_prod;
    private String nombre_prod;
    private String fecha_salida;
    private String descripcion;
    private int cantidad_salida;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCod_salida() {
        return cod_salida;
    }

    public void setCod_salida(String cod_salida) {
        this.cod_salida = cod_salida;
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

    public int getCantidad_salida() {
        return cantidad_salida;
    }

    public void setCantidad_salida(int cantidad_salida) {
        this.cantidad_salida = cantidad_salida;
    }

    public String getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(String fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
