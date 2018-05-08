package fr.umlv.matou.utils;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

public class MatouScanner {
	
	/**
	 * Scan one line on the system input.
	 * @return return the scanned line. May return null if scanner.hasNext() block failed.
	 */
	public static String scanLine(InputStream is) {
		try(Scanner scanner = new Scanner(Objects.requireNonNull(is))) {
			while(!scanner.hasNext()) {}
			return scanner.next();
		}
	}
}
