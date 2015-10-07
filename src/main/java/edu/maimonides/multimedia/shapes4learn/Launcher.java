package edu.maimonides.multimedia.shapes4learn;

import edu.maimonides.multimedia.shapes4learn.analysis.LexicalAnalyzer;
import edu.maimonides.multimedia.shapes4learn.interpreter.CodeException;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.*;
import java.awt.Component;

/**
 * @author Matias Giorgio
 * 
 */
public class Launcher {

	/**
	 * @param args
	 */

	public static void main(String[] args) {

		JButton boton;

		JFrame frame = new JFrame("Shapes4Learn");
		JPanel panelPrincipal = new JPanel();
		final JTextArea areaTexto = new JTextArea("Ingrese Sentencia...");
		areaTexto.setRows(3);

		panelPrincipal.add(areaTexto, BorderLayout.NORTH);

		boton = new javax.swing.JButton();
		boton.setAlignmentX(Component.CENTER_ALIGNMENT);
		boton.setText("Ejecutar Analizador Léxico:");
		boton.setPreferredSize(new Dimension(200, 50));

		boton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				BotonActionPerformed(evt);
			}

			private void BotonActionPerformed(ActionEvent evt) {
				String texto;
				texto = areaTexto.getText();

				System.out.println(texto);

				LexicalAnalyzer analizadorLexico = new LexicalAnalyzer();
				try {
					analizadorLexico.interpret(texto, null);
				} catch (CodeException e) {
					e.printStackTrace();
				}
			}
		});

		panelPrincipal.add(boton);
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

		frame.getContentPane().add(panelPrincipal);
		frame.setSize(250, 250);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		//frame.pack();

	}
}