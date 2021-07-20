package client.exceptions;

/**
 * GameStateException is a subclass of the RuntimeException class and so an unchecked exception.
 * GameStateException is thrown if a gameState GET request to the server is answered with an Error response message.
 * 
 * @author Adem
 *
 */
public class GameStateException extends RuntimeException {


	public GameStateException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
