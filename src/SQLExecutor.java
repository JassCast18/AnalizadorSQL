import java.io.*;
import java.util.*;

public class SQLExecutor {
    private String filePath;

    public SQLExecutor(String filePath) {
        this.filePath = filePath;
    }

    public void execute(String selectedCols, String condition, String orderBy, boolean ascending) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.out.println("❌ El archivo está vacío.");
                return;
            }

            String[] headers = headerLine.split(",");
            List<Map<String, String>> records = new ArrayList<>();

            // Leer todos los registros
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.trim().split("[,\\s]+");

                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    row.put(headers[i], values[i]);
                }
                records.add(row);
            }

            // Aplicar condición WHERE
            String[] condParts = condition.split(" ");
            String condCol = condParts[0];
            String operator = condParts[1];
            String condVal = condParts[2];

            List<Map<String, String>> filtered = new ArrayList<>();
            for (Map<String, String> row : records) {
                String value = row.get(condCol);
                if (compare(value, condVal, operator)) {
                    filtered.add(row);
                }
            }

            // Ordenar
            filtered.sort((a, b) -> {
                String val1 = a.get(orderBy);
                String val2 = b.get(orderBy);
                int cmp;
                try {
                    cmp = Integer.compare(Integer.parseInt(val1), Integer.parseInt(val2));
                } catch (NumberFormatException e) {
                    cmp = val1.compareTo(val2);
                }
                return ascending ? cmp : -cmp;
            });

            // Mostrar columnas
            String[] colList = selectedCols.equals("*") ? headers : selectedCols.split(",");

            // Imprimir resultados
            System.out.println("\n📊 Resultados:");
            for (String col : colList)
                System.out.print(col + "\t");
            System.out.println();

            for (Map<String, String> row : filtered) {
                for (String col : colList) {
                    System.out.print(row.get(col) + "\t");
                }
                System.out.println();
            }

        } catch (IOException e) {
            System.out.println("❌ Error al leer archivo: " + e.getMessage());
        }
    }

    private boolean compare(String val1, String val2, String op) {
        try {
            int a = Integer.parseInt(val1);
            int b = Integer.parseInt(val2);

            return switch (op) {
                case ">" -> a > b;
                case "<" -> a < b;
                case ">=" -> a >= b;
                case "<=" -> a <= b;
                case "=" -> a == b;
                case "!=" -> a != b;
                default -> false;
            };
        } catch (NumberFormatException e) {
            return val1.equals(val2);
        }
    }
}
