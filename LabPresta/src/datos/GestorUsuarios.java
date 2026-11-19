package datos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;

public class GestorUsuarios {
    private static final String RUTA_ARCHIVO = "usuarios.csv";

    public static List<Usuario> cargarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty() || linea.startsWith("cedula,")) {
                    continue;
                }
                String[] partes = linea.split(",", -1);
                if (partes.length == 4) {
                    Usuario u = new Usuario(
                        partes[0].trim(),
                        partes[1].trim(),
                        partes[2].trim(),
                        partes[3].trim()
                    );
                    lista.add(u);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer usuarios.csv: " + e.getMessage());
        }
        return lista;
    }

    public static void guardarUsuarios(List<Usuario> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            bw.write("cedula,nombre,tipo,telefono");
            bw.newLine();
            for (Usuario u : lista) {
                bw.write(u.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios.csv: " + e.getMessage());
        }
    }
}