package datos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import modelo.Equipo;

public class GestorEquipos {
    private static final String RUTA_ARCHIVO = "equipos.csv";

    public static List<Equipo> cargarEquipos() {
        List<Equipo> lista = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty() || linea.startsWith("codigo,")) {
                    continue;
                }
                String[] partes = linea.split(",");
                if (partes.length == 4) {
                    Equipo eq = new Equipo(
                        partes[0].trim(),
                        partes[1].trim(),
                        partes[2].trim(),
                        partes[3].trim()
                    );
                    lista.add(eq);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer equipos.csv: " + e.getMessage());
        }
        return lista;
    }

    public static void guardarEquipos(List<Equipo> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            bw.write("codigo,tipo,descripcion,estado");
            bw.newLine();
            for (Equipo eq : lista) {
                bw.write(eq.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar equipos.csv: " + e.getMessage());
        }
    }
}
