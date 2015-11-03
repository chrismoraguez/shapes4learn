package edu.maimonides.multimedia.shapes4learn.analysis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.WordUtils;

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
	public Integer lineNumber=1;

	public AST analyze(List<Token> tokens) throws SyntacticException {
		AST ast = new AST();
		List<Token> sentenceGrammar = new ArrayList<>();

		System.out.println("-----------------------------------");
		System.out.println("Analisis Sintactico \n");
		
		for (Token token : tokens) {
			tipoToken = token.getTipoToken();

			// Se valida que el token no indique fin de sentencia
			if (!tipoToken.equals("Fin de sentencia")) {
				// No es fin de sentencia, se va reconstruyendo la sentencia
				sentenceGrammar.add(token);
			} else {
				// Es fin de sentencia, se cierra la sentencia y se manda a analizar sintacticamente
				sentenceGrammar.add(token);
				checkSentence(sentenceGrammar,lineNumber);
				sentenceGrammar = new ArrayList<>(); 
				lineNumber++;
			}
		}

		return ast;
	}

	private void checkSentence(List<Token> sentences, Integer lineNumber) {
		// Se analiza sintacticamente cada sentencia recibida
		String lookahead = sentences.get(0).getTipoToken();
		Integer wordNumber = 0;
		Boolean validity = false;

		for (Token word : sentences) {
			wordNumber++;

			// Dependiendo la accion ingresada por el usuario se valida su sintaxis
			switch (lookahead) {
			case "create":
				validity = checkOrderCreate(word, wordNumber,lineNumber);
				break;
			case "setcolor":
				validity = checkOrderSetColor(word, wordNumber,lineNumber);
				break;
			case "setbase":
				checkOrderSetBase(word, wordNumber,lineNumber);
				break;
			case "setheight":
				checkOrderSetHeight(word, wordNumber,lineNumber);
				break;
			case "setradius":
				checkOrderSetRadius(word, wordNumber,lineNumber);
				break;
			case "setposition":
				// checkOrderSetPosition();
				break;
			default:
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba definicion de accion create | setcolor | setbase | setheight | setradius");
				System.out.println();
			}
			
			if (!validity){
				// Se detecto algun error en la sentencia, se finaliza el analisis sintactico de esa sentencia
				break;
			}
		}
		
		// Dependiendo la accion ingresada por el usuario se valida que la cantidad de comandos sea la correcta
		if(validity){
			checkCommands(lookahead,wordNumber);
		}
	}

	private boolean checkOrderCreate(Token word, Integer wordNumber, Integer lineNumber) {
		// Debe ser CREATE
		if (wordNumber == 1) {
			if(word.getTipoToken().equals("create")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				return false;
			}
		}

		// Debe ser SHAPE 
		if (wordNumber == 2) {
			if(word.getTipoToken().equals("forma")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo SHAPE");
				System.out.println();
				return false;
			}
		}

		// Debe ser ID
		if (wordNumber == 3) {
			if(word.getTipoToken().equals("id")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo ID");
				System.out.println();
				return false;
			}
		}
		
		// Debe ser FIN DE SENTENCIA
		if (wordNumber == 4) {
			if(word.getTipoToken().equals("Fin de sentencia")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba fin de sentencia ;");
				System.out.println();
				return false;
			}
		}
		
		return false;
	}
	
	
	private boolean checkOrderSetColor(Token word, Integer wordNumber, Integer lineNumber) {
		// Debe ser SETCOLOR
		if (wordNumber == 1) {
			if(word.getTipoToken().equals("setcolor")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				return false;
			}
		}

		// Debe ser COLOR_DEF 
		if (wordNumber == 2) {
			if(word.getTipoToken().equals("color")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo SHAPE");
				System.out.println();
				return false;
			}
		}

		// Debe ser IN
		if (wordNumber == 3) {
			if(word.getTipoToken().equals("in")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo ID");
				System.out.println();
				return false;
			}
		}
		
		// Debe ser SHAPE
		if (wordNumber == 4) {
			if(word.getTipoToken().equals("forma")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba fin de sentencia ;");
				System.out.println();
				return false;
			}
		}
		
		// Debe ser ID
		if (wordNumber == 5) {
			if(word.getTipoToken().equals("id")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo ID");
				System.out.println();
				return false;
			}
		}
		
		// Debe ser FIN DE SENTENCIA
		if (wordNumber == 6) {
			if(word.getTipoToken().equals("Fin de Sentencia")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba fin de sentencia ;");
				System.out.println();
				return false;
			}
		}
		
		return false;
	}
	
	private boolean checkOrderSetBase(Token word, Integer wordNumber, Integer lineNumber) {
		// Debe ser SETBASE
		if (wordNumber == 1) {
			if(word.getTipoToken().equals("setbase")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println();
				return false;
			}
		}

		// Debe ser EXPRESSION 
		if (wordNumber == 2) {
			if(word.getTipoToken().equals("expresion")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo SHAPE");
				System.out.println();
				return false;
			}
		}

		// Debe ser IN
		if (wordNumber == 3) {
			if(word.getTipoToken().equals("in")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo ID");
				System.out.println();
				return false;
			}
		}
		
		// Debe ser RECTANGLE
		if (wordNumber == 4) {
			if(word.getLexema().equals("rectangle")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba la palabra RECTANGLE");
				System.out.println();
				return false;
			}
		}
		
		// Debe ser ID
		if (wordNumber == 5) {
			if(word.getTipoToken().equals("id")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo ID");
				System.out.println();
				return false;
			}
		}
		
		// Debe ser FIN DE SENTENCIA
		if (wordNumber == 6) {
			if(word.getTipoToken().equals("Fin de Sentencia")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba fin de sentencia ;");
				System.out.println();
				return false;
			}
		}
				
		return false;
	}
	
	private boolean checkOrderSetHeight(Token word, Integer wordNumber, Integer lineNumber) {
		// Debe ser SETHEIGHT
		if (wordNumber == 1) {
			if(word.getTipoToken().equals("setheight")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				return false;
			}
		}

		// Debe ser EXPRESSION 
		if (wordNumber == 2) {
			if(word.getTipoToken().equals("expresion")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo SHAPE");
				System.out.println();
				return false;
			}
		}

		// Debe ser IN
		if (wordNumber == 3) {
			if(word.getTipoToken().equals("in")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo ID");
				System.out.println();
				return false;
			}
		}
		
		// Debe ser RECTANGLE
		if (wordNumber == 4) {
			if(word.getLexema().equals("rectangle")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba la palabra RECTANGLE");
				System.out.println();
				return false;
			}
		}
		
		// Debe ser ID
		if (wordNumber == 5) {
			if(word.getTipoToken().equals("id")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo ID");
				System.out.println();
				return false;
			}
		}
		
		// Debe ser FIN DE SENTENCIA
		if (wordNumber == 6) {
			if(word.getTipoToken().equals("Fin de Sentencia")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba fin de sentencia ;");
				System.out.println();
				return false;
			}
		}
				
		return false;
	}
	
	private boolean checkOrderSetRadius(Token word, Integer wordNumber, Integer lineNumber) {
		// Debe ser RADIUS
		if (wordNumber == 1) {
			if(word.getTipoToken().equals("setheight")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				return false;
			}
		}

		// Debe ser EXPRESSION 
		if (wordNumber == 2) {
			if(word.getTipoToken().equals("expresion")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo SHAPE");
				System.out.println();
				return false;
			}
		}

		// Debe ser IN
		if (wordNumber == 3) {
			if(word.getTipoToken().equals("in")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo ID");
				System.out.println();
				return false;
			}
		}
		
		// Debe ser CIRCLE
		if (wordNumber == 4) {
			if(word.getLexema().equals("circle")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba la palabra RECTANGLE");
				System.out.println();
				return false;
			}
		}
		
		// Debe ser ID
		if (wordNumber == 5) {
			if(word.getTipoToken().equals("id")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba palabra del tipo ID");
				System.out.println();
				return false;
			}
		}
		
		// Debe ser FIN DE SENTENCIA
		if (wordNumber == 6) {
			if(word.getTipoToken().equals("Fin de Sentencia")){
				return true;
			}else{
				System.out.println("Se ha detectado un error en la linea #"+lineNumber+", palabra #" + wordNumber);
				System.out.println("Error: Se esperaba fin de sentencia ;");
				System.out.println();
				return false;
			}
		}
				
		return false;
	}
	
	private void checkCommands(String lookahead, Integer words) {
		
		// CREATE 
		if((lookahead.equals("create"))&&(words==4)){
			System.out.println("La sintaxis de la linea #" + lineNumber + " es correcta \n");
		}else if(lookahead.equals("create")){
			System.out.println("Se ha detectado un error en la linea #" + lineNumber);
			System.out.println("Error: La cantidad de Comandos recibidos es inferior a la esperada para CREATE \n");
		}
		
		// SETCOLOR 
		if((lookahead.equals("setcolor"))&&(words==6)){
			System.out.println("La sintaxis de la linea #" + lineNumber + " es correcta \n");
		}else if(lookahead.equals("setcolor")){
			System.out.println("Se ha detectado un error en la linea #" + lineNumber);
			System.out.println("Error: La cantidad de Comandos recibidos es inferior a la esperada para SETCOLOR \n");
		}
		
		// SETBASE 
		if((lookahead.equals("setbase"))&&(words==6)){
			System.out.println("La sintaxis de la linea #" + lineNumber + " es correcta \n");
		}else if (lookahead.equals("setbase")){
			System.out.println("Se ha detectado un error en la linea #" + lineNumber);
			System.out.println("Error: La cantidad de Comandos recibidos es inferior a la esperada para SETBASE \n");
		}
		
		// SETHEIGHT 
		if((lookahead.equals("setheight"))&&(words==6)){
			System.out.println("La sintaxis de la linea #" + lineNumber + " es correcta \n");
		}else if (lookahead.equals("setheight")){
			System.out.println("Se ha detectado un error en la linea #" + lineNumber);
			System.out.println("Error: La cantidad de Comandos recibidos es inferior a la esperada para SETHEIGHT \n");
		}
		
		// SETRADIUS 
		if((lookahead.equals("setradius"))&&(words==6)){
			System.out.println("La sintaxis de la linea #" + lineNumber + " es correcta \n");
		}else if (lookahead.equals("setradius")){
			System.out.println("Se ha detectado un error en la linea #" + lineNumber);
			System.out.println("Error: La cantidad de Comandos recibidos es inferior a la esperada para SETRADIUS \n");
		}
	}

}