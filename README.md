# LabPresta — Equipo 5

Aplicación de escritorio desarrollada en **Java Swing** (Apache NetBeans) para gestionar el inventario de equipos, el registro de usuarios y el control de préstamos y devoluciones de un laboratorio.

## Integrantes y Roles
* **Shamir Rivera** — Product Owner / Desarrollador (`HU-C1`: Registro y gestión de estado de equipos)
* **Karen Grefa** — Scrum Master / Desarrolladora (`HU-C2`: Registro y búsqueda de usuarios)
* **Adrián Jara** — Desarrollador (`HU-C3`: Registro de préstamos, cálculo de fechas límite y devoluciones)

## Tablero de Trello
* Enlace al tablero del Sprint: https://trello.com/b/qHszdhLE/equipo5labpresta

## Funcionalidades Implementadas

### HU-C1: Gestión de Equipos (Pestaña *Equipos*)
- Registro de equipos con código, tipo (*Laptop, Proyector, Kit Arduino, Otro*) y descripción, asignando estado inicial **Disponible**.
- Validación de campos vacíos, bloqueo de códigos duplicados, conversión automática a mayúsculas y verificación de formato/prefijo según el tipo (`LAP-`, `PROY-`, `ARD-`, `OTR-`).
- Cambio de estado entre **Disponible** y **En mantenimiento** con mensajes de confirmación, impidiendo modificar equipos en estado **Prestado** o repetir el mismo estado actual.

### HU-C2: Gestión de Usuarios (Pestaña *Usuarios*)
- Registro de usuarios con cédula, nombre completo, tipo (*Estudiante* o *Docente*) y teléfono.
- Validación de campos obligatorios, selección de tipo, formato exacto de **10 dígitos numéricos** para cédula y teléfono, y bloqueo de cédulas o teléfonos duplicados.
- Búsqueda y filtrado en tiempo real por cédula o nombre.

### HU-C3: Gestión de Préstamos y Devoluciones (Pestaña *Préstamos*)
- Selección de usuario registrado, equipo y fecha de préstamo editable (`AAAA-MM-DD`).
- Cálculo automático de la fecha límite según el tipo de usuario: **1 día** para *Estudiante* y **3 días** para *Docente*.
- Validación consolidada que impide prestar equipos que no estén **Disponible** o usar fechas anteriores a la actual, cambiando automáticamente el equipo a **Prestado** al registrar el préstamo.
- Registro de devoluciones con fecha editable, marcando el préstamo como *Devuelto* (o con atraso si supera la fecha límite) y regresando el equipo a **Disponible**.

## Requisitos para Ejecutar
- **Java JDK 21 o superior** (proyecto compilado con **JDK 25** en Apache NetBeans).

## Cómo Ejecutar la Aplicación desde el `.jar` (Recomendado)
La carpeta `LabPresta/dist` ya incluye el ejecutable compilado (`LabPresta.jar`) junto con los archivos `.csv` de prueba limpios y sincronizados.

Primero, descargue o clone el repositorio y abra su terminal ubicada en la carpeta raíz del proyecto (`LabPresta-Equipo5`). Luego, ejecute los comandos según la terminal que utilice:

### Opción 1: Símbolo del sistema (CMD en Windows)
```cmd
cd LabPresta\dist
java -jar LabPresta.jar
```

### Opción 2: PowerShell (Windows)
```powershell
cd .\LabPresta\dist
java -jar .\LabPresta.jar
```

### Opción 3: Git Bash (Windows) / Terminal (Linux o macOS)
```bash
cd LabPresta/dist
java -jar LabPresta.jar
```

> **Nota:** Si la terminal muestra el error *"java no se reconoce como un comando"* o *"command not found"*, significa que el JDK no está agregado al `PATH` del sistema. En ese caso, ejecute el `.jar` indicando la ruta completa a su `java.exe` (por ejemplo, en CMD/PowerShell: `"C:\Ruta\De\Su\JDK\bin\java.exe" -jar LabPresta.jar`).

## Persistencia de Datos
La aplicación guarda automáticamente todos los cambios en archivos CSV delimitados por comas (`,`), ubicados en la misma carpeta desde donde se ejecuta el programa:
* `equipos.csv` — Inventario y estado actual de los equipos.
* `usuarios.csv` — Listado de estudiantes y docentes registrados.
* `prestamos.csv` — Historial de préstamos activos y devueltos.

## Estructura del Proyecto
* `LabPresta/src/modelo/` — Clases de entidad (`Equipo.java`, `Usuario.java`, `Prestamo.java`).
* `LabPresta/src/datos/` — Gestores de lectura y escritura CSV (`GestorEquipos.java`, `GestorUsuarios.java`, `GestorPrestamos.java`).
* `LabPresta/src/vista/` — Interfaz gráfica Swing (`VentanaPrincipal.java`, `PanelUsuarios.java`, `PanelPrestamos.java`).
* `LabPresta/dist/` — Archivo ejecutable `LabPresta.jar` y archivos `.csv` de prueba.