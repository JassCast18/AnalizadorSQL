package main.java.Principal;

import main.java.Vista.VistaPrincipal;

public class Main {
    public static void main(String[] args) {
        VistaPrincipal view = new VistaPrincipal();
        view.setVisible(true);
        /*
         * String input = "SELECT * FROM estudiantes";
         * 
         * LexicalAnalyzer lexer = new LexicalAnalyzer(input);
         * if (!lexer.tokenize()) // Enviar al analizador léxico
         * return;
         * 
         * Parser parser = new Parser(lexer.getTokens());
         * if (!parser.parse()) // Enviar al analizador sintáctico
         * return;
         * 
         * SemanticAnalyzer semantic = new SemanticAnalyzer(parser);
         * if (!semantic.validate()) // Enviar al analizador semántico
         * return;
         * 
         * SQLExecutor executor = new SQLExecutor(semantic.getDataFilePath()); // Enviar
         * al ejecutor SQL (compilador)
         * String selectedCols = String.join(",", parser.getColumns());
         * executor.execute(selectedCols, parser.getCondition(), parser.getOrderBy(),
         * parser.isAscending(),
         * parser.isDistinct());
         */
    }
}
