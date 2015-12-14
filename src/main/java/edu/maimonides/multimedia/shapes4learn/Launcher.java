package edu.maimonides.multimedia.shapes4learn;

import edu.maimonides.multimedia.shapes4learn.analysis.LexicalAnalyzer;
import edu.maimonides.multimedia.shapes4learn.analysis.SemanticAnalyzer;
import edu.maimonides.multimedia.shapes4learn.analysis.SemanticException;
import edu.maimonides.multimedia.shapes4learn.analysis.SyntacticAnalyzer;
import edu.maimonides.multimedia.shapes4learn.analysis.SyntacticException;
import edu.maimonides.multimedia.shapes4learn.interpreter.CodeException;
import edu.maimonides.multimedia.shapes4learn.model.AST;
import edu.maimonides.multimedia.shapes4learn.model.Token;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * @author Matias Giorgio
 * @author Becerra-Gorino-Moraguez-Pernetta
 * 
 */
public class Launcher {

	public static boolean continuo = true;

	public static void main(String[] args) {

		JButton boton;

		JFrame ventana = new JFrame("Shapes4Learn");
		ventana.setTitle("Analizador Léxico/Sintáctico/Semántico");

		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setBackground(Color.GRAY);

		final JTextArea areaTexto = new JTextArea("Ingrese Sentencia aquí...");
		areaTexto.setLineWrap(true);
		areaTexto.setBackground(Color.BLACK);
		areaTexto.setForeground(Color.WHITE);
		areaTexto.setFont(new Font("Times New Roman", Font.PLAIN, 19));

		areaTexto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				areaTexto.setText("");
			}
		});

		areaTexto.setRows(3);

		panelPrincipal.add(areaTexto, BorderLayout.NORTH);

		boton = new javax.swing.JButton();
		boton.setBackground(Color.WHITE);
		boton.setFont(new Font("Times New Roman", Font.BOLD, 16));
		boton.setAlignmentX(Component.CENTER_ALIGNMENT);
		boton.setText("Ejecutar Analizador");
		boton.setPreferredSize(new Dimension(200, 50));

		boton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				BotonActionPerformed(evt);
			}

			private void BotonActionPerformed(ActionEvent evt) {
				String texto;
				texto = areaTexto.getText();
				List<Token> listaTokens = new LinkedList<>();

				LexicalAnalyzer analizadorLexico = new LexicalAnalyzer();
				SyntacticAnalyzer analizadorSintactico = new SyntacticAnalyzer();
				SemanticAnalyzer analizadorSemantico = new SemanticAnalyzer();
				AST ast = new AST();

				try {
					listaTokens.addAll(analizadorLexico.interpret(texto, null));
					try {
						ast = analizadorSintactico.analyze(listaTokens);
						if (!ast.listChildren().isEmpty()) {
							analizadorSemantico.analyze(ast);
						}
					} catch (SyntacticException e) {
						e.printStackTrace();
					} catch (SemanticException e) {
						e.printStackTrace();
					}
				} catch (CodeException e) {
					e.printStackTrace();
				}
			}
		});

		panelPrincipal.add(boton);
		panelPrincipal
				.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

		ventana.getContentPane().add(panelPrincipal);
		ventana.setSize(360, 260);
		ventana.setVisible(true);
		ventana.setLocationRelativeTo(null);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}