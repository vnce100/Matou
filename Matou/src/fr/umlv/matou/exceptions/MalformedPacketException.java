package fr.umlv.matou.exceptions;

public class MalformedPacketException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public MalformedPacketException() {
		super();
	}
	
	public MalformedPacketException(String message) {
		super(message);
	}
}
