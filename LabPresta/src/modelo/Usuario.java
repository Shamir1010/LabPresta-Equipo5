package modelo;

public class Usuario {
    private String cedula;
    private String nombre;
    private String tipo;      
    private String telefono;

    public Usuario(String cedula, String nombre, String tipo, String telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.tipo = tipo;
        this.telefono = telefono;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {

        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String toCSV() {
        return cedula + "," + nombre + "," + tipo + "," + telefono;
    }
}