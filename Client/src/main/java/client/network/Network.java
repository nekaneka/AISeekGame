package client.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import MessagesBase.ERequestState;
import MessagesBase.HalfMap;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import client.enums.CPlayerMove;
import client.exceptions.FailedMoveException;
import client.exceptions.GameStateException;
import client.exceptions.MapException;
import client.exceptions.PlayerRegistrationException;
import client.map.Map;
import client.marshaller.Marshaller;
import client.model.ClientGameState;
import reactor.core.publisher.Mono;
/**
 * Network class is used to communicate to the server with the MessageBase functionality.
 * 
 * @author Adem
 *
 */
public class Network {
	
	static Logger logger = LoggerFactory.getLogger(Network.class);

	private String gameId; 
	private String serverBaseUrl;  
	private String playerId; 
	private Marshaller marshaller; 
	private WebClient baseWebClient;
	
	public Network(String serverBaseUrl, String gameId) {
			this.serverBaseUrl = serverBaseUrl;
			this.gameId = gameId;
			this.marshaller = new Marshaller(); 	
	}
	
	
	
	/** 
	 * Registers player to the specified Game(gameId) with his information.
	 * The response contains a UniquePlayerId, which is saved into the 'playerId' variable and set to the Marshaller.
	 * if an Error happens a PlayerRegistrationException is thrown.
	 * 
	 * @throws PlayerRegistrationException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void registerPlayer() throws PlayerRegistrationException {		
		
		System.out.println("[ ----   PLAYER REGISTERING....   ---- ]");
	
		try {
			this.baseWebClient = WebClient.builder().baseUrl(this.serverBaseUrl + "/games")
					.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) //the network protocol uses XML
				    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
				    .build();
			
			PlayerRegistration playerReg = new PlayerRegistration("Adem", "Mehremic", "01650669");
			
			Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
					.uri("/" + gameId + "/players")
					.body(BodyInserters.fromValue(playerReg)) 
					.retrieve()
					.bodyToMono(ResponseEnvelope.class); 
			
			ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();
			
			if(resultReg.getState() == ERequestState.Error) {
				throw new PlayerRegistrationException("Player registration FAILED! Error message: " + resultReg.getExceptionMessage());
			}
			else {
				UniquePlayerIdentifier uniqueID = resultReg.getData().get();
				this.playerId = uniqueID.getUniquePlayerID();
				this.marshaller.setPlayerId(this.playerId);
				logger.info("[ ----  PLAYER REGISTERED   ---- ]");
			}
		}catch (Exception e) {
			throw new PlayerRegistrationException("Player registration FAILED!");
		}
		
	}
	
	
	/**
	 * The generated Map is passed to the Marshaller to be converted into a ServerMap,
	 * which is then sent with a POST request to the Server side.
	 * If and error occurs a MapException is thrown and the game ends.
	 * 
	 * @param map
	 * @throws MapException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void sendMap(Map map) throws MapException { 
		
		HalfMap halfMap = marshaller.convertToHalfMapNode(map);
		
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
				.uri("/" + gameId + "/halfmaps")
				.body(BodyInserters.fromValue(halfMap)) 
				.retrieve()
				.bodyToMono(ResponseEnvelope.class);
		
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();
		
		if(resultReg.getState() == ERequestState.Error) {
			throw new MapException("Map sending Failed! Error message: " + resultReg.getExceptionMessage());
		}
		else {
			logger.info("[ ----   MAP WAS SENT   ----- ]");
		}
	}

	
	
	/**
	 * Sends the next PlayerMove to the server. Convert the client side to server side move.
	 * If an error happens the FailedMoveException is thrown.
	 * @param move - a client side move of enum type CPlayerMove(Up, Down, Left, Right). 
	 * @throws FailedMoveException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void sendMove(CPlayerMove move) throws FailedMoveException {
				
		PlayerMove playerMove = marshaller.convertToPlayerMove(move);
		
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
				.uri("/" + gameId + "/moves")
				.body(BodyInserters.fromValue(playerMove)) // specify the data which is set to the server
				.retrieve()
				.bodyToMono(ResponseEnvelope.class); // specify the object returned by the server
		
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();
		
		if(resultReg.getState() == ERequestState.Error) {
			throw new FailedMoveException("Network Move sending Failed! Error message: " + resultReg.getExceptionMessage());
		}
	}



	/**
	 * Method that requests the current gameState from the server and passes the result to the marshaller
	 * @param clientGameState
	 * @throws GameStateException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getClientState(ClientGameState clientGameState) throws GameStateException {
		
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
				.uri("/" + gameId + "/states/" + playerId)
				.retrieve()
				.bodyToMono(ResponseEnvelope.class); 

		ResponseEnvelope<GameState> requestResult = webAccess.block();
		
		if(requestResult.getState() == ERequestState.Error) {
			throw new GameStateException("GameState Request Failed! Error message: " + requestResult.getExceptionMessage());
		}else {
			GameState gameState = requestResult.getData().get();
			if(clientGameState.setGameStateId(gameState.getGameStateId()))
				marshaller.marshallGameState(gameState, clientGameState);
		}
	}
	
	
}
