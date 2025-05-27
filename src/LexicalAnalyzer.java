import java.util.*;
import java.util.regex.*;

public class LexicalAnalyzer {
    private String input;
    private List<Token> tokens = new ArrayList<>();

    // Palabras clave válidas
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
            "SELECT", "FROM", "WHERE", "ORDER", "BY", "ASC", "DESC", "ALL", "DISTINCT"));

    // Operadores
    private static final Set<String> OPERATORS = new HashSet<>(Arrays.asList(
            "=", "<", ">", "<=", ">=", "<>", "!="));

    public LexicalAnalyzer(String input) {
        this.input = input;
    }

    public boolean tokenize() {
        // Preprocesar operadores de dos caracteres para que queden como tokens únicos
        String tmp = input;
        String[] multiOps = { ">=", "<=", "<>", "!=", "==" };
        for (String op : multiOps) {
            tmp = tmp.replace(op, " " + op + " ");
        }
        // Ahora separar por espacios y símbolos
        String[] parts = tmp.split("\\s+|(?=\\W)|(?<=\\W)");
        for (String part : parts) {
            if (part.isBlank())
                continue;
            Token token = identifyToken(part);
            if (token.getType() == Token.Type.ERROR) {
                System.out.println("Error léxico: Token no reconocido -> " + part);
                return false;
            }
            tokens.add(token);
        }

        // Mostrar tokens encontrados
        System.out.println("Tokens encontrados:");
        for (Token t : tokens) {
            System.out.println(t);
        }
        return true;
    }

    private Token identifyToken(String part) {
        if (KEYWORDS.contains(part.toUpperCase())) {
            return new Token(Token.Type.KEYWORD, part.toUpperCase());
        } else if (OPERATORS.contains(part)) {
            return new Token(Token.Type.OPERATOR, part);
        } else if (part.equals(",")) {
            return new Token(Token.Type.COMMA, part);
        } else if (part.equals("*")) {
            return new Token(Token.Type.ASTERISK, part);
        } else if (part.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            return new Token(Token.Type.IDENTIFIER, part);
        } else if (part.matches("\\d+")) {
            return new Token(Token.Type.NUMBER, part);
        } else {
            return new Token(Token.Type.ERROR, part);
        }
    }

    public List<Token> getTokens() {
        return tokens;
    }
}