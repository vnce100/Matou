package fr.umlv.matou;

import fr.umlv.matou.exceptions.MalformedPseudoException;
import fr.umlv.matou.utils.PseudoChecker;

public class User {
	private final String pseudo;
	
	/**
	 * 
	 * @param pseudo
	 */
	public User(String pseudo) {
		if(!PseudoChecker.isConformed(pseudo)) {
			throw new MalformedPseudoException();
		}
		this.pseudo = pseudo;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPseudo() {
		return pseudo;
	}
}
