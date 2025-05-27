import java.util.*;

public class Parser {
    private List<Token> tokens;
    private int index = 0;
    private String tableName;
    private List<String> columns = new ArrayList<>();
    private String condition = "";
    private String orderBy = "";
    private boolean ascending = true;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public boolean parse() {
        if (!match(Token.Type.KEYWORD, "SELECT"))
            return error("Falta SELECT");

        // ALL o DISTINCT (opcional)
        if (match(Token.Type.KEYWORD, "ALL") || match(Token.Type.KEYWORD, "DISTINCT")) {
            // aceptado pero no se usa en esta versión
        }

        // * o lista de columnas
        if (match(Token.Type.ASTERISK)) {
            columns.add("*");
        } else {
            if (!parseColumnList())
                return false;
        }

        if (!match(Token.Type.KEYWORD, "FROM"))
            return error("Falta FROM");
        if (!match(Token.Type.IDENTIFIER))
            return error("Falta nombre de tabla");

        tableName = previous().getValue();

        if (!match(Token.Type.KEYWORD, "WHERE"))
            return error("Falta WHERE");

        if (!parseCondition())
            return false;

        if (!match(Token.Type.KEYWORD, "ORDER"))
            return error("Falta ORDER");
        if (!match(Token.Type.KEYWORD, "BY"))
            return error("Falta BY");

        if (!match(Token.Type.IDENTIFIER))
            return error("Falta columna para ORDER BY");
        orderBy = previous().getValue();

        if (match(Token.Type.KEYWORD, "DESC"))
            ascending = false;
        else
            match(Token.Type.KEYWORD, "ASC"); // opcional

        System.out.println("✔️ Sintaxis válida.");
        return true;
    }

    private boolean parseColumnList() {
        while (true) {
            if (!match(Token.Type.IDENTIFIER))
                return error("Se esperaba nombre de columna");
            columns.add(previous().getValue());

            if (!match(Token.Type.COMMA))
                break;
        }
        return true;
    }

    private boolean parseCondition() {
        if (!match(Token.Type.IDENTIFIER))
            return error("Falta campo en la condición WHERE");
        condition += previous().getValue() + " ";

        if (!match(Token.Type.OPERATOR))
            return error("Falta operador en la condición");
        condition += previous().getValue() + " ";

        if (!match(Token.Type.IDENTIFIER) && !match(Token.Type.NUMBER)) {
            return error("Falta valor en la condición");
        }
        condition += previous().getValue();

        return true;
    }

    private boolean match(Token.Type type) {
        if (index >= tokens.size())
            return false;
        if (tokens.get(index).getType() == type) {
            index++;
            return true;
        }
        return false;
    }

    private boolean match(Token.Type type, String value) {
        if (index >= tokens.size())
            return false;
        Token t = tokens.get(index);
        if (t.getType() == type && t.getValue().equalsIgnoreCase(value)) {
            index++;
            return true;
        }
        return false;
    }

    private Token previous() {
        return tokens.get(index - 1);
    }

    private boolean error(String message) {
        System.out.println("❌ Error de sintaxis: " + message);
        return false;
    }

    // Métodos públicos para uso posterior
    public String getTableName() {
        return tableName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public String getCondition() {
        return condition;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public boolean isAscending() {
        return ascending;
    }
}
