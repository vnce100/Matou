package fr.umlv.matou.exceptions;

public class MalformedPseudoException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public MalformedPseudoException() {
		super();
	}
	
	public MalformedPseudoException(String message) {
		super(message);
	}
}
