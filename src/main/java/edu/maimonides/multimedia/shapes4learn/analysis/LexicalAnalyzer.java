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
 * @author Becerra-Gorino-Moraguez-Pernetta
 * 
 */
public class LexicalAnalyzer implements Interpreter {

	public List<Token> analyze(String code) throws CodeException {

		List<Token> tokens = new LinkedList<>();

		// Defino lexemas para cada token.

		String create = "\\b(create)\\b";
		Pattern PatronCreate = Pattern.compile(create);

		String setColor = "\\b(setcolor)\\b";
		Pattern PatronSetColor = Pattern.compile(setColor);

		String setBase = "\\b(setbase)\\b";
		Pattern PatronSetBase = Pattern.compile(setBase);

		String setHeight = "\\b(setheight)\\b";
		Pattern PatronSetHeight = Pattern.compile(setHeight);

		String setRadius = "\\b(setradius)\\b";
		Pattern PatronSetRadius = Pattern.compile(setRadius);

		String setPosition = "\\b(setposition)\\b";
		Pattern PatronSetPosition = Pattern.compile(setPosition);

		String forma = "\\b(?:shape|rectangle|circle)\\b";
		Pattern PatronForma = Pattern.compile(forma);

		String colorDef = "#[a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9]";
		Pattern PatronColor = Pattern.compile(colorDef);

		String expresionNumerica = "(^[-+]?[0-9]*\\.?[0-9]+$)|(\\()|(\\))|(\\*)|(\\+)|(\\-)|(\\/)";
		Pattern PatronExpresionNumerica = Pattern.compile(expresionNumerica);

		String in = "^in$";
		Pattern PatronIn = Pattern.compile(in);

		String id = "^[a-zA-Z]+$";
		Pattern PatronId = Pattern.compile(id);

		String coma = "^[,]$";
		Pattern PatronComa = Pattern.compile(coma);

		String puntoComa = "^[;]$";
		Pattern PatronPuntoComa = Pattern.compile(puntoComa);

		StringTokenizer stringTokenizer = new StringTokenizer(code);

		String tokenActual = "";
		String tokenFinal = "";

		String tokenPuntoComa = "";

		boolean huboErrores = false;

		boolean lexNoValido = false;

		while (stringTokenizer.hasMoreTokens()) {

			Token token = new Token();

			boolean finDeSentencia = false;

			tokenActual = stringTokenizer.nextToken();

			if (tokenActual.contains(";")) {
				String[] pfinal = StringUtils.split(tokenActual, ";");

				finDeSentencia = true;
				tokenActual = pfinal[0];
				tokenPuntoComa = ";";
				if (pfinal.length > 1) {
					tokenFinal = pfinal[1];
				}
			}

			boolean tokenEncontrado = false;
			token.setValidez(false);

			Matcher matcherCreate = PatronCreate.matcher(tokenActual);
			if (!tokenEncontrado && matcherCreate.matches() && !lexNoValido) {
				System.out.println("Token: Create - Lexema: " + tokenActual);

				token = new Token();
				token.setTipoToken("create");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;
			}

			Matcher matcherSetColor = PatronSetColor.matcher(tokenActual);
			if (!tokenEncontrado && matcherSetColor.matches() && !lexNoValido) {
				System.out.println("Token: SetColor - Lexema: " + tokenActual);

				token = new Token();
				token.setTipoToken("setcolor");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;
			}

			Matcher matcherSetBase = PatronSetBase.matcher(tokenActual);
			if (!tokenEncontrado && matcherSetBase.matches() && !lexNoValido) {
				System.out.println("Token: SetBase - Lexema: " + tokenActual);

				token = new Token();
				token.setTipoToken("setbase");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;
			}

			Matcher matcherSetHeight = PatronSetHeight.matcher(tokenActual);
			if (!tokenEncontrado && matcherSetHeight.matches() && !lexNoValido) {
				System.out.println("Token: SetHeight - Lexema: " + tokenActual);

				token = new Token();
				token.setTipoToken("setheight");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;
			}

			Matcher matcherSetRadius = PatronSetRadius.matcher(tokenActual);
			if (!tokenEncontrado && matcherSetRadius.matches() && !lexNoValido) {
				System.out.println("Token: SetRadius - Lexema: " + tokenActual);

				token = new Token();
				token.setTipoToken("setradius");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;
			}

			Matcher matcherSetPosition = PatronSetPosition.matcher(tokenActual);
			if (!tokenEncontrado && matcherSetPosition.matches()
					&& !lexNoValido) {
				System.out.println("Token: SetPosition - Lexema: "
						+ tokenActual);

				token = new Token();
				token.setTipoToken("setposition");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;
			}

			Matcher matcherForma = PatronForma.matcher(tokenActual);
			if (!tokenEncontrado && matcherForma.matches() && !lexNoValido) {
				System.out.println("Token: Forma - Lexema: " + tokenActual);
				token = new Token();
				token.setTipoToken("forma");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;
			}

			Matcher matcherColor = PatronColor.matcher(tokenActual);
			if (!tokenEncontrado && matcherColor.matches() && !lexNoValido) {
				System.out.println("Token: Color - Lexema: " + tokenActual);
				token = new Token();
				token.setTipoToken("color");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;
			}

			Matcher matcherExpresionNumerica = PatronExpresionNumerica
					.matcher(tokenActual);
			if (!tokenEncontrado && matcherExpresionNumerica.find()
					&& !lexNoValido) {
				System.out.println("Token: Expresión Numérica - Lexema: "
						+ tokenActual);

				token = new Token();
				token.setTipoToken("expresion");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;
			}

			Matcher matcherComa = PatronComa.matcher(tokenActual);
			if (!tokenEncontrado && matcherComa.matches() && !lexNoValido) {
				System.out.println("Token: Coma - Lexema: " + tokenActual);

				token = new Token();
				token.setTipoToken("coma");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;
			}

			Matcher matcherIn = PatronIn.matcher(tokenActual);
			if (!tokenEncontrado && matcherIn.matches() && !lexNoValido) {
				System.out.println("Token: In - Lexema: " + tokenActual);

				token = new Token();
				token.setTipoToken("in");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;
			}

			Matcher matcherId = PatronId.matcher(tokenActual);
			if (!tokenEncontrado && matcherId.matches() && !lexNoValido) {
				System.out.println("Token: ID - Lexema: " + tokenActual);
				token = new Token();
				token.setTipoToken("id");
				token.setLexema(tokenActual);
				token.setValidez(true);
				tokens.add(token);

				tokenEncontrado = true;
			}

			if (finDeSentencia) {
				lexNoValido = true;
			}

			if (!tokenEncontrado) {
				System.out
						.println("'" + tokenActual + "': lexema desconocido.");
				token = new Token();
				token.setLexema(tokenActual);
				token.setTipoToken("lexema desconocido");
				token.setValidez(false);
				tokens.add(token);
				huboErrores = true;
			}

			if (!tokenFinal.equalsIgnoreCase("")) {

				System.out.println("'" + tokenFinal + "': lexema desconocido.");
				token = new Token();
				token.setLexema(tokenFinal);
				token.setTipoToken("lexema desconocido");
				token.setValidez(false);
				tokens.add(token);
				huboErrores = true;
			}

			Matcher matcherPuntoComa = PatronPuntoComa.matcher(tokenPuntoComa);
			if (matcherPuntoComa.matches()) {

				System.out.println("Token: Fin de Sentencia - Lexema: ;");

				token = new Token();
				token.setTipoToken("fin de sentencia");
				token.setLexema(tokenPuntoComa);
				token.setValidez(true);
				tokens.add(token);

				// tokenEncontrado = true;
			}
		}

		if (huboErrores) {
			System.out.println("El analizador léxico ha encontrado errores.");
		}

		/*
		 * Iterator<Token> iterator = tokens.iterator();
		 * 
		 * while (iterator.hasNext()) {
		 * System.out.println(iterator.next().getTipoToken()); }
		 */
		return tokens;
	}

	@Override
	public List<Token> interpret(String code, ShapeAmbient ambient)
			throws CodeException {
		code = code.replace(";", ";\n");
		code = code.replace(",", " , ");
		String[] lines = StringUtils.split(code, "\n");
		List<Token> tokens = new LinkedList<>();

		for (String line : lines) {
			if (!line.trim().equalsIgnoreCase("")) {
				System.out.println("Sentencia a analizar: \n'" + line + "' \n");
				System.out.println("Análisis léxico:");
				tokens.addAll(analyze(line));
				System.out.println("-----------------------------------");
			}
		}
		return tokens;
	}

	@Override
	public void interpret(InputStream stream, ShapeAmbient ambient)
			throws CodeException, IOException {
		this.interpret(IOUtils.toString(stream), ambient);
	}
}
