package client.controller;

import client.enums.PlayerGameState;
import client.exceptions.FailedMoveException;
import client.exceptions.GameStateException;
import client.exceptions.MapException;
import client.exceptions.PlayerRegistrationException;
import client.map.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.cli.GameVisualisation;
import client.enums.CPlayerMove;
import client.model.ClientGameState;
import client.network.Network;

@SuppressWarnings("unused")
public class GameController {
	
	static Logger logger = LoggerFactory.getLogger(GameController.class);

	private Network network; 
	private MoveController moveController; 
	private MapController mapController; 
	private ClientGameState gameState; 
	private GameVisualisation cli; 

	public GameController(Network network) {		
		this.network = network;
		this.mapController = new MapController();
		this.moveController = new MoveController();
		this.gameState = new ClientGameState();
		this.cli  = new GameVisualisation(gameState);
	}
	
	
	/**
	 * Method that starts the Map generation sending process.
	 * After a valid map was generated, the method waits until it is the players turn to act and sends the map
	 * @throws MapException 
	 */
	public void generateMap() throws MapException {
		
		Map map = mapController.generateMap();
		checkTurn();
		network.sendMap(map);
	}
	
	
	
	/**
	 * Starts the route calculation and sending of moves upon the players turn to act until the game is finished.
	 * @throws FailedMoveException 
	 * 
	 */
	public void startMoving() throws FailedMoveException{
		
		while(!gameState.gameFinished()) {
			checkTurn();
			moveController.setMap(gameState.getMap());
				
			if(!gameState.gameFinished()) {
				try {
					CPlayerMove move = moveController.getNextMove(gameState.isMyPTreasureState());
					network.sendMove(move);
				} catch (FailedMoveException e) {
					logger.error("Client Move sending Failed!", e);
				}
				
				
			}
		}
		
	}
	

	
	/**
	 * Method that periodically invokes the Network function to check current GameState until it is the Players turn to act.
	 * If an Error happens in the process, the error message will be logged.
	 */
	private void checkTurn() {
		
		do {
			try {
				Thread.sleep(400);
				network.getClientState(gameState);
			} catch (InterruptedException e) {
				logger.error("Thread Error in checkTurn() " + e);
			} catch (GameStateException e) {
				logger.error("GameState Request Failed!", e);
			}
			
		}while(gameState.getMyEPlayerState().equals(PlayerGameState.ShouldWait));
	}
	
}
