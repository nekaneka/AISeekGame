package client.cli;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import client.enums.PlayerGameState;
import client.map.Map;
import client.model.ClientGameState;

public class GameVisualisation implements PropertyChangeListener {

	private String property;
    private Object newValue;
	
	
	public GameVisualisation(ClientGameState cgs) {	
		cgs.addPropertyChangeListener(this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		property = evt.getPropertyName();
		newValue = evt.getNewValue();
		
		if(property.equals("TreasureFound")) {
			if(newValue.equals(true)) System.out.println("[ ----   TREASURE FOUND - SEARCHING FOR ENEMY FORTRES  ---- ]\n");
			else System.out.println("[ ----   TREASURE STILL NOT FOUND  ---- ]\n");
		}
			
	
		if(property.equals("Turn")) {
			if(((PlayerGameState) newValue).equals(PlayerGameState.ShouldActNext))
				System.out.println("\n[ ----   MY TURN   ---- ]\n");
			else if(((PlayerGameState) newValue).equals(PlayerGameState.Won)) {
				System.out.println("\n*********************************");
				System.out.println("[ ---- \\ ^_^ /  YOU WON   ---- ]\n");
			}
			else 
				System.out.println("\n############################\n[ ----   YOU LOST   ---- ]\n");


				
		}
			
		
		if(property.equals("TurnCounter"))
			System.out.println("\n[ ----   CURRENT ROUND: "+ (int) newValue + "   ---- ]");
		
		
		if(property.equals("Map")) {
			System.out.println("\n[ ----   MAP UPDATE   ---- ]\n");
			System.out.println("P1 -- MY AVATAR\tP2 -- ENEMY AVATAR\t## -- MY FORTRES\t** -- MY TREASURE\t~~ -- ENEMY FORTRES\n");
			
			Map map = (Map) newValue; 
			map.printMap();
		}

	}
	
	
	public Object getNewValue() {
		return newValue;
	}
	
}
