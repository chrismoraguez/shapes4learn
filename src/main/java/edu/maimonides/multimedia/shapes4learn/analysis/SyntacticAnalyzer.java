package edu.maimonides.multimedia.shapes4learn.analysis;

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

	public String lookahead;

	public AST analyze(List<Token> tokens) throws SyntacticException {
		AST ast = new AST();

		// Si hay lexemas desconocidos, � es necesario igual realizar el
		// an�lisis sint�ctico ?

		for (Token token : tokens) {
			lookahead = token.getTipoToken();

			System.out.println("Token: " + token.getLexema());
			if (!lookahead.equals(";")) {
				
			}
		}

		checkOrder();

		return ast;
	}

	private void checkOrder() {

	}
}