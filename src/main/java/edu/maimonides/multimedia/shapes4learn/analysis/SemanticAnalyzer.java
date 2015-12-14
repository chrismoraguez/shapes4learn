package edu.maimonides.multimedia.shapes4learn.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.maimonides.multimedia.shapes4learn.Launcher;
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
	String result = "";
	List<AST> listChildren;
	public static String LEFT_DONE = "LEFT_DONE";

	public SemanticAnalyzer() {
	}

	public static List<String> idList = new ArrayList<String>();

	public AST analyze(AST ast) throws SemanticException {

		System.out.println("\n-----------------------------------\n");
		System.out.println("An�lisis Sem�ntico:\n");

		listChildren = ast.listChildren();
		boolean errorSemantico = false;
		Integer lineNumber = 1;

		System.out.println("Funciones: \n");
		if (Launcher.continuo) {
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
				if (errorSemantico) {
					System.out
							.println("El analizador sem�ntico ha encontrado errores.");
					break;
				}
				lineNumber++;
			}
		}

		return ast;
	}

	private boolean checkSemanticSetPosition(AST astTemp, List<String> idList,
			Integer lineNumber) {
		// Chequeo si las expresiones son iguales o menores a cero, si pasa
		// �sto, arroja error. Si no, reemplazar el resultado en el AST.
		// En este caso, son dos expresiones a verificar.

		// Se valida si la lista est� vac�a, osea que no se realiz� ning�n
		// create. Si est� vac�a, debe fallar y arrojar error, sino pasa a
		// recorrer la lista.
		if (idList.isEmpty()) {
			// ID INEXISTENTE, se imprime el error y se devuelve true
			System.out.println("Se ha detectado un error en la linea #"
					+ lineNumber + ": el ID " + astTemp.getChild(2).getValue()
					+ " no existe.");
			return true;
		}
		
		// 1- Se recorre el AST de la EXPRESSION 1
		visitAST(astTemp.getChild(0));
		
		// 2- Se pasa de polaca a polaca inversa
		postfixNotation = preToPost(infixNotation);

		// 3- Se revuelve la EXPRESSION 1
		result = resolveExpression(postfixNotation.trim());

		// 4 - Se crea AST con resultado
		AST astResult = new AST();
		astResult.setValue(result);
		astResult.setLineNumber(lineNumber);

		// 5 - Se reemplaza la EXPRESSION 1 por el RESULTADO
		astTemp.replace(astTemp.getChild(0), astResult);
		
		// 6 - Se recorre el AST de la EXPRESSION 2
		infixNotation = "";
		visitAST(astTemp.getChild(1));
		
		// 7 - Se pasa de polaca a polaca inversa
		postfixNotation = preToPost(infixNotation);

		// 8- Se revuelve la EXPRESSION 2
		result = resolveExpression(postfixNotation.trim());

		// 9 - Se crea AST con resultado
		astResult = new AST();
		astResult.setValue(result);
		astResult.setLineNumber(lineNumber);

		// 10 - Se reemplaza la EXPRESSION 2 por el RESULTADO
		astTemp.replace(astTemp.getChild(1), astResult);
		
		// 11 - Se valida si existe el ID en la lista de IDs existentes.
		// Si existe, debe procesar y, si no existe, la funci�n es inv�lida
		for (String id : idList) {
			if (!id.equalsIgnoreCase(astTemp.getChild(2).getValue())) {
				// ID INEXISTENTE, se imprime el error y se devuelve true
				System.out.println("Se ha detectado un error en la linea #"
						+ lineNumber + ": el ID "
						+ astTemp.getChild(2).getValue() + " no existe.");
				return true;
			}
		}

		System.out.println("La semantica de la linea #" + lineNumber
				+ " es correcta.");
		return false;
	}

	private boolean checkSemanticSetRadius(AST astTemp, List<String> idList,
			Integer lineNumber) {
		// Chequeo si la expresi�n es igual o menor a cero, si pasa �sto, arroja
		// error. Si no, reemplazar el resultado en el AST.

		// Se valida si la lista est� vac�a, osea que no se realiz� ning�n
		// create. Si est� vac�a, debe fallar y arrojar error, sino pasa a
		// recorrer la lista.
		if (idList.isEmpty()) {
			// ID INEXISTENTE, se imprime el error y se devuelve true
			System.out.println("Se ha detectado un error en la linea #"
					+ lineNumber + ": el ID " + astTemp.getChild(1).getValue()
					+ " no existe.");
			return true;
		}
		
		// 1- Se recorre el AST de la EXPRESSION
		visitAST(astTemp.getChild(0));

		// 2- Se pasa de polaca a polaca inversa
		postfixNotation = preToPost(infixNotation);

		// 3- Se revuelve la EXPRESSION
		result = resolveExpression(postfixNotation.trim());

		// 4 - Se crea AST con resultado
		AST astResult = new AST();
		astResult.setValue(result);
		astResult.setLineNumber(lineNumber);

		// 5 - Se reemplaza la EXPRESSION por el RESULTADO
		astTemp.replace(astTemp.getChild(0), astResult);

		// 6 - Se valida si existe el ID en la lista de IDs existentes.
		// Si existe, debe procesar y, si no existe, la funci�n es inv�lida
		for (String id : idList) {
			if (!id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				// ID INEXISTENTE, se imprime el error y se devuelve true
				System.out.println("Se ha detectado un error en la linea #"
						+ lineNumber + ": el ID "
						+ astTemp.getChild(1).getValue() + " no existe.");
				return true;
			}
		}

		System.out.println("La semantica de la linea #" + lineNumber
				+ " es correcta.");
		return false;
	}

	private boolean checkSemanticSetHeight(AST astTemp, List<String> idList,
			Integer lineNumber) {
		// Chequeo si la expresi�n es igual o menor a cero, si pasa �sto, arroja
		// error. Si no, reemplazar el resultado en el AST.

		// Se valida si la lista est� vac�a, osea que no se realiz� ning�n
		// create. Si est� vac�a, debe fallar y arrojar error, sino pasa a
		// recorrer la lista.
		if (idList.isEmpty()) {
			// ID INEXISTENTE, se imprime el error y se devuelve true
			System.out.println("Se ha detectado un error en la linea #"
					+ lineNumber + ": el ID " + astTemp.getChild(1).getValue()
					+ " no existe.");
			return true;
		}
		
		// 1- Se recorre el AST de la EXPRESSION
				visitAST(astTemp.getChild(0));

		// 2- Se pasa de polaca a polaca inversa
		postfixNotation = preToPost(infixNotation);

		// 3- Se revuelve la EXPRESSION
		result = resolveExpression(postfixNotation.trim());

		// 4 - Se crea AST con resultado
		AST astResult = new AST();
		astResult.setValue(result);
		astResult.setLineNumber(lineNumber);

		// 5 - Se reemplaza la EXPRESSION por el RESULTADO
		astTemp.replace(astTemp.getChild(0), astResult);

		// 6- Se valida si existe el ID en la lista de IDs existentes.
		// Si existe, debe procesar y, si no existe, la funci�n es inv�lida
		for (String id : idList) {
			if (!id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				// ID INEXISTENTE, se imprime el error y se devuelve true
				System.out.println("Se ha detectado un error en la linea #"
						+ lineNumber + ": el ID "
						+ astTemp.getChild(1).getValue() + " no existe.");
				return true;
			}
		}

		System.out.println("La semantica de la linea #" + lineNumber
				+ " es correcta.");
		return false;
	}

	private boolean checkSemanticSetBase(AST astTemp, List<String> idList,
			Integer lineNumber) {
		// Chequeo si la expresi�n es igual o menor a cero, si pasa �sto, arroja
		// error. Si no, reemplazar el resultado en el AST.

		// Se valida si la lista est� vac�a, osea que no se realiz� ning�n
		// create. Si est� vac�a, debe fallar y arrojar error, sino pasa a
		// recorrer la lista.
		if (idList.isEmpty()) {
			// ID INEXISTENTE, se imprime el error y se devuelve true
			System.out.println("Se ha detectado un error en la linea #"
					+ lineNumber + ": el ID " + astTemp.getChild(1).getValue()
					+ " no existe.");
			return true;
		}

		// 1- Se recorre el AST de la EXPRESSION
		visitAST(astTemp.getChild(0));

		// 2- Se pasa de polaca a polaca inversa
		postfixNotation = preToPost(infixNotation);

		// 3- Se revuelve la EXPRESSION
		result = resolveExpression(postfixNotation.trim());

		// 4 - Se crea AST con resultado
		AST astResult = new AST();
		astResult.setValue(result);
		astResult.setLineNumber(lineNumber);

		// 5 - Se reemplaza la EXPRESSION por el RESULTADO
		astTemp.replace(astTemp.getChild(0), astResult);

		// 6- Se valida si existe el ID en la lista de IDs existentes.
		// Si existe, debe procesar y, si no existe, la funci�n es inv�lida
		for (String id : idList) {
			if (!id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				// ID INEXISTENTE, se imprime el error y se devuelve true
				System.out.println("Se ha detectado un error en la linea #"
						+ lineNumber + ": el ID "
						+ astTemp.getChild(1).getValue() + " no existe.");
				return true;
			}
		}

		System.out.println("La semantica de la linea #" + lineNumber
				+ " es correcta.");
		return false;
	}

	private boolean checkSemanticSetColor(AST astTemp, List<String> idList,
			Integer lineNumber) {

		// Se valida si la lista est� vac�a, osea que no se realiz� ning�n
		// create. Si est� vac�a, debe fallar y arrojar error, sino pasa a
		// recorrer la lista.
		if (idList.isEmpty()) {
			// ID INEXISTENTE, se imprime el error y se devuelve true
			System.out.println("Se ha detectado un error en la linea #"
					+ lineNumber + ": el ID " + astTemp.getChild(1).getValue()
					+ " no existe.");
			return true;
		}

		// 1- Se valida si existe el ID en la lista de IDs existentes.
		// Si existe, debe procesar y, si no existe, la funci�n es inv�lida
		for (String id : idList) {
			if (!id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				// ID INEXISTENTE, se imprime el error y se devuelve true
				System.out.println("Se ha detectado un error en la linea #"
						+ lineNumber + ": el ID "
						+ astTemp.getChild(1).getValue() + " no existe.");
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
				// ID EXISTENTE, se imprime el error y se devuelve true
				System.out.println("Se ha detectado un error en la linea #"
						+ lineNumber + ": el ID "
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
		// Recorre el AST de la EXPRESSION y la rearma en notacion polaca

		// Se va rearmando EXPRESSION
		infixNotation = infixNotation + " " + ast.getValue();

		// Se obtiene una lista de los hijos de la raiz
		List<AST> listChildren = ast.listChildren();

		for (AST tmp : listChildren) {
			// El hijo tiene hijos y por cada uno se invoca de nuevo el metodo
			// para imprimirlos
			visitAST(tmp);
		}
	}

	public String preToPost(String infixNotation) {
		System.out.println("infixNotation " + infixNotation);
		// Conversor de polaca a polaca inversa para luego poder resolver la
		// operacion
		String strPostfix = "";

		Stack<String> operatorStack = new Stack<String>();

		char[] prefixExp = infixNotation.toCharArray();
		for (int i = 0; i < prefixExp.length; i++) {
			// Se ignora si es blanco
			if (prefixExp[i] == ' ') {
				continue;
			}

			if (isOperator(prefixExp[i])) {
				operatorStack.push(String.valueOf(prefixExp[i]));
			} else {
				strPostfix = strPostfix + " " + String.valueOf(prefixExp[i]);

				while (!operatorStack.empty()
						&& operatorStack.peek().equals(LEFT_DONE)) {
					operatorStack.pop();

					strPostfix = strPostfix + " " + operatorStack.pop();
				}
				operatorStack.push(LEFT_DONE);
			}
		}

		// Devuelve en polaca inversa
		return strPostfix;

	}

	private static boolean isOperator(char c) {
		// Evalua si el char recibido es operador

		char[] operators = { '+', '-', '/', '*' };
		boolean isOp = false;
		for (int i = 0; i < operators.length; i++) {
			if (c == operators[i]) {
				isOp = true;
				break;
			}
		}
		return isOp;
	}

	private String resolveExpression(String infixNotation) {
		// Se limpia la expression
		String[] post = infixNotation.split(" ");

		// Declaraci�n de las pilas
		Stack<String> E = new Stack<String>(); // Pila entrada
		Stack<String> P = new Stack<String>(); // Pila de operandos

		// A�adir post (array) a la Pila de entrada
		for (int i = post.length - 1; i >= 0; i--) {
			E.push(post[i]);
		}

		// Algoritmo de Evaluaci�n Expression
		String operadores = "+-*/%";
		while (!E.isEmpty()) {
			if (operadores.contains("" + E.peek())) {
				P.push(evaluar(E.pop(), P.pop(), P.pop()) + "");
			} else {
				P.push(E.pop());
			}
		}

		// Mostrar resultados:
		return P.peek();

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
}