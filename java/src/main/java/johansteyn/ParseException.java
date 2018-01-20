package johansteyn;

/**
 * An exception that can occur while parsing a Paint Shop input file,
 * due to incorrect file contents.
 */
public class ParseException extends Exception {
	/**
	 * @param message Explain the reason for the parse exception.
	 */
	public ParseException(String message) {
		super(message);
	}
}

