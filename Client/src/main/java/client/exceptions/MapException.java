package client.exceptions;

/**
 * MapException is checked exception that extends the super class Exception.
 * The MapException is thrown if a not valid map is generated.
 * 
 * @author Adem
 *
 */
public class MapException extends Exception {

	public MapException(String message) {
		super(message);
	}

}
