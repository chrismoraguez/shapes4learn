package edu.maimonides.multimedia.shapes4learn.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.management.StringValueExp;

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

		// Se recorre e imprime el AST
		visitAST(nodoPrincipal);
		
	
		
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
			if (word.getTipoToken().equals("expresion")) {
				// Se pasa la expresion a anotacion polaca inversa para luego poder trabajarla
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
				astExpresion = new AST();
				astExpresion = createAritmeticNode(inversa);
				astSetHeight.addChild(astExpresion);
				
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

		// Debe ser SHAPE
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
				System.out.println("Error: Se esperaba palabra del tipo SHAPE");
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
			if (word.getTipoToken().equals("expresion")) {
				// Se pasa la expresion a anotacion polaca inversa para luego poder trabajarla
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
			if (word.getTipoToken().equals("expresion")) {
				// Se pasa la expresion a anotacion polaca inversa para luego poder trabajarla
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

	private void visitAST(AST ast) {
		// Metodo que recorre el AST y va imprimiendo por pantalla
		
		// Imprimo al padre
		System.out.println(ast.getValue());
		
		// Obtengo una lista de los hijos de la raiz
		List <AST> listChildren=ast.listChildren();
		
		for (AST tmp: listChildren){				
				// El hijo tiene hijos y por cada uno invoco de nuevo el metodo para imprimirlos
				visitAST(tmp);
		}
	}
	
	/*private void showGuiones(Integer astIteratorNumber) {  ************** LA COMENTO DADO QUE TODAVIA NO LA ESTAMOS UTILIZANDO, LUEGO LA VAMOS A OPTIMIZAR Y UTILIZAR ****************
		// Imprime los guiones que van a la izquierda del hijo, es solo un tema de formato 
		System.out.print("\n");
		
		for (int i = 0; i < astIteratorNumber; i++){
			// Tantos guiones como # de iteracion en la que estoy
			System.out.print("-");
		}
		
	}*/

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
			if (word.getTipoToken().equals("expresion")) {
				// Se pasa la expresion a anotacion polaca inversa para luego poder trabajarla
				String inversa = convertToPolacaInversa(word.getLexema());
				
				// Se confecciona el AST de la expresion aritmetica
				astExpresion = new AST();
				astExpresion = createAritmeticNode(inversa);
				astSetHeight.addChild(astExpresion);
				
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
				System.out.println("Push de ... " + caracter);
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
		// Verifica la jerarquia
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
		// Se depura la expresion aritmetica 
		   String expr = depurar(notation);
		   String[] arrayInfix = expr.split(" ");
		   
	    // Se declaran de las pilas
	    Stack < String > E = new Stack < String > (); //Pila entrada
	    Stack < String > P = new Stack < String > (); //Pila temporal para operadores
	    Stack < String > S = new Stack < String > (); //Pila salida

	    // Añadir al array a la Pila de entrada (E)
	    for (int i = arrayInfix.length - 1; i >= 0; i--) {
	      E.push(arrayInfix[i]);
	    }

	    try {
	      // Algoritmo de conversion a polaca inversa
	      while (!E.isEmpty()) {
	        switch (inversa(E.peek())){
	          case 1:
	            P.push(E.pop());
	            break;
	          case 3:
	          case 4:
	            while(inversa(P.peek()) >= inversa(E.peek())) {
	              S.push(P.pop());
	            }
	            P.push(E.pop());
	            break; 
	          case 2:
	            while(!P.peek().equals("(")) {
	              S.push(P.pop());
	            }
	            P.pop();
	            E.pop();
	            break; 
	          default:
	            S.push(E.pop()); 
	        } 
	      } 
		 
	      
	      // Se reemplaza expr con el resultado de la inversa
	      expr = S.toString().replaceAll("[\\]\\[,]", "");
	     
	    }catch(Exception ex){ 
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
		
        for (char c: inversa.toCharArray()) {
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
        return  stack.pop();

	} 
	
	  private static String depurar(String s) {
		 // Se eliminan caracteres que "molestan" en la expresion aritmetica
	    s = s.replaceAll("\\s+", ""); 
	    s = "(" + s + ")";
	    String simbols = "+-*/()";
	    String str = "";
	  
	    //Deja espacios entre operadores
	    for (int i = 0; i < s.length(); i++) {
	      if (simbols.contains("" + s.charAt(i))) {
	        str += " " + s.charAt(i) + " ";
	      }else str += s.charAt(i);
	    }
	    return str.replaceAll("\\s+", " ").trim();
	  }
	  
	 
	  private static int inversa(String op) {
		// Jerarquia de los operadores
		int prf = 99;
	    if (op.equals("^")) prf = 5;
	    if (op.equals("*") || op.equals("/")) prf = 4;
	    if (op.equals("+") || op.equals("-")) prf = 3;
	    if (op.equals(")")) prf = 2;
	    if (op.equals("(")) prf = 1;
	    return prf;
	  }
	  
}