package edu.maimonides.multimedia.shapes4learn.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import edu.maimonides.multimedia.shapes4learn.interpreter.CodeException;
import edu.maimonides.multimedia.shapes4learn.interpreter.Interpreter;
import edu.maimonides.multimedia.shapes4learn.model.ShapeAmbient;
import edu.maimonides.multimedia.shapes4learn.model.Token;

/**
 * This class is responsible for the first part of the interpreter: lexical
 * analysis. It will be implemented by the students to perform the proper
 * operations.
 * 
 * @author Matias Giorgio
 * 
 */
public class LexicalAnalyzer implements Interpreter {

	public List<Token> analyze(String code) throws LexicalException {

		List<Token> tokens = new LinkedList<>();

		Token token = new Token();

		// Defino lexemas para cada token.

		String create = "\\b(create)\\b";
		Pattern PatronCreate = Pattern.compile(create);

		String forma = "\\b(?:shape|rectangle|circle)\\b";
		Pattern PatronForma = Pattern.compile(forma);

		String id = "^[a-zA-Z]+$";
		Pattern PatronId = Pattern.compile(id);

		String puntoComa = "^[;]$";
		Pattern PatronPuntoComa = Pattern.compile(puntoComa);

		/*
		 * // Seteamos los próximos a probar String setPosition =
		 * "\\b(setposition)\\b"; Pattern PatronSetPosition =
		 * Pattern.compile(setPosition);
		 * 
		 * String setColor = "\\b(setcolor)\\b"; Pattern PatronSetColor =
		 * Pattern.compile(setColor);
		 * 
		 * String setBase = "\\b(setbase)\\b"; Pattern PatronSetBase =
		 * Pattern.compile(setBase);
		 * 
		 * String setHeight = "\\b(setheight)\\b"; Pattern PatronSetHeight =
		 * Pattern.compile(setHeight);
		 * 
		 * String setRadius = "\\b(setradius)\\b"; Pattern PatronSetRadius =
		 * Pattern.compile(setRadius);
		 * 
		 * String colorDef =
		 * "#[a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9]"
		 * ; Pattern PatronColor = Pattern.compile(colorDef);
		 * 
		 * String expresionNumerica =
		 * "(^[-+]?[0-9]*\\.?[0-9]+$)|(\\()|(\\))|(\\*)|(\\+)|(\\-)|(\\/)";
		 * Pattern PatronNumericExpression = Pattern.compile(expresionNumerica);
		 */

		StringTokenizer stringTokenizer = new StringTokenizer(code);

		String tokenActual = "";

		boolean huboErrores = false;

		while (stringTokenizer.hasMoreTokens()) {

			// create circle circulo;
			tokenActual = stringTokenizer.nextToken();

			boolean tokenEncontrado = false;
			token.setValidez(false);

			Matcher matcherCreate = PatronCreate.matcher(tokenActual);

			if (!tokenEncontrado && matcherCreate.matches()) {
				System.out.println("Token: create - Lexema: " + tokenActual);

				token.setTipoToken("create");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;

			}

			Matcher matcherForma = PatronForma.matcher(tokenActual);
			if (!tokenEncontrado && matcherForma.matches()) {
				System.out.println("Token: forma - Lexema: " + tokenActual);

				token.setTipoToken("forma");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;

			}

			Matcher matcherId = PatronId.matcher(tokenActual);
			if (!tokenEncontrado && matcherId.matches()) {
				System.out.println("Token: id - Lexema: " + tokenActual);

				token.setTipoToken("id");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;

			}

			Matcher matcherPuntoComa = PatronPuntoComa.matcher(tokenActual);
			if (!tokenEncontrado && matcherPuntoComa.matches()) {
				System.out.println("Token: Fin de sentencia - Lexema: "
						+ tokenActual);

				token.setTipoToken("Fin de sentencia");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;

			}

			if (tokenEncontrado) {
				System.out
						.println("'" + tokenActual + "': lexema desconocido.");
				huboErrores = true;
			}
		}

		if (huboErrores) {
			System.out.println("El analizador léxico ha encontrado errores.");
		}

		return tokens;
	}

	@Override
	public void interpret(String code, ShapeAmbient ambient)
			throws CodeException, LexicalException {
		String[] lines = StringUtils.split(code, "\n");
		List<Token> tokens = new LinkedList<>();

		for (String line : lines) {
			tokens.addAll(analyze(line));
		}
		
		
		
		
	}

	@Override
	public void interpret(InputStream stream, ShapeAmbient ambient)
			throws CodeException, IOException, LexicalException {
		this.interpret(IOUtils.toString(stream), ambient);
	}
}
