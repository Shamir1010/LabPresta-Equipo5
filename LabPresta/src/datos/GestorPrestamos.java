package datos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Prestamo;

public class GestorPrestamos {
    private static final String RUTA_ARCHIVO = "prestamos.csv";

    public static List<Prestamo> cargarPrestamos() {
        List<Prestamo> lista = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty() || linea.startsWith("codigoEquipo,")) {
                    continue;
                }
                String[] partes = linea.split(",", -1);
                if (partes.length == 5) {
                    String codigoEquipo = partes[0].trim();
                    String cedulaUsuario = partes[1].trim();
                    LocalDate fechaPrestamo = LocalDate.parse(partes[2].trim());
                    LocalDate fechaLimite = LocalDate.parse(partes[3].trim());
                    LocalDate fechaDevolucion = "null".equalsIgnoreCase(partes[4].trim()) || partes[4].trim().isEmpty()
                            ? null
                            : LocalDate.parse(partes[4].trim());

                    Prestamo p = new Prestamo(codigoEquipo, cedulaUsuario, fechaPrestamo, fechaLimite, fechaDevolucion);
                    lista.add(p);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer prestamos.csv: " + e.getMessage());
        }
        return lista;
    }

    public static void guardarPrestamos(List<Prestamo> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            bw.write("codigoEquipo,cedulaUsuario,fechaPrestamo,fechaLimite,fechaDevolucion");
            bw.newLine();
            for (Prestamo p : lista) {
                bw.write(p.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar prestamos.csv: " + e.getMessage());
        }
    }
}