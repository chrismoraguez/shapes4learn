package edu.maimonides.multimedia.shapes4learn.model.exceptions;

/**
 * This exception can be thrown where a user provides a shape id and there is no
 * shape related to it.
 * 
 * @author Matias Giorgio
 * 
 */
public class InexistentShapeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InexistentShapeException() {
	}

	public InexistentShapeException(String message) {
		super(message);
	}
}
