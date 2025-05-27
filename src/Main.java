public class Main {
    public static void main(String[] args) {
        String input = "SELECT nombre, edad FROM empleados WHERE edad > 30 ORDER BY nombre ASC";

        LexicalAnalyzer lexer = new LexicalAnalyzer(input);
        if (!lexer.tokenize())
            return;

        Parser parser = new Parser(lexer.getTokens());
        if (!parser.parse())
            return;

        SemanticAnalyzer semantic = new SemanticAnalyzer(parser);
        if (!semantic.validate())
            return;

        SQLExecutor executor = new SQLExecutor(semantic.getDataFilePath());
        String selectedCols = String.join(",", parser.getColumns());
        executor.execute(selectedCols, parser.getCondition(), parser.getOrderBy(), parser.isAscending());
    }
}
