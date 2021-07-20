package client.map;

import client.enums.FortState;
import client.enums.PlayerPositionState;
import client.enums.Terrain;
import client.enums.TreasureState;

/**
 * Represents the cornerstones of what the map consists of.
 * 
 * @author Adem
 *
 */
public class Field {

	private FortState fortState; 
	private PlayerPositionState playerPositionState; 
	private Terrain terrain; 
	private TreasureState treasureState; 
	
	/**
	 * The unique combination of the values x and y indicated the position of the field in the Map 
	 */
	private int x; 
	private int y; 

	
	public Field(int x, int y, Terrain terrain) {
		this.x = x; 
		this.y = y;
		this.fortState = FortState.NOORUNJNOWNFORTSTATE;
		this.playerPositionState = PlayerPositionState.NoPlayerPresent;
		this.treasureState = TreasureState.NOORUNKNOWNTREAURESTATE;
		this.terrain = terrain;
	}
	

	public Terrain getTerrain() {
		return terrain;
	}
	
	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}
	
	public FortState getFortState() {
		return fortState;
	}

	public void setFortState(FortState fortState) {
		this.fortState = fortState;
	}

	public boolean getFortPresent() {
		return fortState.equals(FortState.MYFORTPRESENT);
	}
	
	public PlayerPositionState getPlayerPositionState() {
		return playerPositionState;
	}

	public void setPlayerPositionState(PlayerPositionState playerPositionState) {
		this.playerPositionState = playerPositionState;
	}

	public TreasureState getTreasureState() {
		return treasureState;
	}

	public void setTreasureState(TreasureState treasureState) {
		this.treasureState = treasureState;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	

	/**
	 * Method that return if the field Terrain type is the same as the passed one to the method.
	 * If it is the same true is returned, otherwise false.
	 * @param terrain
	 * @return
	 */
	public boolean isEqualTerrain(Terrain terrain) {
		return this.terrain.equals(terrain);
	}
	
	
	
	/**
	 * Method used to print a field in the map visualization
	 * @return
	 */
	public String printField() {
		if(treasureState == TreasureState.MYTREASUREISPRESENT) return " **";
		
		if(fortState == FortState.MYFORTPRESENT) return " ##";
		else if (fortState == FortState.ENEMYFORTPRESENT) return " ~~";
		
		if(playerPositionState == PlayerPositionState.BothPlayerPosition) return "P12"; 
		else if(playerPositionState == PlayerPositionState.EnemyPlayerPosition) return " P2"; 
		else if(playerPositionState == PlayerPositionState.MyPosition) return " P1";
		
		
		if(terrain == Terrain.GRASS) return " G "; 
		else if(terrain == Terrain.MOUNTAIN) return " M ";
				
		return " W ";
	}
	
	
	
	@Override
	public String toString() {				
		return "X : " + x + " Y: " + y;
	}
}
