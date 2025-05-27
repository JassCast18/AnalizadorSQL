import java.io.*;
import java.util.*;

public class SemanticAnalyzer {
    private String tableName;
    private List<String> columns;
    private String condition;
    private String orderBy;

    private List<String> tableColumns;

    public SemanticAnalyzer(Parser parser) {
        this.tableName = parser.getTableName();
        this.columns = parser.getColumns();
        this.condition = parser.getCondition();
        this.orderBy = parser.getOrderBy();
    }

    public boolean validate() {
        // Verificar si la tabla existe (archivo)
        File file = new File(
                "A:/caste/Documents/Jaser/Tareas UMG/7mo Semestre/CompiladoresProyecto/ProyectCompiladores/src/empleados.txt");

        if (!file.exists()) {
            System.out.println("❌ Error semántico: Tabla '" + tableName + "' no existe (archivo no encontrado)");
            return false;
        }

        // Leer encabezado de columnas
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            if (header == null) {
                System.out.println("❌ Error semántico: El archivo está vacío.");
                return false;
            }

            tableColumns = Arrays.asList(header.split(","));

            // Verificar columnas seleccionadas
            if (!columns.get(0).equals("*")) {
                for (String col : columns) {
                    if (!tableColumns.contains(col)) {
                        System.out.println("❌ Error semántico: Columna '" + col + "' no existe.");
                        return false;
                    }
                }
            }

            // Verificar columna de condición WHERE
            String condCol = condition.split(" ")[0];
            if (!tableColumns.contains(condCol)) {
                System.out.println("❌ Error semántico: Columna en condición '" + condCol + "' no existe.");
                return false;
            }

            // Verificar columna de ORDER BY
            if (!tableColumns.contains(orderBy)) {
                System.out.println("❌ Error semántico: Columna en ORDER BY '" + orderBy + "' no existe.");
                return false;
            }

            System.out.println("✔️ Semántica válida.");
            return true;

        } catch (IOException e) {
            System.out.println("❌ Error al leer el archivo: " + e.getMessage());
            return false;
        }
    }

    // Devuelve la ruta del archivo para el siguiente paso
    public String getDataFilePath() {
        return tableName + ".txt";
    }
}
