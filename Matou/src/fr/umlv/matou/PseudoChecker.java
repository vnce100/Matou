package fr.umlv.matou;

public class PseudoChecker {
	private final static int MAX_PSEUDO_SIZE = 20;
	
	/**
	 * Check if the string in parameter is a valid pseudo. 
	 * @param pseudo is the string to check.
	 * @return true if string is a pseudo. Else, return false.
	 */
	public static boolean isConformed(String pseudo) {
		return isShort(pseudo) & isAlphaNum(pseudo);
	}
	
	private static boolean isAlphaNum(String pseudo) {
		for(int i=0; i<pseudo.length(); i++) {
			if(!Character.isLetter(pseudo.charAt(i)) || !Character.isDigit(pseudo.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	private static boolean isShort(String pseudo) {
		return pseudo.length() < MAX_PSEUDO_SIZE;
	}
}
