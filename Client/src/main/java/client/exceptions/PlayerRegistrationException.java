package client.exceptions;

/**
 * PlayerRegistrationException is a subclass of the Exception class.
 * PlayerRegistrationException is thrown if a registration request fails.
 * When this exception is thrown the program terminates.
 * 
 * @author Adem
 *
 */
public class PlayerRegistrationException extends Exception {

	private static final long serialVersionUID = 2L;

	public PlayerRegistrationException(String message) {
		super(message);
	}

}
