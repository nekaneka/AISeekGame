package client.marshaller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.EMove;
import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesBase.PlayerMove;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import client.enums.CPlayerMove;
import client.enums.FortState;
import client.enums.PlayerGameState;
import client.enums.PlayerPositionState;
import client.enums.Terrain;
import client.enums.TreasureState;
import client.exceptions.MarshallerException;
import client.map.Field;
import client.map.Map;
import client.model.ClientGameState;

/**
 * 
 * @author Adem
 *
 */
public class Marshaller {
	
	static Logger logger = LoggerFactory.getLogger(Marshaller.class);
	
	private String playerId;
	
	
	public void setPlayerId(String playerId) {
		if(this.playerId == null) this.playerId = playerId;
	}

	
	
	/**
	 * Method that converts the server side GamesState into our Client side GameState model
	 * with the whole map and player states.
	 * The method calls separate private methods for map and player conversion and  
	 * @param gameState
	 * @param clientGameState
	 */
	public void marshallGameState(GameState gameState, ClientGameState clientGameState) {
		clientGameState.increaseTurnCounter();
		convertMap(gameState, clientGameState);

		for(PlayerState player: gameState.getPlayers()) {
			if(player.getUniquePlayerID().equals(playerId)) {	
				clientGameState.setMyEPlayerState(convertToCientPlayerState(player.getState()));
				clientGameState.setMyPTreasureState(player.hasCollectedTreasure());
			}
		}
	}

	
	
	/**
	 * Return client side Player state based on the state of the passed Server side PlayeState
	 * @param state
	 * @return PlayerGameState
	 */
	private PlayerGameState convertToCientPlayerState(EPlayerGameState state) {
		switch (state) {
			case ShouldActNext:
				return PlayerGameState.ShouldActNext;
			case ShouldWait: 
				return PlayerGameState.ShouldWait;
			case Won:
				return PlayerGameState.Won;
			case Lost: 
				return PlayerGameState.Lost;
			default:
				logger.error("No Matching PlayerGameState type on client side for: " +  state);
				throw new MarshallerException("No Matching PlayerGameState type on client side for: " + state);
				
		}
	}

	
	
	/**
	 * Converts the Map received from the server into our Client side Map.
	 * By looping through the server map-node list new client fields are created based on the node parameters. 
	 * After the fields are created, our map-field list is updated.
	 * @param gameState
	 * @param clientGameState
	 */
	private void convertMap(GameState gameState, ClientGameState clientGameState) {
		if(gameState.getMap().isPresent() && gameState.getMap().get().getMapNodes().size() > 32) {
			FullMap fullMap = gameState.getMap().get(); 	
			List<Field> fullMapFields = new ArrayList<>();
				 
			for(FullMapNode node: fullMap.getMapNodes()) {	
				Field field = new Field(node.getX(), node.getY(), convertToClientTerrain(node));  
					
				if(field.getX() > 7) clientGameState.setHorizontalMap(true);

				field.setFortState(convertToClientFortState(node));
				field.setPlayerPositionState(convertToClientPlayerPosition(node));
				field.setTreasureState(convertToClientTreasureState(node));
				
				fullMapFields.add(field);
			}	
			clientGameState.setMap(fullMapFields); 
		}
	}

	
	
	/**
	 * Return client side treasure state of the field based on the state of the passed node
	 * @param node
	 * @return TreasureState
	 */
	private TreasureState convertToClientTreasureState(FullMapNode node) {
		
		switch (node.getTreasureState()) {
			case MyTreasureIsPresent:
				return TreasureState.MYTREASUREISPRESENT;
			case NoOrUnknownTreasureState: 
				return TreasureState.NOORUNKNOWNTREAURESTATE;
			default: 
				logger.error("No Matching TreasureState type on client side for: " +  node.getTreasureState());
				throw new MarshallerException("No Matching FortState type on client side for: " +  node.getTreasureState());
		}
	}

	
	
	/**
	 * Return client side fort state of the field based on the state of the passed node
	 * @param node
	 * @param field
	 */
	private FortState convertToClientFortState(FullMapNode node) {
		
		switch (node.getFortState()) {
			case NoOrUnknownFortState:
				return FortState.NOORUNJNOWNFORTSTATE;
			case EnemyFortPresent:
				return FortState.ENEMYFORTPRESENT;
			case MyFortPresent: 
				return FortState.MYFORTPRESENT;
			default:
				logger.error("No Matching FortState type on client side for: " +  node.getFortState());
				throw new MarshallerException("No Matching FortState type on client side for: " +  node.getFortState());
		}
	}

	
	
	/**
	 * Return client side terrain state of the field based on the state of the passed node
	 * @param node
	 * @return
	 */
	private Terrain convertToClientTerrain(FullMapNode node) {
		
		switch (node.getTerrain()) {
			case Grass:
				return Terrain.GRASS;
			case Mountain:
				return Terrain.MOUNTAIN;
			case Water: 
				return Terrain.WATER;
			default:
				logger.error("No Matching Terrain type on client side for: " +  node.getTerrain());
				throw new MarshallerException("No Matching Terrain type on client side for: " +  node.getTerrain());
		}
	}

	
	
	/**
	 * Return client side Player position state of the field based on the state of the passed node
	 * @param node
	 * @return PlayerPositionState
	 */
	private PlayerPositionState convertToClientPlayerPosition(FullMapNode node) {
		switch (node.getPlayerPositionState()) {
			case NoPlayerPresent:
				return PlayerPositionState.NoPlayerPresent;
			case BothPlayerPosition:
				return PlayerPositionState.BothPlayerPosition;
			case MyPosition:
				return PlayerPositionState.MyPosition;	
			case EnemyPlayerPosition: 
				return PlayerPositionState.EnemyPlayerPosition;	
			default:
				logger.error("No Matching PlayerPositionState type on client side for: " +  node.getPlayerPositionState());
				throw new MarshallerException("No Matching PlayerPositionState type on client side for: " +  node.getPlayerPositionState());
			}
	}
	
	
	
	/**
	 * Converts MapFields from Map to HalfMapNodes and returns a new Server side map
	 * @param playerId 
	 * @param map
	 * @return HalfMap
	 */
	public HalfMap convertToHalfMapNode(Map map) {
		
		Collection<HalfMapNode> nodes = new ArrayList<HalfMapNode>();
		for(Field field: map.getAllFieldList()) {
			ETerrain terrain = ETerrain.Water; 
			if(field.isEqualTerrain(Terrain.GRASS)) terrain = ETerrain.Grass;
			else if(field.isEqualTerrain(Terrain.MOUNTAIN)) terrain = ETerrain.Mountain;
			HalfMapNode node = new HalfMapNode(field.getX(), field.getY(), field.getFortPresent(), terrain);
			
			nodes.add(node);
		}
		return new HalfMap(playerId, nodes);
	}
	
	
	
	/**
	 * Converts client side move directions to server side PlayerMove Direction
	 * @param move
	 * @return PlayerMove
	 */
	public PlayerMove convertToPlayerMove(CPlayerMove move) {
		
		switch (move) {
		case Up:
			return PlayerMove.of(playerId, EMove.Up); 
		case Left:
			return PlayerMove.of(playerId, EMove.Left); 
		case Right:
			return PlayerMove.of(playerId, EMove.Right);
		case Down:
			return PlayerMove.of(playerId, EMove.Down); 
		default:
			logger.error("No Matching PlayerMove type on server side for: " +  move);
			throw new MarshallerException("No Matching PlayerMove type on server side for: " +  move);
		}
	}
	
}
