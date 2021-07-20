package client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.controller.GameController;
import client.exceptions.FailedMoveException;
import client.exceptions.MapException;
import client.exceptions.PlayerRegistrationException;
import client.network.Network;


public class MainClient {
	
	static Logger logger = LoggerFactory.getLogger(MainClient.class);

	public static void main(String[] args) {

		 
		
		String serverBaseUrl = args[1];
		String gameId = args[2];
		
		Network network = new Network(serverBaseUrl, gameId);
		GameController controller = new GameController(network);
		
		try {
			network.registerPlayer();
			controller.generateMap();
			controller.startMoving();
		} catch (PlayerRegistrationException e) {
			logger.error("Client Player registration Failed!", e);
		}catch (MapException e) {
			logger.error("Client Map sending Failed!", e);
		} catch (FailedMoveException e) {
			logger.error("Client Move sending Failed!", e);
		}
		
	}
}
