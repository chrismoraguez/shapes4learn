package edu.maimonides.multimedia.shapes4learn.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
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
	public Integer astIteratorNumber = 0;
	public AST nodoPrincipal = new AST();
	public AST nodoPrincipalExpr = new AST();
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
	public boolean continuo = true;

	public AST analyze(List<Token> tokens) throws SyntacticException {

		List<Token> sentenceGrammar = new ArrayList<>();

		System.out.println("-----------------------------------");
		System.out.println("Análisis Sintáctico: \n");

		for (Token token : tokens) {

			tipoToken = token.getTipoToken();

			if (continuo) {
				// Se valida que el token no indique fin de sentencia
				if (!tipoToken.equals("fin de sentencia")
						&& (!tokens.isEmpty())) {
					// No es fin de sentencia, se va reconstruyendo la
					// sentencia
					sentenceGrammar.add(token);
				} else {
					// Es fin de sentencia, se cierra la sentencia y se
					// manda a analizar sintacticamente
					sentenceGrammar.add(token);
					continuo = checkSentence(sentenceGrammar, lineNumber);
					sentenceGrammar = new ArrayList<>();
					lineNumber++;
				}
			}
		}

		if (!tipoToken.equals("fin de sentencia")) {
			// Para casos en los cuales no se hayan finalizado la
			// sentencia correctamente
			continuo = checkSentence(sentenceGrammar, lineNumber);
			sentenceGrammar = new ArrayList<>();
		}

		// Se recorre e imprime el AST
		if (continuo) {
			System.out
					.println("---------------------------Impresión AST-----------------------------");
			visitAST(nodoPrincipal);
			return nodoPrincipal;
		} else {
			return new AST();
		}
	}

	private boolean checkSentence(List<Token> sentences, Integer lineNumber) {
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
				return false;
			}
		}

		// Dependiendo la accion ingresada por el usuario se valida que la
		// cantidad de comandos sea la correcta
		if (validate) {
			checkCommands(lookahead, wordNumber);
		}
		return true;
	}

	private void visitAST(AST ast) {
		// Metodo que recorre el AST y va imprimiendo por pantalla

		if (ast.getValue().equals("create")
				|| ast.getValue().equals("setcolor")
				|| ast.getValue().equals("setbase")
				|| ast.getValue().equals("setheight")
				|| ast.getValue().equals("setposition")
				|| ast.getValue().equals("setradius")) {
			System.out
					.println("Representación árbol: " + ast.getValue() + "\n");
		}
		// Imprimo al padre
		System.out.println(ast.getValue());

		// Obtengo una lista de los hijos de la raiz
		List<AST> listChildren = ast.listChildren();

		for (AST tmp : listChildren) {
			// El hijo tiene hijos y por cada uno invoco de nuevo el metodo para
			// imprimirlos
			showGuiones(astIteratorNumber);
			astIteratorNumber++;
			visitAST(tmp);
		}
		astIteratorNumber--;
	}

	private void showGuiones(Integer astIteratorNumber) {
		// Imprime los guiones que van a la izquierda del hijo, es solo un tema
		// de formato
		System.out.print("\n");

		for (int i = 0; i < astIteratorNumber; i++) {
			// Tantos guiones como # de iteracion en la que estoy
			System.out.print("-");
		}
	}

	private boolean checkOrderSetPosition(Token word, Integer wordNumber,
			Integer lineNumber) {
		// Debe ser SETPOSITION
		if (wordNumber == 1) {
			if (word.getTipoToken().equals("setposition")) {
				astSetPosition = new AST();
				astSetPosition.setValue(word.getLexema());
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
			if (word.getTipoToken().equals("expresion")
					&& validarExpresion(word.getLexema())) {
				// Se pasa la expresion a anotacion polaca inversa para luego
				// poder trabajarla
				String inversa = convertToPolacaInversa(word.getLexema());

				// Se confecciona el AST de la expresion aritmetica
				astExpresion = new AST();
				astExpresion = createAritmeticNode(inversa);
				astSetPosition.addChild(astExpresion);

				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out
						.println("Error: Se esperaba palabra del tipo EXPRESSION/Expresión Inválida");
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
			if (word.getTipoToken().equals("expresion")
					&& validarExpresion(word.getLexema())) {
				// Se pasa la expresion a anotacion polaca inversa para luego
				// poder trabajarla
				String inversa = convertToPolacaInversa(word.getLexema());

				// Se confecciona el AST de la expresion aritmetica
				astExpresion = new AST();
				astExpresion = createAritmeticNode(inversa);
				astSetPosition.addChild(astExpresion);

				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out
						.println("Error: Se esperaba palabra del tipo EXPRESSION/Expresión Inválida");
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
			if (word.getTipoToken().equals("shape")) {
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
				astID = new AST();
				astID.setValue(word.getLexema());
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
				astCreate = new AST();
				astCreate.setValue(word.getLexema());
				astCreate.setLineNumber(lineNumber);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				return false;
			}
		}

		// Debe ser FORMA
		if (wordNumber == 2) {
			if (word.getTipoToken().equals("forma")) {
				astForma = new AST();
				astForma.setValue(word.getLexema());
				astForma.setLineNumber(lineNumber);
				astCreate.addChild(astForma);
				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo FORMA");
				System.out.println();
				return false;
			}
		}

		// Debe ser ID
		if (wordNumber == 3) {
			if (word.getTipoToken().equals("id")) {
				astID = new AST();
				astID.setValue(word.getLexema());
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
				astSetColor = new AST();
				astSetColor.setValue(word.getLexema());
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
				astColor = new AST();
				astColor.setValue(word.getLexema());
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
			if (word.getTipoToken().equals("shape")) {
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
		if (wordNumber == 5) {
			if (word.getTipoToken().equals("id")) {
				astID = new AST();
				astID.setValue(word.getLexema());
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
				astSetBase = new AST();
				astSetBase.setValue(word.getLexema());
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
			if (word.getTipoToken().equals("expresion")
					&& validarExpresion(word.getLexema())) {
				// Se pasa la expresion a anotacion polaca inversa para luego
				// poder trabajarla
				String inversa = convertToPolacaInversa(word.getLexema());

				// Se confecciona el AST de la expresion aritmetica
				astExpresion = new AST();
				astExpresion = createAritmeticNode(inversa);
				astSetBase.addChild(astExpresion);

				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out
						.println("Error: Se esperaba palabra del tipo EXPRESSION/Expresión Inválida");
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
				astID = new AST();
				astID.setValue(word.getLexema());
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
				astSetHeight = new AST();
				astSetHeight.setValue(word.getLexema());
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
			if (word.getTipoToken().equals("expresion")
					&& validarExpresion(word.getLexema())) {
				// Se pasa la expresion a anotacion polaca inversa para luego
				// poder trabajarla
				String inversa = convertToPolacaInversa(word.getLexema());

				// Se confecciona el AST de la expresion aritmetica
				astExpresion = new AST();
				astExpresion = createAritmeticNode(inversa);
				astSetHeight.addChild(astExpresion);

				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out
						.println("Error: Se esperaba palabra del tipo EXPRESSION/Expresión Inválida");
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
				astID = new AST();
				astID.setValue(word.getLexema());
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
				astSetRadius = new AST();
				astSetRadius.setValue(word.getLexema());
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
			if (word.getTipoToken().equals("expresion")
					&& validarExpresion(word.getLexema())) {
				// Se pasa la expresion a anotacion polaca inversa para luego
				// poder trabajarla
				String inversa = convertToPolacaInversa(word.getLexema());

				// Se confecciona el AST de la expresion aritmetica
				astExpresion = new AST();
				astExpresion = createAritmeticNode(inversa);
				astSetRadius.addChild(astExpresion);

				return true;
			} else {
				System.out.println("Se ha detectado un error en la línea #"
						+ lineNumber + ", palabra #" + wordNumber);
				System.out
						.println("Error: Se esperaba palabra del tipo EXPRESSION/Expresión Inválida");
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
				System.out.println("Error: Se esperaba la palabra CIRCLE");
				System.out.println();
				return false;
			}
		}

		// Debe ser ID
		if (wordNumber == 5) {
			if (word.getTipoToken().equals("id")) {
				astID = new AST();
				astID.setValue(word.getLexema());
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

	private String convertToPolacaInversa(String notation) {
		// Se depura la expresion aritmetica
		String expr = depurar(notation);
		String[] arrayInfix = expr.split(" ");

		// Se declaran de las pilas
		Stack<String> e = new Stack<String>(); // Pila entrada
		Stack<String> p = new Stack<String>(); // Pila temporal para operadores
		Stack<String> s = new Stack<String>(); // Pila salida

		// Añadir al array a la Pila de entrada (E)
		for (int i = arrayInfix.length - 1; i >= 0; i--) {
			e.push(arrayInfix[i]);
		}

		try {
			// Algoritmo de conversion a polaca inversa
			while (!e.isEmpty()) {
				switch (inversa(e.peek())) {
				case 1:
					p.push(e.pop());
					break;
				case 3:
				case 4:
					while (inversa(p.peek()) >= inversa(e.peek())) {
						s.push(p.pop());
					}
					p.push(e.pop());
					break;
				case 2:
					while (!p.peek().equals("(")) {
						s.push(p.pop());
					}
					p.pop();
					e.pop();
					break;
				default:
					s.push(e.pop());
				}
			}

			// Se reemplaza expr con el resultado de la inversa
			expr = s.toString().replaceAll("[\\]\\[,]", "");

		} catch (Exception ex) {
			System.out.println("Error en la expresión aritmetica");
			System.err.println(ex);
		}

		return expr;
	}

	private AST createAritmeticNode(String inversa) {
		// Este metodo arma el AST de una expresion aritmetica

		// Se instancia la pila y ASTs a utilizar
		Stack<AST> stack = new Stack<AST>();
		AST leftValue = new AST();
		AST rightValue = new AST();
		AST node = new AST();

		for (char c : inversa.toCharArray()) {
			if (c != ' ') {
				// Se crea el nodo actual (ast)
				node = new AST();
				node.setValue(Character.toString(c));

				if ("+-*/".indexOf(c) != -1) {
					// Se desapilan los hijos
					rightValue = stack.pop();
					leftValue = stack.pop();

					// Agregamos los AST hijos al AST padre
					node.addChild(leftValue);
					node.addChild(rightValue);
				}
				stack.push(node);
			}
		}
		stack.push(node);
		return stack.pop();
	}

	private static String depurar(String s) {
		// Se eliminan caracteres que "molestan" en la expresion aritmetica
		s = s.replaceAll("\\s+", "");
		s = "(" + s + ")";
		String simbols = "+-*/()";
		String str = "";

		// Deja espacios entre operadores
		for (int i = 0; i < s.length(); i++) {
			if (simbols.contains("" + s.charAt(i))) {
				str += " " + s.charAt(i) + " ";
			} else
				str += s.charAt(i);
		}
		return str.replaceAll("\\s+", " ").trim();
	}

	private static int inversa(String op) {
		// Jerarquia de los operadores
		int prf = 99;
		if (op.equals("^"))
			prf = 5;
		if (op.equals("*") || op.equals("/"))
			prf = 4;
		if (op.equals("+") || op.equals("-"))
			prf = 3;
		if (op.equals(")"))
			prf = 2;
		if (op.equals("("))
			prf = 1;
		return prf;
	}

	boolean validarExpresion(String cadena) {
		Stack<String> stack = new Stack<String>();
		int i = 0;
		int estadoAux, estadoActual = 1;
		String simbolo;

		// System.out.println("Se procede a la validación de la expresión:");

		while (i < cadena.length()) {
			simbolo = Character.toString(cadena.charAt(i));
			estadoAux = estadoActual;
			estadoActual = comprobarOperacion(simbolo, estadoAux, stack);
			i++;
		}

		boolean pilaVacia = stack.empty();
		try {
			stack.pop();
		} catch (Exception e) {

		}

		if ((pilaVacia) && (estadoActual == 2)) {
			estadoActual++;
		}

		if ((esEstadoAceptacion(estadoActual) == 1)) {
			// System.out.println("La expresión aritmética ingresada es válida\n");
			return true;
		} else {
			// System.out.println("La expresión aritmética ingresada es inválida\n");
			return false;
		}
	}

	int comprobarOperacion(String simbolo, int estadoActual, Stack<String> pila) {
		/* Se comprueba si la operación aritmética es válida */

		boolean pilaVacia = pila.empty();
		try {
			pila.pop();
		} catch (Exception e) {
		}

		switch (estadoActual) {
		case 1: {
			if ((simbolo.charAt(0) == 40) && (pilaVacia)) {
				/* Se valida ingreso de "(" con pila vacia */
				pila.push(simbolo);
			} else if ((simbolo.charAt(0) == 40) && (!pilaVacia)) {
				/* Se valida ingreso de "(" con pila no vacia */
				pila.push(simbolo);
				pila.push(simbolo);
			} else if (((simbolo.charAt(0) >= 48) && (simbolo.charAt(0) <= 57))
					&& (!pilaVacia)) {
				/* Se valida ingreso de numeros del 0 al 9 con pila no vacia */
				pila.push(simbolo);
				estadoActual++;
			} else if (((simbolo.charAt(0) >= 48) && (simbolo.charAt(0) <= 57))
					&& (pilaVacia)) {
				/* Se valida ingreso de numeros del 0 al 9 con pila vacia */
				estadoActual++;
			} else {
				estadoActual = 0; /* Falla la sarta */
			}
		}
			break;

		case 2: {
			if ((simbolo.charAt(0) == 41)
					|| ((simbolo.charAt(0) >= 48) && (simbolo.charAt(0) <= 57))
					&& (!pilaVacia)) {
				/* Se mira la pila y paso al siguiente signo */
			} else if ((simbolo.charAt(0) == 41) && (pilaVacia)) {
				estadoActual = 0; /* Falla la sarta */
			} else if (((42 == simbolo.charAt(0)) || (43 == simbolo.charAt(0))
					|| (45 == simbolo.charAt(0)) || (47 == simbolo.charAt(0)))
					&& (pilaVacia)) {
				/* Se valida ingreso de operadores con pila vacia */
				estadoActual--;
			} else if (((42 == simbolo.charAt(0)) || (43 == simbolo.charAt(0))
					|| (45 == simbolo.charAt(0)) || (47 == simbolo.charAt(0)))
					&& (!pilaVacia)) {
				/* Se valida ingreso de operadores con pila no vacia */
				pila.push(simbolo);
				estadoActual--;
			}
		}
			break;
		}
		return estadoActual;
	}

	int esEstadoAceptacion(int estadoActual) {
		/*
		 * Se verifica si el estado actual es estado de aceptación y se termina
		 * de comprobar la operación aritmética
		 */

		int estadoAceptacion = 3;

		if (estadoAceptacion == estadoActual) {
			return 1;
		} else {
			return 0;
		}
	}
}