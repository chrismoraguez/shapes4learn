package edu.maimonides.multimedia.shapes4learn;

import edu.maimonides.multimedia.shapes4learn.analysis.LexicalAnalyzer;
import edu.maimonides.multimedia.shapes4learn.interpreter.CodeException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * @author Becerra-Gorino-Moraguez-Pernetta
 * 
 */
public class Launcher {

	/**
	 * @param args
	 */

	public static void main(String[] args) {

		JButton boton;

		JFrame ventana = new JFrame("Shapes4Learn");
		ventana.setTitle("Analizador Léxico");

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
		panelPrincipal
				.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

		ventana.getContentPane().add(panelPrincipal);
		ventana.setSize(360, 260);
		ventana.setVisible(true);
		ventana.setLocationRelativeTo(null);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}