package main.java.Vista;

import com.formdev.flatlaf.FlatLightLaf;
import main.java.Compilacion.LexicalAnalyzer;
import main.java.Compilacion.Parser;
import main.java.Compilacion.SQLExecutor;
import main.java.Compilacion.SemanticAnalyzer;
import java.awt.FlowLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;

public class VistaPrincipal extends javax.swing.JFrame {

    public VistaPrincipal() {
        initComponents();
    }

    private void ejecutarAnalisisSQL(String input) {
        jTextArea1.setText(""); // Limpiar el área de texto antes de mostrar resultados

        java.io.PrintStream consolaOriginal = System.out;
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream ps = new java.io.PrintStream(baos);
        System.setOut(ps);

        try {
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
            executor.execute(selectedCols, parser.getCondition(), parser.getOrderBy(), parser.isAscending(),
                    parser.isDistinct());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.flush();
            System.setOut(consolaOriginal);
            jTextArea1.setText(baos.toString());
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new JPanel();
        Secuencia = new JTextField();
        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        jButton1 = new JButton("Ejecutar Consulta");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Reconocedor SQL - Interfaz Visual");

        jPanel1.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));

        Secuencia.putClientProperty("JTextField.placeholderText", "Escribe tu consulta SQL aquí...");
        Secuencia.putClientProperty("JComponent.roundRect", true);
        Secuencia.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        Secuencia.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        Secuencia.addActionListener(this::SecuenciaActionPerformed);
        jPanel1.add(Secuencia);
        jPanel1.add(Box.createRigidArea(new Dimension(0, 10)));

        jTextArea1.setEditable(false);
        jTextArea1.setFont(new Font("Consolas", Font.PLAIN, 14));
        jTextArea1.setRows(15);
        jScrollPane1.setViewportView(jTextArea1);
        jPanel1.add(jScrollPane1);
        jPanel1.add(Box.createRigidArea(new Dimension(0, 10)));

        jButton1.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jButton1.setBackground(new Color(0, 123, 255));
        jButton1.setForeground(Color.WHITE);
        jButton1.setFocusPainted(false);
        jButton1.setPreferredSize(new Dimension(200, 40));
        jButton1.addActionListener(this::jButton1ActionPerformed);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(jButton1);
        buttonPanel.setOpaque(false);
        jPanel1.add(buttonPanel);

        getContentPane().add(jPanel1);
        setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);
    }

    private void SecuenciaActionPerformed(java.awt.event.ActionEvent evt) {
        // Evento de Enter en el TextField
        ejecutarAnalisisSQL(Secuencia.getText());
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        ejecutarAnalisisSQL(Secuencia.getText());
    }

    public static void main(String args[]) {
        try {
            // UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("No se pudo establecer FlatLaf.");
        }

        java.awt.EventQueue.invokeLater(() -> new VistaPrincipal().setVisible(true));
    }

    // Variables declaration
    private javax.swing.JTextField Secuencia;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
}
