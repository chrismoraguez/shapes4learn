package edu.maimonides.multimedia.shapes4learn.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.maimonides.multimedia.shapes4learn.model.AST;

/**
 * This class is responsible for the third part of the interpreter: semantic
 * analysis. It will be implemented by the students to perform the proper
 * operations.
 * 
 * @author Matias Giorgio
 * 
 */
public class SemanticAnalyzer {

	String infixNotation = "";
	String postfixNotation = "";

	public SemanticAnalyzer() {
	}

	public List<String> idList = new ArrayList<String>();

	public AST analyze(AST ast) throws SemanticException {

		System.out.println("\n-----------------------------------\n");
		System.out.println("Análisis Semántico:\n");

		List<AST> listChildren = ast.listChildren();
		boolean errorSemantico = false;
		Integer lineNumber = 1;

		System.out.println("\nFunciones: \n");
		for (AST astTemp : listChildren) {

			// System.out.println(astTemp.getValue());
			switch (astTemp.getValue()) {
			case "create":
				errorSemantico = checkSemanticCreate(astTemp, idList,
						lineNumber);
				break;
			case "setcolor":
				errorSemantico = checkSemanticSetColor(astTemp, idList,
						lineNumber);
				break;
			case "setbase":
				errorSemantico = checkSemanticSetBase(astTemp, idList,
						lineNumber);
				break;
			case "setheight":
				errorSemantico = checkSemanticSetHeight(astTemp, idList,
						lineNumber);
				break;
			case "setradius":
				errorSemantico = checkSemanticSetRadius(astTemp, idList,
						lineNumber);
				break;
			case "setposition":
				errorSemantico = checkSemanticSetPosition(astTemp, idList,
						lineNumber);
				break;
			}

			lineNumber++;
		}

		if (errorSemantico) {

		}

		return ast;
	}

	private boolean checkSemanticSetPosition(AST astTemp, List<String> idList,
			Integer lineNumber) {
		// Chequeo si las expresiones son iguales o menores a cero, si pasa
		// ésto, arroja error. Si no, reemplazar el resultado en el AST.
		// En este caso, son dos expresiones a verificar.

		// 1- Se valida si existe el ID en la lista de IDs existentes.
		// Si existe, debe arrojar error y, si no existe, se agrega
		for (String id : idList) {
			if (id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				// ID EXISTENTE, se imprime el error y se devuelve false
				System.out.println("Se ha detectado un error en la linea #"
						+ lineNumber + " el ID "
						+ astTemp.getChild(1).getValue() + " ya existe.");
				return true;
			}
		}

		System.out.println("La semantica de la linea #" + lineNumber
				+ "es correcta.");
		return false;
	}

	private boolean checkSemanticSetRadius(AST astTemp, List<String> idList,
			Integer lineNumber) {
		// Chequeo si la expresión es igual o menor a cero, si pasa ésto, arroja
		// error. Si no, reemplazar el resultado en el AST.

		// 1- Se valida si existe el ID en la lista de IDs existentes.
		// Si existe, debe arrojar error y, si no existe, se agrega
		for (String id : idList) {
			if (id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				// ID EXISTENTE, se imprime el error y se devuelve false
				System.out.println("Se ha detectado un error en la linea #"
						+ lineNumber + " el ID "
						+ astTemp.getChild(1).getValue() + " ya existe.");
				return true;
			}
		}

		System.out.println("La semantica de la linea #" + lineNumber
				+ "es correcta.");
		return false;
	}

	private boolean checkSemanticSetHeight(AST astTemp, List<String> idList,
			Integer lineNumber) {
		// 1- Se valida si existe el ID en la lista de IDs existentes.
		// Si existe, debe arrojar error y, si no existe, se agrega
		for (String id : idList) {
			if (id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				// ID EXISTENTE, se imprime el error y se devuelve false
				System.out.println("Se ha detectado un error en la linea #"
						+ lineNumber + " el ID "
						+ astTemp.getChild(1).getValue() + " ya existe.");
				return true;
			}
		}

		System.out.println("La semantica de la linea #" + lineNumber
				+ "es correcta.");
		return false;
	}
  
	private boolean checkSemanticSetBase(AST astTemp, List<String> idList,
			Integer lineNumber) {
		// 1- Se recorre el AST de la EXPRESSION
		visitAST(astTemp.getChild(0));

		// 2- Se pasa de polaca a polaca inversa
		//postfixNotation = convertNotation(infixNotation);

		// 3- Se revuelve la EXPRESSION
		// System.out.println("Polaca " + polacaNotation);
		resolveExpression(postfixNotation.trim());

		// 3- Se valida si existe el ID en la lista de IDs existentes.
		// Si existe, debe arrojar error y, si no existe, se agrega
		for (String id : idList) {
			if (id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				// ID EXISTENTE, se imprime el error y se devuelve false
				System.out.println("Se ha detectado un error en la linea #"
						+ lineNumber + " el ID "
						+ astTemp.getChild(1).getValue() + " ya existe.");
				return true;
			}
		}

		System.out.println("La semantica de la linea #" + lineNumber
				+ "es correcta.");
		return false;
	}

	/*private String convertNotation(String infixNotation) {
		infixNotation = infixNotation.replace(" ","");
		System.out.println("imprimo infixNotation " + infixNotation );
		String expr = depurar(infixNotation);
		System.out.println("imprimo infixNotation 2 " + infixNotation );
		String[] arrayInfix = expr.split(" ");

		// Declaración de las pilas
		Stack<String> E = new Stack<String>(); // Pila entrada
		Stack<String> P = new Stack<String>(); // Pila temporal para operadores
		Stack<String> S = new Stack<String>(); // Pila salida

		// Añadir la array a la Pila de entrada (E)
		for (int i = arrayInfix.length - 1; i >= 0; i--) {
			System.out.println("Valor i " + i);
			E.push(arrayInfix[i]);
			System.out.println("Valor E " + E.peek()); 
		}

		try {
			// Algoritmo Infijo a Postfijo
			while (!E.isEmpty()) {
				switch (pref(E.peek())) {
				case 1:
					System.out.println("Valor pila 1 " + E.peek());
					P.push(E.pop());
					break;
				case 3:
				case 4:
					while (pref(P.peek()) >= pref(E.peek())) { 
						System.out.println("Valor pila 2 " + P.peek());
						S.push(P.pop());
					}
					System.out.println("Valor pila 3 " + E.peek());
					P.push(E.pop());
					break;
				case 2:
					while (!P.peek().equals("(")) {
						System.out.println("Valor pila 4 " + P.peek());
						S.push(P.pop());
					}
					P.pop();
					E.pop();
					break;
				default:
					System.out.println("Valor pila 5 " + E.peek());
					S.push(E.pop());
				}
			}

			// Eliminacion de 'basura' en la expression
			// String infix = expr.replace(" ", "");
			System.out.println("Substring " + S.toString());
			expr = S.toString().replaceAll("[\\]\\[,]", "");

			// Mostrar resultados:
			// System.out.println("Expresion Infija: " + infix);
			System.out.println("Expresion Postfija: " + expr);

		} catch (Exception ex) {
			System.out.println("Error en la EXPRESSION");
			System.err.println(ex);
		}

		return expr;
	}*/

	private boolean checkSemanticSetColor(AST astTemp, List<String> idList,
			Integer lineNumber) {
		// 1- Se valida si existe el ID en la lista de IDs existentes.
		// Si existe, debe arrojar error y, si no existe, se agrega
		for (String id : idList) {
			if (id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				// ID EXISTENTE, se imprime el error y se devuelve false
				System.out.println("Se ha detectado un error en la linea #"
						+ lineNumber + " el ID "
						+ astTemp.getChild(1).getValue() + " ya existe.");
				return true;
			}
		}

		System.out.println("La semantica de la linea #" + lineNumber
				+ " es correcta.");
		return false;
	}

	private boolean checkSemanticCreate(AST astTemp, List<String> idList,
			Integer lineNumber) {

		// 1- Se valida si existe el ID en la lista de IDs existentes.
		// Si existe, debe arrojar error y, si no existe, se agrega
		for (String id : idList) {
			if (id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				// ID EXISTENTE, se imprime el error y se devuelve false
				System.out.println("Se ha detectado un error en la linea #"
						+ lineNumber + " el ID "
						+ astTemp.getChild(1).getValue() + " ya existe.");
				return true;
			}
		}

		idList.add(astTemp.getChild(1).getValue());
		System.out.println("La semantica de la linea #" + lineNumber
				+ " es correcta.");
		return false;
	}

	private void visitAST(AST ast) {
		// Metodo que recorre el AST de la EXPRESSION y la rearma en notacion
		// polaca inversa

		// Se va rearmando EXPRESSION
		infixNotation = infixNotation + " " + ast.getValue();

		// Se ontiene una lista de los hijos de la raiz
		List<AST> listChildren = ast.listChildren();

		for (AST tmp : listChildren) {
			// El hijo tiene hijos y por cada uno se invoca de nuevo el metodo
			// para
			// imprimirlos
			visitAST(tmp);
		}
	}

	private void resolveExpression(String infixNotation) {

		// Se limpia la expression
		System.out.println("Polaquita " + infixNotation);
		String[] post = infixNotation.split(" ");

		// Declaración de las pilas
		Stack<String> E = new Stack<String>(); // Pila entrada
		Stack<String> P = new Stack<String>(); // Pila de operandos

		// Añadir post (array) a la Pila de entrada
		for (int i = post.length - 1; i >= 0; i--) {
			E.push(post[i]);
		}

		// Algoritmo de Evaluación Expression
		String operadores = "+-*/%";
		while (!E.isEmpty()) {
			if (operadores.contains("" + E.peek())) {
				P.push(evaluar(E.pop(), P.pop(), P.pop()) + "");
			} else {
				P.push(E.pop());
			}
		}

		// Mostrar resultados:
		System.out.println("Expresion: " + infixNotation);
		System.out.println("Resultado: " + P.peek());

	}

	private static int evaluar(String op, String n2, String n1) {
		int num1 = Integer.parseInt(n1);
		int num2 = Integer.parseInt(n2);
		if (op.equals("+"))
			return (num1 + num2);
		if (op.equals("-"))
			return (num1 - num2);
		if (op.equals("*"))
			return (num1 * num2);
		if (op.equals("/"))
			return (num1 / num2);
		if (op.equals("%"))
			return (num1 % num2);
		return 0;
	}

	private static String depurar(String s) {
		// Elimina espacios en blanco
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

	// Jerarquia de los operadores
	private static int pref(String op) {
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

}