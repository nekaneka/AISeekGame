package client.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import client.enums.PlayerGameState;
import client.map.Field;
import client.map.Map;

public class ClientGameState {
	
	private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

	private String gameStateId = "";
	private Map map; 
	private boolean myPTreasureState; 
	private PlayerGameState myEPlayerState; 
	private int turnCounter = 0; 

	public ClientGameState() {
		this.map = new Map();
		this.myEPlayerState = PlayerGameState.ShouldWait;
		this.myPTreasureState = false; 
	}

	public String getGameStateId() {
		return gameStateId;
	}

	public boolean setGameStateId(String id) {
		if(this.gameStateId.equals(id)) return false;

		this.gameStateId = id;
		changes.firePropertyChange("gameStateId", null, id);	
		return true;
	}
	
	public void increaseTurnCounter() {
		this.turnCounter++;
		changes.firePropertyChange("TurnCounter", null, this.turnCounter);
	}

	public Map getMap() {
		return map;
	}

	public void setMap(List<Field> mapFields) {
		this.map.setAllFieldList(mapFields);
		changes.firePropertyChange("Map", null, map);
	}
	
	public boolean isMyPTreasureState() {
		return myPTreasureState;
	}

	public void setMyPTreasureState(boolean myPTreasureState) {
		this.myPTreasureState = myPTreasureState;
		if(!gameFinished()) changes.firePropertyChange("TreasureFound", null, this.myPTreasureState);
	}

	public PlayerGameState getMyEPlayerState() {
		return myEPlayerState;
	}

	public void setMyEPlayerState(PlayerGameState myEPlayerState) {
		this.myEPlayerState = myEPlayerState;
		if(this.myEPlayerState != PlayerGameState.ShouldWait) changes.firePropertyChange("Turn", null, this.myEPlayerState);
	}
	
	public void setHorizontalMap(boolean verticalMap) {
		this.map.setHorizontal(verticalMap);
	}
	
	public boolean gameFinished() {
		return myEPlayerState.equals(PlayerGameState.Lost) 
				|| myEPlayerState.equals(PlayerGameState.Won);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener view) {
        changes.addPropertyChangeListener(view);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }
	
}
