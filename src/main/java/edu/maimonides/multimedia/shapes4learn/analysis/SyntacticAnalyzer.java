package edu.maimonides.multimedia.shapes4learn.analysis;

import java.util.ArrayList;
import java.util.List;
import edu.maimonides.multimedia.shapes4learn.model.AST;
import edu.maimonides.multimedia.shapes4learn.model.Token;

/**
 * This class is responsible for the second part of the interpreter: syntactic
 * analysis. It will be implemented by the students to perform the proper
 * operations.
 * 
 * @author Matias Giorgio
 * 
 */
public class SyntacticAnalyzer {

	public SyntacticAnalyzer() {
	}

	public String tipoToken;
	public Integer lineNumber = 1;
	public AST nodoPrincipal = new AST();
	public AST astCreate = new AST();
	public AST astForma = new AST();
	public AST astID = new AST();
	public AST astSetColor = new AST();
	public AST astColor = new AST();
	public AST astSetBase = new AST();
	public AST astExpresion = new AST();
	public AST astSetHeight = new AST();
	public AST astSetRadius = new AST();
	public AST astSetPosition = new AST();
	public AST astExpresion2 = new AST();

	public AST analyze(List<Token> tokens) throws SyntacticException {

		List<Token> sentenceGrammar = new ArrayList<>();

		System.out.println("-----------------------------------");
		System.out.println("Análisis Sintáctico: \n");

		for (Token token : tokens) {
			tipoToken = token.getTipoToken();

			// Se valida que el token no indique fin de sentencia
			if (!tipoToken.equals("fin de sentencia")) {
				// No es fin de sentencia, se va reconstruyendo la sentencia
				sentenceGrammar.add(token);
			} else {
				// Es fin de sentencia, se cierra la sentencia y se manda a
				// analizar sintacticamente
				sentenceGrammar.add(token);
				checkSentence(sentenceGrammar, lineNumber);
				sentenceGrammar = new ArrayList<>();
				lineNumber++;
			}
		}

		System.out.println("Cantidad de hijos: "
				+ nodoPrincipal.listChildren().size());

		for (int i = 0; i < nodoPrincipal.listChildren().size(); i++) {

			if (nodoPrincipal.getChild(i).getTipoToken().equals("setposition")) {
				String nodoPadre = nodoPrincipal.getChild(i).getTipoToken();
				String nodoHijo1 = nodoPrincipal.getChild(i).getChild(0)
						.getTipoToken();
				String nodoHijo2 = nodoPrincipal.getChild(i).getChild(1)
						.getTipoToken();
				String nodoHijo3 = nodoPrincipal.getChild(i).getChild(2)
						.getTipoToken();

				System.out.printf("Representación árbol (izq-der): \n");

				System.out.printf("- Nodo Padre: %s\n", nodoPadre);
				System.out.printf("- 1er Hijo: %s ", nodoHijo1);
				System.out.printf("- 2do Hijo: %s ", nodoHijo2);
				System.out.printf("- 3er Hijo: %s \n", nodoHijo3);

			} else {
				String nodoPadre = nodoPrincipal.getChild(i).getTipoToken();
				String nodoHijo1 = nodoPrincipal.getChild(i).getChild(0)
						.getTipoToken();
				String nodoHijo2 = nodoPrincipal.getChild(i).getChild(1)
						.getTipoToken();

				System.out.printf("Representación árbol (izq-der): \n");

				System.out.printf("- Nodo Padre: %s\n", nodoPadre);
				System.out.printf("- 1er Hijo: %s ", nodoHijo1);
				System.out.printf("- 2do Hijo: %s \n", nodoHijo2);
			}
		}

		return nodoPrincipal;
	}

	private void checkSentence(List<Token> sentences, Integer lineNumber) {
		// Se analiza sintácticamente cada sentencia recibida
		String lookahead = sentences.get(0).getTipoToken();
		Integer wordNumber = 0;
		Boolean validate = false;

		for (Token word : sentences) {
			wordNumber++;

			// Dependiendo la acción ingresada por el usuario se valida su
			// sintaxis
			switch (lookahead) {
			case "create":
				validate = checkOrderCreate(word, wordNumber, lineNumber);
				break;
			case "setcolor":
				validate = checkOrderSetColor(word, wordNumber, lineNumber);
				break;
			case "setbase":
				validate = checkOrderSetBase(word, wordNumber, lineNumber);
				break;
			case "setheight":
				validate = checkOrderSetHeight(word, wordNumber, lineNumber);
				break;
			case "setradius":
				validate = checkOrderSetRadius(word, wordNumber, lineNumber);
				break;
			case "setposition":
				validate = checkOrderSetPosition(word, wordNumber, lineNumber);
				break;
			default:
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out
						.println("Error: Se esperaba definición de acción create | setcolor | setbase | setheight | setradius");
				System.out.println();
			}

			if (!validate) {
				// Se detecto algun error en la sentencia, se finaliza el
				// analisis sintactico de esa sentencia
				break;
			}
		}

		// Dependiendo la accion ingresada por el usuario se valida que la
		// cantidad de comandos sea la correcta
		if (validate) {
			checkCommands(lookahead, wordNumber);
		}
	}

	private boolean checkOrderSetPosition(Token word, Integer wordNumber,
			Integer lineNumber) {
		// Debe ser SETPOSITION
		if (wordNumber == 1) {
			if (word.getTipoToken().equals("setposition")) {

				astSetPosition.setTipoToken(word.getLexema());
				astSetPosition.setLineNumber(lineNumber);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println();
				return false;
			}
		}

		// Debe ser EXPRESSION
		if (wordNumber == 2) {
			if (word.getTipoToken().equals("expresion")) {

				astExpresion.setTipoToken(word.getLexema());
				astExpresion.setLineNumber(lineNumber);
				astSetPosition.addChild(astExpresion);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out
						.println("Error: Se esperaba palabra del tipo EXPRESSION");
				System.out.println();
				return false;
			}
		}

		// Debe ser COMA
		if (wordNumber == 3) {
			if (word.getTipoToken().equals("coma")) {
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba símbolo del tipo COMA");
				System.out.println();
				return false;
			}
		}

		// Debe ser EXPRESSION
		if (wordNumber == 4) {
			if (word.getTipoToken().equals("expresion")) {

				astExpresion2.setTipoToken(word.getLexema());
				astExpresion2.setLineNumber(lineNumber);
				astSetPosition.addChild(astExpresion2);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out
						.println("Error: Se esperaba palabra del tipo EXPRESSION");
				System.out.println();
				return false;
			}
		}
		// Debe ser IN
		if (wordNumber == 5) {
			if (word.getTipoToken().equals("in")) {
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo IN");
				System.out.println();
				return false;
			}
		}

		// Debe ser SHAPE
		if (wordNumber == 6) {
			if (word.getTipoToken().equals("forma")) {
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo SHAPE");
				System.out.println();
				return false;
			}
		}

		// Debe ser ID
		if (wordNumber == 7) {
			if (word.getTipoToken().equals("id")) {

				astID.setTipoToken(word.getLexema());
				astID.setLineNumber(lineNumber);
				astSetPosition.addChild(astID);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo ID");
				System.out.println();
				return false;
			}
		}

		// Debe ser FIN DE SENTENCIA
		if (wordNumber == 8) {
			if (word.getTipoToken().equals("fin de sentencia")) {

				nodoPrincipal.addChild(astSetPosition);
				String nodoPadre = nodoPrincipal.getChild(5).getTipoToken();
				String nodoHijo1 = nodoPrincipal.getChild(5).getChild(0)
						.getTipoToken();
				String nodoHijo2 = nodoPrincipal.getChild(5).getChild(1)
						.getTipoToken();
				String nodoHijo3 = nodoPrincipal.getChild(5).getChild(2)
						.getTipoToken();

				System.out
						.printf("Representación árbol (izq-der) de la función SetPosition: \n");

				System.out.printf("- Nodo Padre: %s\n", nodoPadre);
				System.out.printf("- 1er Hijo: %s ", nodoHijo1);
				System.out.printf("- 2do Hijo: %s ", nodoHijo2);
				System.out.printf("- 3er Hijo: %s\n\n", nodoHijo3);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba fin de sentencia ';'");
				System.out.println();
				return false;
			}
		}
		return false;
	}

	private boolean checkOrderCreate(Token word, Integer wordNumber,
			Integer lineNumber) {

		// Debe ser CREATE
		if (wordNumber == 1) {
			if (word.getTipoToken().equals("create")) {

				astCreate.setTipoToken(word.getLexema());
				astCreate.setLineNumber(lineNumber);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				return false;
			}
		}

		// Debe ser SHAPE
		if (wordNumber == 2) {
			if (word.getTipoToken().equals("forma")) {

				astForma.setTipoToken(word.getLexema());
				astForma.setLineNumber(lineNumber);
				astCreate.addChild(astForma);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo SHAPE");
				System.out.println();
				return false;
			}
		}

		// Debe ser ID
		if (wordNumber == 3) {
			if (word.getTipoToken().equals("id")) {

				astID.setTipoToken(word.getLexema());
				astID.setLineNumber(lineNumber);
				astCreate.addChild(astID);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo ID");
				System.out.println();
				return false;
			}
		}

		// Debe ser FIN DE SENTENCIA
		if (wordNumber == 4) {
			if (word.getTipoToken().equals("fin de sentencia")) {

				nodoPrincipal.addChild(astCreate);
				String nodoPadre = nodoPrincipal.getChild(0).getTipoToken();
				String nodoHijo1 = nodoPrincipal.getChild(0).getChild(0)
						.getTipoToken();
				String nodoHijo2 = nodoPrincipal.getChild(0).getChild(1)
						.getTipoToken();

				System.out
						.printf("Representación árbol (izq-der) de la función Create: \n");

				System.out.printf("- Nodo Padre: %s\n", nodoPadre);
				System.out.printf("- 1er Hijo: %s ", nodoHijo1);
				System.out.printf("- 2do Hijo: %s\n\n", nodoHijo2);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba fin de sentencia ;");
				System.out.println();
				return false;
			}
		}
		return false;
	}

	private boolean checkOrderSetColor(Token word, Integer wordNumber,
			Integer lineNumber) {
		// Debe ser SETCOLOR
		if (wordNumber == 1) {
			if (word.getTipoToken().equals("setcolor")) {

				astSetColor.setTipoToken(word.getLexema());
				astSetColor.setLineNumber(lineNumber);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				return false;
			}
		}

		// Debe ser COLOR_DEF
		if (wordNumber == 2) {
			if (word.getTipoToken().equals("color")) {

				astColor.setTipoToken(word.getLexema());
				astColor.setLineNumber(lineNumber);
				astSetColor.addChild(astColor);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo COLOR");
				System.out.println();
				return false;
			}
		}

		// Debe ser IN
		if (wordNumber == 3) {
			if (word.getTipoToken().equals("in")) {
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo IN");
				System.out.println();
				return false;
			}
		}

		// Debe ser SHAPE
		if (wordNumber == 4) {
			if (word.getTipoToken().equals("forma")) {
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba fin de sentencia ;");
				System.out.println();
				return false;
			}
		}

		// Debe ser ID
		if (wordNumber == 5) {
			if (word.getTipoToken().equals("id")) {

				astID.setTipoToken(word.getLexema());
				astID.setLineNumber(lineNumber);
				astSetColor.addChild(astID);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo ID");
				System.out.println();
				return false;
			}
		}

		// Debe ser FIN DE SENTENCIA
		if (wordNumber == 6) {
			if (word.getTipoToken().equals("fin de sentencia")) {
				nodoPrincipal.addChild(astSetColor);
				String nodoPadre = nodoPrincipal.getChild(1).getTipoToken();
				String nodoHijo1 = nodoPrincipal.getChild(1).getChild(0)
						.getTipoToken();
				String nodoHijo2 = nodoPrincipal.getChild(1).getChild(1)
						.getTipoToken();

				System.out
						.printf("Representación árbol (izq-der) de la función SetColor: \n");

				System.out.printf("- Nodo Padre: %s\n", nodoPadre);
				System.out.printf("- 1er Hijo: %s ", nodoHijo1);
				System.out.printf("- 2do Hijo: %s\n\n", nodoHijo2);

				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba fin de sentencia ;");
				System.out.println();
				return false;
			}
		}
		return false;
	}

	private boolean checkOrderSetBase(Token word, Integer wordNumber,
			Integer lineNumber) {
		// Debe ser SETBASE
		if (wordNumber == 1) {
			if (word.getTipoToken().equals("setbase")) {

				astSetBase.setTipoToken(word.getLexema());
				astSetBase.setLineNumber(lineNumber);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println();
				return false;
			}
		}

		// Debe ser EXPRESSION
		if (wordNumber == 2) {
			if (word.getTipoToken().equals("expresion")) {

				astExpresion.setTipoToken(word.getLexema());
				astExpresion.setLineNumber(lineNumber);
				astSetBase.addChild(astExpresion);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out
						.println("Error: Se esperaba palabra del tipo EXPRESSION");
				System.out.println();
				return false;
			}
		}

		// Debe ser IN
		if (wordNumber == 3) {
			if (word.getTipoToken().equals("in")) {
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo IN");
				System.out.println();
				return false;
			}
		}

		// Debe ser RECTANGLE
		if (wordNumber == 4) {
			if (word.getLexema().equals("rectangle")) {
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba la palabra RECTANGLE");
				System.out.println();
				return false;
			}
		}

		// Debe ser ID
		if (wordNumber == 5) {
			if (word.getTipoToken().equals("id")) {

				astID.setTipoToken(word.getLexema());
				astID.setLineNumber(lineNumber);
				astSetBase.addChild(astID);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo ID");
				System.out.println();
				return false;
			}
		}

		// Debe ser FIN DE SENTENCIA
		if (wordNumber == 6) {
			if (word.getTipoToken().equals("fin de sentencia")) {

				nodoPrincipal.addChild(astSetBase);
				String nodoPadre = nodoPrincipal.getChild(2).getTipoToken();
				String nodoHijo1 = nodoPrincipal.getChild(2).getChild(0)
						.getTipoToken();
				String nodoHijo2 = nodoPrincipal.getChild(2).getChild(1)
						.getTipoToken();

				System.out
						.printf("Representación árbol (izq-der) de la función SetBase: \n");

				System.out.printf("- Nodo Padre: %s\n", nodoPadre);
				System.out.printf("- 1er Hijo: %s ", nodoHijo1);
				System.out.printf("- 2do Hijo: %s\n\n", nodoHijo2);

				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba fin de sentencia ;");
				System.out.println();
				return false;
			}
		}
		return false;
	}

	private boolean checkOrderSetHeight(Token word, Integer wordNumber,
			Integer lineNumber) {
		// Debe ser SETHEIGHT
		if (wordNumber == 1) {
			if (word.getTipoToken().equals("setheight")) {

				astSetHeight.setTipoToken(word.getLexema());
				astSetHeight.setLineNumber(lineNumber);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				return false;
			}
		}

		// Debe ser EXPRESSION
		if (wordNumber == 2) {
			if (word.getTipoToken().equals("expresion")) {

				astExpresion.setTipoToken(word.getLexema());
				astExpresion.setLineNumber(lineNumber);
				astSetHeight.addChild(astExpresion);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out
						.println("Error: Se esperaba palabra del tipo EXPRESION");
				System.out.println();
				return false;
			}
		}

		// Debe ser IN
		if (wordNumber == 3) {
			if (word.getTipoToken().equals("in")) {
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo IN");
				System.out.println();
				return false;
			}
		}

		// Debe ser RECTANGLE
		if (wordNumber == 4) {
			if (word.getLexema().equals("rectangle")) {
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba la palabra RECTANGLE");
				System.out.println();
				return false;
			}
		}

		// Debe ser ID
		if (wordNumber == 5) {
			if (word.getTipoToken().equals("id")) {

				astID.setTipoToken(word.getLexema());
				astID.setLineNumber(lineNumber);
				astSetHeight.addChild(astID);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo ID");
				System.out.println();
				return false;
			}
		}

		// Debe ser FIN DE SENTENCIA
		if (wordNumber == 6) {
			if (word.getTipoToken().equals("fin de sentencia")) {

				nodoPrincipal.addChild(astSetHeight);
				String nodoPadre = nodoPrincipal.getChild(3).getTipoToken();
				String nodoHijo1 = nodoPrincipal.getChild(3).getChild(0)
						.getTipoToken();
				String nodoHijo2 = nodoPrincipal.getChild(3).getChild(1)
						.getTipoToken();

				System.out
						.printf("Representación árbol (izq-der) de la función SetHeight: \n");

				System.out.printf("- Nodo Padre: %s\n", nodoPadre);
				System.out.printf("- 1er Hijo: %s ", nodoHijo1);
				System.out.printf("- 2do Hijo: %s\n\n", nodoHijo2);

				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba fin de sentencia ;");
				System.out.println();
				return false;
			}
		}
		return false;
	}

	private boolean checkOrderSetRadius(Token word, Integer wordNumber,
			Integer lineNumber) {
		// Debe ser RADIUS
		if (wordNumber == 1) {
			if (word.getTipoToken().equals("setradius")) {

				astSetRadius.setTipoToken(word.getLexema());
				astSetRadius.setLineNumber(lineNumber);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				return false;
			}
		}

		// Debe ser EXPRESSION
		if (wordNumber == 2) {
			if (word.getTipoToken().equals("expresion")) {

				astExpresion.setTipoToken(word.getLexema());
				astExpresion.setLineNumber(lineNumber);
				astSetRadius.addChild(astExpresion);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo SHAPE");
				System.out.println();
				return false;
			}
		}

		// Debe ser IN
		if (wordNumber == 3) {
			if (word.getTipoToken().equals("in")) {
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo IN");
				System.out.println();
				return false;
			}
		}

		// Debe ser CIRCLE
		if (wordNumber == 4) {
			if (word.getLexema().equals("circle")) {
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba la palabra RECTANGLE");
				System.out.println();
				return false;
			}
		}

		// Debe ser ID
		if (wordNumber == 5) {
			if (word.getTipoToken().equals("id")) {

				astID.setTipoToken(word.getLexema());
				astID.setLineNumber(lineNumber);
				astSetRadius.addChild(astID);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo ID");
				System.out.println();
				return false;
			}
		}

		// Debe ser FIN DE SENTENCIA
		if (wordNumber == 6) {
			if (word.getTipoToken().equals("fin de sentencia")) {

				nodoPrincipal.addChild(astSetRadius);
				String nodoPadre = nodoPrincipal.getChild(4).getTipoToken();
				String nodoHijo1 = nodoPrincipal.getChild(4).getChild(0)
						.getTipoToken();
				String nodoHijo2 = nodoPrincipal.getChild(4).getChild(1)
						.getTipoToken();

				System.out
						.printf("Representación árbol (izq-der) de la función SetRadius: \n");

				System.out.printf("- Nodo Padre: %s\n", nodoPadre);
				System.out.printf("- 1er Hijo: %s ", nodoHijo1);
				System.out.printf("- 2do Hijo: %s\n\n", nodoHijo2);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba fin de sentencia ;");
				System.out.println();
				return false;
			}
		}
		return false;
	}

	private void checkCommands(String lookahead, Integer words) {

		// CREATE
		if ((lookahead.equals("create")) && (words == 4)) {
			System.out.println("La sintaxis de la línea #" + lineNumber
					+ " es correcta \n");
		} else if (lookahead.equals("create")) {
			System.out.println("Se ha detectado un error en la línea #"
					+ lineNumber);
			System.out
					.println("Error: La cantidad de Comandos recibidos es inferior a la esperada para CREATE \n");
		}

		// SETCOLOR
		if ((lookahead.equals("setcolor")) && (words == 6)) {
			System.out.println("La sintaxis de la línea #" + lineNumber
					+ " es correcta \n");
		} else if (lookahead.equals("setcolor")) {
			System.out.println("Se ha detectado un error en la línea #"
					+ lineNumber);
			System.out
					.println("Error: La cantidad de Comandos recibidos es inferior a la esperada para SETCOLOR \n");
		}

		// SETBASE
		if ((lookahead.equals("setbase")) && (words == 6)) {
			System.out.println("La sintaxis de la línea #" + lineNumber
					+ " es correcta \n");
		} else if (lookahead.equals("setbase")) {
			System.out.println("Se ha detectado un error en la línea #"
					+ lineNumber);
			System.out
					.println("Error: La cantidad de Comandos recibidos es inferior a la esperada para SETBASE \n");
		}

		// SETHEIGHT
		if ((lookahead.equals("setheight")) && (words == 6)) {
			System.out.println("La sintaxis de la línea #" + lineNumber
					+ " es correcta \n");
		} else if (lookahead.equals("setheight")) {
			System.out.println("Se ha detectado un error en la línea #"
					+ lineNumber);
			System.out
					.println("Error: La cantidad de Comandos recibidos es inferior a la esperada para SETHEIGHT \n");
		}

		// SETRADIUS
		if ((lookahead.equals("setradius")) && (words == 6)) {
			System.out.println("La sintaxis de la línea #" + lineNumber
					+ " es correcta \n");
		} else if (lookahead.equals("setradius")) {
			System.out.println("Se ha detectado un error en la línea #"
					+ lineNumber);
			System.out
					.println("Error: La cantidad de Comandos recibidos es inferior a la esperada para SETRADIUS \n");
		}

		// SETPOSITION
		if ((lookahead.equals("setposition")) && (words == 8)) {
			System.out.println("La sintaxis de la línea #" + lineNumber
					+ " es correcta \n");
		} else if (lookahead.equals("setposition")) {
			System.out.println("Se ha detectado un error en la línea #"
					+ lineNumber);
			System.out
					.println("Error: La cantidad de Comandos recibidos es inferior a la esperada para SETPOSITION \n");
		}
	}
}