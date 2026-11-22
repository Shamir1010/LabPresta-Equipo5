package modelo;

import java.time.LocalDate;

public class Prestamo {
    private String codigoEquipo;
    private String cedulaUsuario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucion;

    public Prestamo(String codigoEquipo, String cedulaUsuario, String tipoUsuario, LocalDate fechaPrestamo) {
        this.codigoEquipo = codigoEquipo;
        this.cedulaUsuario = cedulaUsuario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite = calcularFechaLimite(fechaPrestamo, tipoUsuario);
        this.fechaDevolucion = null;
    }

    public Prestamo(String codigoEquipo, String cedulaUsuario, LocalDate fechaPrestamo, LocalDate fechaLimite, LocalDate fechaDevolucion) {
        this.codigoEquipo = codigoEquipo;
        this.cedulaUsuario = cedulaUsuario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite = fechaLimite;
        this.fechaDevolucion = fechaDevolucion;
    }

    // Regla de negocio: 1 día para Estudiante, 3 días para Docente
    public static LocalDate calcularFechaLimite(LocalDate fechaPrestamo, String tipoUsuario) {
        if ("Docente".equalsIgnoreCase(tipoUsuario)) {
            return fechaPrestamo.plusDays(3);
        }
        return fechaPrestamo.plusDays(1);
    }

    public boolean estaActivo() {
        return fechaDevolucion == null;
    }

    public boolean estaVencido(LocalDate fechaReferencia) {
        return estaActivo() && fechaReferencia.isAfter(fechaLimite);
    }

    public boolean estaAtrasado() {
        return !estaActivo() && fechaDevolucion.isAfter(fechaLimite);
    }

    public String getCodigoEquipo() {
        return codigoEquipo;
    }

    public void setCodigoEquipo(String codigoEquipo) {
        this.codigoEquipo = codigoEquipo;
    }

    public String getCedulaUsuario() {
        return cedulaUsuario;
    }

    public void setCedulaUsuario(String cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String toCSV() {
        String devStr = (fechaDevolucion != null) ? fechaDevolucion.toString() : "null";
        return codigoEquipo + "," + cedulaUsuario + "," + fechaPrestamo.toString() + "," + fechaLimite.toString() + "," + devStr;
    }
}