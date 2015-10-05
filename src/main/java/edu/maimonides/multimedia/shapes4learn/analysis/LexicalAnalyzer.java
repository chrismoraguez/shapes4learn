package edu.maimonides.multimedia.shapes4learn.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
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
		
		
		
		StringTokenizer stringTokenizer = new StringTokenizer(code);
		
		String tokenActual = "";
		
        while (stringTokenizer.hasMoreTokens()){
        	
        	// create circle circulo;
        	tokenActual = stringTokenizer.nextToken();        	
        	
        	
        	
        	
        	
        	
        	
        	
        	token.setTipoToken("create");
    		token.setLexema("\\b(create)\\b");
        	tokenActual.equals(token.getLexema());
        	
        }

		
			
		
			
		
		// TODO Implement.
		
		return tokens;
	}

	@Override
	public void interpret(String code, ShapeAmbient ambient)
			throws CodeException {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void interpret(InputStream stream, ShapeAmbient ambient)
			throws CodeException, IOException {
		// TODO Auto-generated method stub
		
	}
}
