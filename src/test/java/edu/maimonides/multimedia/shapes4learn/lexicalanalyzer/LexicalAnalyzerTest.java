package edu.maimonides.multimedia.shapes4learn.lexicalanalyzer;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.maimonides.multimedia.shapes4learn.analysis.LexicalAnalyzer;
import edu.maimonides.multimedia.shapes4learn.interpreter.CodeException;
import edu.maimonides.multimedia.shapes4learn.model.ShapeAmbient;
import edu.maimonides.multimedia.shapes4learn.model.Token;
import edu.maimonides.multimedia.shapes4learn.model.impl.BasicShapeAmbient;

/**
 * Class LexicalAnalyzerTest to test the different cases of the lexical
 * analyzer.
 * 
 * @author Becerra-Gorino-Moraguez-Pernetta
 *
 */

public class LexicalAnalyzerTest {

	private LexicalAnalyzer analizadorLexico;
	private ShapeAmbient ambient;

	@Before
	public void setUp() throws Exception {
		analizadorLexico = new LexicalAnalyzer();
		ambient = new BasicShapeAmbient();
	}

	@Test
	public void testCorrectSentences() {

		List<Token> tokens = new LinkedList<>();
		try {
			tokens.addAll(analizadorLexico.analyze("create circle circulo;"));
			for (Token token : tokens) {
				if (!token.getValidez()) {
					fail("Test Failed! - Incorrect Token");
				}
			}
		} catch (CodeException e) {
			fail("Test Failed! - Code Exception");
			// e.printStackTrace();
		}
	}

	@Test
	public void testIncorrectSentences() {

		List<Token> tokens = new LinkedList<>();
		try {
			tokens.addAll(analizadorLexico.analyze("create circ4le circulo;"));
			Iterator<Token> iterator = tokens.iterator();

			while (iterator.hasNext()) {
				System.out.println(iterator.next().getTipoToken()
						+ iterator.next().getLexema()
						+ iterator.next().getValidez());
				if (iterator.next().getValidez()) {
					System.out.println("Paso: "
							+ iterator.next().getTipoToken());
					fail("Test Failed! - Incorrect Token");
				}
			}
		} catch (CodeException e) {
			fail("Test Failed! - Code Exception");
			// e.printStackTrace();
		}
	}

}
