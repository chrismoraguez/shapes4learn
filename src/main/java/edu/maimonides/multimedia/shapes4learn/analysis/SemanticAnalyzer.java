package edu.maimonides.multimedia.shapes4learn.analysis;

import java.util.ArrayList;
import java.util.List;

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

	public SemanticAnalyzer() {
	}

	public List<String> idList = new ArrayList<String>();

	public AST analyze(AST ast) throws SemanticException {

		System.out.println("\n-----------------------------------\n");
		System.out.println("Análisis Semántico:\n");

		List<AST> listChildren = ast.listChildren();
		boolean errorSemantico = false;

		System.out.println("\nFunciones: \n");
		for (AST astTemp : listChildren) {

			System.out.println(astTemp.getValue());
			switch (astTemp.getValue()) {
			case "create":
				errorSemantico = checkSemanticCreate(astTemp, idList);
				break;
			case "setcolor":
				errorSemantico = checkSemanticSetColor(astTemp, idList);
				break;
			case "setbase":
				errorSemantico = checkSemanticSetBase(astTemp, idList);
				break;
			case "setheight":
				errorSemantico = checkSemanticSetHeight(astTemp, idList);
				break;
			case "setradius":
				errorSemantico = checkSemanticSetRadius(astTemp, idList);
				break;
			case "setposition":
				errorSemantico = checkSemanticSetPosition(astTemp, idList);
				break;
			}
		}

		if (errorSemantico) {

		}

		return ast;
	}

	private boolean checkSemanticSetPosition(AST astTemp, List<String> idList2) {
		// Chequeo si las expresiones son iguales o menores a cero, si pasa
		// ésto, arroja error. Si no, reemplazar el resultado en el AST.
		// En este caso, son dos expresiones a verificar.

		// Chequeo si existe el ID en la lista de IDs existentes y, si no
		// existe, arroja error
		for (String id : idList) {
			if (id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				return false;
			}
		}
		return true;

	}

	private boolean checkSemanticSetRadius(AST astTemp, List<String> idList2) {
		// Chequeo si la expresión es igual o menor a cero, si pasa ésto, arroja
		// error. Si no, reemplazar el resultado en el AST.

		// Chequeo si existe el ID en la lista de IDs existentes y, si no
		// existe, arroja error
		for (String id : idList) {
			if (id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				return false;
			}
		}
		return true;
	}

	private boolean checkSemanticSetHeight(AST astTemp, List<String> idList2) {
		// Chequeo si la expresión es igual o menor a cero, si pasa ésto, arroja
		// error. Si no, reemplazar el resultado en el AST.

		// Chequeo si existe el ID en la lista de IDs existentes y, si no
		// existe, arroja error
		for (String id : idList) {
			if (id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				return false;
			}
		}
		return true;
	}

	private boolean checkSemanticSetBase(AST astTemp, List<String> idList2) {
		// Chequeo si la expresión es igual o menor a cero, si pasa ésto, arroja
		// error. Si no, reemplazar el resultado en el AST.

		// Chequeo si existe el ID en la lista de IDs existentes y, si no
		// existe, arroja error
		for (String id : idList) {
			if (id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				return false;
			}
		}
		return true;
	}

	private boolean checkSemanticSetColor(AST astTemp, List<String> idList2) {
		// Chequeo si existe el ID en la lista de IDs existentes y, si no
		// existe, arroja error
		for (String id : idList) {
			if (id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				return false;
			}
		}
		return true;
	}

	private boolean checkSemanticCreate(AST astTemp, List<String> idList) {

		// Chequeo si existe el ID en la lista de IDs existentes. Si existe,
		// debe arrojar error y, si no
		// existe, lo agrego
		for (String id : idList) {
			if (id.equalsIgnoreCase(astTemp.getChild(1).getValue())) {
				return true;
			}
		}
		return false;
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
			visitAST(tmp);
		}
	}
}