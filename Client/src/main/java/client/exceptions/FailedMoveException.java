package client.exceptions;

/**
 * FailedMoveException is a subclass of the Exception class.
 * FailedMoveException is thrown if a move POST request to the server is answered with an Error response message.
 * 
 * @author Adem
 *
 */
public class FailedMoveException extends Exception {

	public FailedMoveException(String message) {
		super(message);
	}


	
}
