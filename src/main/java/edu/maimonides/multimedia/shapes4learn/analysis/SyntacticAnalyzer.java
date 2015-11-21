package edu.maimonides.multimedia.shapes4learn.analysis;

import java.util.ArrayList;
import java.util.List;

import edu.maimonides.multimedia.shapes4learn.model.AST;
import edu.maimonides.multimedia.shapes4learn.model.Pila;
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
				// Es fin de sentencia, se cierra la sentencia y se manda a analizar sintacticamente
				sentenceGrammar.add(token);
				checkSentence(sentenceGrammar, lineNumber);
				sentenceGrammar = new ArrayList<>();
				lineNumber++;
			}
		}

		System.out.println("Cantidad de hijos: "
				+ nodoPrincipal.listChildren().size());

		for (int i = 0; i < nodoPrincipal.listChildren().size(); i++) {

			if (nodoPrincipal.getChild(i).getValue().equals("setposition")) {
				String nodoPadre = nodoPrincipal.getChild(i).getValue();
				String nodoHijo1 = nodoPrincipal.getChild(i).getChild(0)
						.getValue();
				String nodoHijo2 = nodoPrincipal.getChild(i).getChild(1)
						.getValue();
				String nodoHijo3 = nodoPrincipal.getChild(i).getChild(2)
						.getValue();

				System.out.printf("Representación árbol (izq-der): \n");

				System.out.printf("- Nodo Padre: %s\n", nodoPadre);
				System.out.printf("- 1er Hijo: %s ", nodoHijo1);
				System.out.printf("- 2do Hijo: %s ", nodoHijo2);
				System.out.printf("- 3er Hijo: %s \n", nodoHijo3);

			} else {
				String nodoPadre = nodoPrincipal.getChild(i).getValue();
				String nodoHijo1 = nodoPrincipal.getChild(i).getChild(0)
						.getValue();
				String nodoHijo2 = nodoPrincipal.getChild(i).getChild(1)
						.getValue();

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
				// Se detecto algun error en la sentencia, se finaliza el analisis sintactico de esa sentencia
				break;
			}
		}

		// Dependiendo la accion ingresada por el usuario se valida que la cantidad de comandos sea la correcta
		if (validate) {
			checkCommands(lookahead, wordNumber);
		}
	}

	private boolean checkOrderSetPosition(Token word, Integer wordNumber,
			Integer lineNumber) {
		// Debe ser SETPOSITION
		if (wordNumber == 1) {
			if (word.getTipoToken().equals("setposition")) {
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
			if (word.getTipoToken().equals("expresion")) {
				// Se pasa la expresion a anotacion polaca inversa para luego poder trabajarla
				String inversa = convertToPolacaInversa(word.getLexema());

				// Se confecciona el AST de la expresion aritmetica
				AST tmp = createExpressionAST(inversa, lineNumber);
				astExpresion.setValue(word.getLexema());
				astExpresion.setLineNumber(lineNumber);

				System.out.println("Raiz ---> " + tmp.getValue());
				System.out.println("Nodo 0 --->  " + tmp.getChild(0).getValue());
				System.out.println("Nodo 1 --->  " + tmp.getChild(1).getValue());
				System.out.println("Nodo 1.1 --->  " + tmp.getChild(1).getChild(0).getValue());
				System.out.println("Nodo 1.2 --->  " + tmp.getChild(1).getChild(1).getValue());

				System.exit(0);

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
				// Se pasa la expresion a anotacion polaca inversa para luego poder trabajarla
				String inversa = convertToPolacaInversa(word.getLexema());

				// Se confecciona el AST de la expresion aritmetica
				AST tmp = createExpressionAST(inversa, lineNumber);
				astExpresion.setValue(word.getLexema());
				astExpresion.setLineNumber(lineNumber);

				System.out.println("Raiz ---> " + tmp.getValue());
				System.out.println("Nodo 0 --->  " + tmp.getChild(0).getValue());
				System.out.println("Nodo 1 --->  " + tmp.getChild(1).getValue());
				System.out.println("Nodo 1.1 --->  " + tmp.getChild(1).getChild(0).getValue());
				System.out.println("Nodo 1.2 --->  " + tmp.getChild(1).getChild(1).getValue());

				System.exit(0);

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
				astCreate.setValue(word.getLexema());
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
				astForma.setValue(word.getLexema());
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
			if (word.getTipoToken().equals("expresion")) {
				// Se pasa la expresion a anotacion polaca inversa para luego poder trabajarla
				String inversa = convertToPolacaInversa(word.getLexema());
				
				// Se confecciona el AST de la expresion aritmetica
				AST tmp = createExpressionAST(inversa, lineNumber);
				astExpresion.setValue(word.getLexema());
				astExpresion.setLineNumber(lineNumber);
				
				System.out.println("Raiz ---> " + tmp.getValue());
				System.out.println("Nodo 0 --->  " + tmp.getChild(0).getValue());
				System.out.println("Nodo 1 --->  " + tmp.getChild(1).getValue());
				System.out.println("Nodo 1.1 --->  " + tmp.getChild(1).getChild(0).getValue());
				System.out.println("Nodo 1.2 --->  " + tmp.getChild(1).getChild(1).getValue());
				
				System.exit(0);
				
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
			if (word.getTipoToken().equals("expresion")) {
				// Se pasa la expresion a anotacion polaca inversa para luego poder trabajarla
				String inversa = convertToPolacaInversa(word.getLexema());

				// Se confecciona el AST de la expresion aritmetica
				AST tmp = createExpressionAST(inversa, lineNumber);
				astExpresion.setValue(word.getLexema());
				astExpresion.setLineNumber(lineNumber);

				System.out.println("Raiz ---> " + tmp.getValue());
				System.out.println("Nodo 0 --->  " + tmp.getChild(0).getValue());
				System.out.println("Nodo 1 --->  " + tmp.getChild(1).getValue());
				System.out.println("Nodo 1.1 --->  " + tmp.getChild(1).getChild(0).getValue());
				System.out.println("Nodo 1.2 --->  " + tmp.getChild(1).getChild(1).getValue());

				System.exit(0);

				astSetBase.addChild(astExpresion);
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
			if (word.getTipoToken().equals("expresion")) {
				// Se pasa la expresion a anotacion polaca inversa para luego poder trabajarla
				String inversa = convertToPolacaInversa(word.getLexema());
				
				// Se valida el orden que posee la operacion aritmetica
				checkOrderArithmeticExpression(inversa);

				// Se confecciona el AST de la expresion aritmetica
				AST tmp = createExpressionAST(inversa, lineNumber);
				astExpresion.setValue(word.getLexema());
				astExpresion.setLineNumber(lineNumber);

				System.out.println("Raiz ---> " + tmp.getValue());
				System.out.println("Nodo 0 --->  " + tmp.getChild(0).getValue());
				System.out.println("Nodo 1 --->  " + tmp.getChild(1).getValue());
				System.out.println("Nodo 1.1 --->  " + tmp.getChild(1).getChild(0).getValue());
				System.out.println("Nodo 1.2 --->  " + tmp.getChild(1).getChild(1).getValue());

				System.exit(0);

				astSetBase.addChild(astExpresion);
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

	private void checkOrderArithmeticExpression(String inversa) {
		/*String[] parts = inversa.split(" ");

		// Se recorre parte por parte y se valida que el orden sea correcto
		for (int i = 0; i < parts.length; i++) {
			// Si o si debe ser un operador aritmetico ya que no puede empezar de otra manera
			if((i==0)&&(parts[0].matches("[0-9]*"))){
				
				
			}else{ }
		}*/
		
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

	public static Pila disarmNotation(String infijo) {
		infijo = '(' + infijo; // Agregamos al final del infijo un parenteris
		int tamaño = infijo.length();
		Pila PilaDefinitiva = new Pila(tamaño);
		Pila PilaTemp = new Pila(tamaño);
		PilaTemp.push(')'); // Agregamos a la pila temporal parenteris
		for (int i = tamaño - 1; i > -1; i--) {
			char caracter = infijo.charAt(i);
			switch (caracter) {
			case ')':
				PilaTemp.push(caracter);
				break;
			case '+':
			case '-':
			case '^':
			case '*':
			case '/':
				while (Jerarquia(caracter) > Jerarquia(PilaTemp.nextPop()))
					PilaDefinitiva.push(PilaTemp.pop());
				PilaTemp.push(caracter);
				break;
			case '(':
				while (PilaTemp.nextPop() != ')')
					PilaDefinitiva.push(PilaTemp.pop());
				PilaTemp.pop();
				break;
			default:
				PilaDefinitiva.push(caracter);
			}
		}
		return PilaDefinitiva;
	}

	public static int Jerarquia(char elemento) {
		int res = 0;
		switch (elemento) {
		case ')':
			res = 5;
			break;
		case '^':
			res = 4;
			break;
		case '*':
		case '/':
			res = 3;
			break;
		case '+':
		case '-':
			res = 2;
			break;
		case '(':
			res = 1;
			break;
		}
		return res;
	}

	private String convertToPolacaInversa(String notation) {
		// Este metodo pasa la operacion aritmetica a notacion polaca inversa
		String tmpNotation = "";

		Pila p1 = disarmNotation(notation);

		// Se recorre la pila y a partir de eso se va concatenando para armar la notacion polaca inversa
		while (p1.i > 0) {
			tmpNotation += p1.pop();
			tmpNotation += " ";
		}
		
		return tmpNotation;
	}

	private AST createExpressionAST(String inversa, Integer lineNumber) {
		// Este metodo crea el AST de una expresion aritmetica
		AST expressionNodo1 = new AST();
		AST expressionNodo2 = new AST();
		AST expressionNodo3 = new AST();

		// 1) Se segmenta la expresion aritmetica por espacio (asi se obtiene caracter por caracter)
		System.out.println("Operacion pasada a inversa  " + inversa);
		String[] parts = inversa.split(" ");

		// 2) Se recorre la expresion y cada vez que se detecta un operador se crea un nuevo NODO HIJO
		for (int i = 0; i < parts.length; i++) {
			if (i == 0) {
				// Para la primer iteracion se contempla a la operacion inicial como nodo raiz
				expressionNodo1.setValue(parts[i]);
				expressionNodo1.setLineNumber(lineNumber);
				//System.out.println("Iteracion if1 " + i + " " + expressionNodo1.getValue());
			} else if ((parts[i].trim().equals("+")) || (parts[i].trim().equals("-")) || (parts[i].trim().equals("*")) || (parts[i].trim().equals("/"))) {
				// Se detecta un operador, se crea un nuevo nodo
				// ANTES SE VALIDA QUE puede ser que el nodo expressionNodo ya tenga hijos , asi que por las dudas se verifica eso y de ser asi se lo agrega a nodo raiz
			
				if (expressionNodo3.listChildren().size() > 0) {
					// Significa que tiene hijos, es decir , ya paso por el IF de abajo
					expressionNodo1.addChild(expressionNodo3);
				}
				
				expressionNodo3 = new AST();
				expressionNodo3.setValue(parts[i]);
				expressionNodo3.setLineNumber(lineNumber);
				//System.out.println("Iteracion if2 " + i + " "+ expressionNodo3.getValue());
			} else {
				// Se utiliza el nodo existente
				expressionNodo2 = new AST();
				expressionNodo2.setValue(parts[i]);
				expressionNodo2.setLineNumber(lineNumber);

				// Puede suceder que todavia nodo 3 no exista, en ese caso lo agrego a nodo 1
				if (expressionNodo3.getValue().equals("")) {
					expressionNodo1.addChild(expressionNodo2); 
				} else {
					expressionNodo3.addChild(expressionNodo2);
				}

				//System.out.println("Iteracion if3 " + i + " "+ expressionNodo2.getValue());
			}
		}

		expressionNodo1.addChild(expressionNodo3);

		return expressionNodo1;

	}
}