public class Main {
    public static void main(String[] args) {
        String input = "SELECT NOMBRE,EDAD FROM estudiantes WHERE EDAD>10 ORDER BY NOMBRE DESC";

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
