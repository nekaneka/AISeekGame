package client.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.enums.CPlayerMove;
import client.enums.PlayerPositionState;
import client.enums.Terrain;
import client.exceptions.FailedMoveException;
import client.map.Field;
import client.map.Map;
import client.model.Node;


class SortGrass implements Comparator<Field>{
	@Override
	public int compare(Field o1, Field o2) {
		return o1.getTerrain().getCost() - o2.getTerrain().getCost();		
	}
}


/**
 * Route calculation
 * @author Adem
 *
 */
public class MoveController { 
	
	static Logger logger = LoggerFactory.getLogger(MoveController.class);

	private Map map = null; 
	private List<Field> notVisitedGrassField = new ArrayList<Field>(); 
	private HashMap<Field, Node> potentionalMoveList = new HashMap<Field, Node>(); 
	private Node nextNode = null;
	private boolean treasureCollected = false;
	private boolean mapNormalPos;
	
	
	public void setMap(Map map) {
		if(this.map == null) {
			this.map = map;
			findHalfMapPositions();
		}
	}

	

	/**
	 * Gets the next CPlayerMove or starts the route calculation if the goal field was reached.
	 * If all Grass fields are visited and there is no next move a FailedMoveException is thrown.
	 * 
	 * @param treasureCollect - boolean indicates if the treasure was found 
	 * @return CPlayerMove
	 * @throws FailedMoveException
	 */
	public CPlayerMove getNextMove(boolean treasureCollect) throws FailedMoveException {
		if(treasureCollect == true && this.treasureCollected == false) {
			this.treasureCollected = treasureCollect;
			findNotVisitedFields();
			nextNode = null;
		}
		
		if(nextNode == null || nextNode.getMoveList().isEmpty()) {
			for(Field field: map.getAllFieldList()) {
				if(field.getPlayerPositionState().equals(PlayerPositionState.MyPosition) 
						|| field.getPlayerPositionState().equals(PlayerPositionState.BothPlayerPosition)) {
					nextNode = new Node(field, 10000, null);
					potentionalMoveList = new HashMap<Field, Node>();
					findNextMode(field, new LinkedList<CPlayerMove>(),  10000);
				}		
			}
			nextNode = getNodeMinCost();
			notVisitedGrassField.remove(visitedField(nextNode.getField()));
		}
		logger.info("Player next Field destination: " + nextNode.getField().toString());
		
		if(nextNode.getMoveList().isEmpty()) throw new FailedMoveException("There is no Next move");
		
		return nextNode.getMove();
	}

	
	
	/**
	 * Returns the Node with the lowest cost and the field which was not visited
	 * 
	 * @return Node
	 */
	private Node getNodeMinCost() {
		nextNode = new Node(null, 10000);

		for(Field field: potentionalMoveList.keySet()) {
			if(visitedField(field) != null 
					&& potentionalMoveList.get(field).getCost() < nextNode.getCost()) 
				nextNode = potentionalMoveList.get(field);
		}
		
		return nextNode;
	}



	/**
	 * Checks if the notVisitedGrassField List contains the passed field.
	 * 
	 * @param field
	 * @return Field
	 */
	private Field visitedField(Field field) {
		for(Field f: notVisitedGrassField)
			if(f.getX() == field.getX() && f.getY() == field.getY()) return f; 
		return null;
	}



	/**
	 * Calculated all possible routes to all fields, from the current position.
	 * If a new field is reached, a new Node is created with the previous route and cost that is need to reach to it.
	 * If a field is reached that was already reachable his route and cost will be changed if the new route if better.
	 * 
	 * @param field
	 * @param list
	 * @param cost
	 */
	private void findNextMode(Field field,LinkedList<CPlayerMove> list, int cost) {
		
		if(potentionalMoveList.containsKey(field) && potentionalMoveList.get(field).getCost() > cost) {
			potentionalMoveList.get(field).setCost(cost);
			potentionalMoveList.get(field).updateMoveList(list);
		}
		else if(potentionalMoveList.containsKey(field) && potentionalMoveList.get(field).getCost() < cost) return; 
		else potentionalMoveList.put(field, new Node(field, cost, list)); 	
		
		
		
		List<Field> neighbors = new ArrayList<Field>();
		neighbors = map.getNeighbors(field);		
		Collections.sort(neighbors, new SortGrass()); 
		
		for(Field neighbour: neighbors) {
			if(!neighbour.isEqualTerrain(Terrain.WATER)) {
				int newCost = field.getTerrain().getCost() + neighbour.getTerrain().getCost(); 
				if(!list.isEmpty()) newCost += cost;
	
				LinkedList<CPlayerMove> list2 = new LinkedList<CPlayerMove>(); 
				list2.addAll(list);
					
				for(int i = 0; i < field.getTerrain().getCost() + neighbour.getTerrain().getCost(); i++)
					list2.add(moveDirection(field, neighbour));
				
				findNextMode(neighbour, list2, newCost);
			}
		}	
	}



	/**
	 * Return CPlayerMove based on the position of the neighbor
	 * @param field
	 * @param neighbor
	 * @return CPlayerMove
	 */
	private CPlayerMove moveDirection(Field field, Field neighbor) {
		if(field.getX() == neighbor.getX() && field.getY() < neighbor.getY()) return CPlayerMove.Down; 
		if(field.getX() == neighbor.getX() && field.getY() > neighbor.getY()) return CPlayerMove.Up; 
		if(field.getX() < neighbor.getX() && field.getY() == neighbor.getY()) return CPlayerMove.Right; 
		return CPlayerMove.Left;
	}



	/**
	 * Determines the client side of the full map.
	 * If the field with my fortress has x <= 7 and y <= 3, the player halfMap is in the normal position,
	 * otherwise the players halfMap is either the right side of the fullMap(Horizontal) or the lower side(Vertical).
	 */
	private void findHalfMapPositions() {
		for(Field field: map.getAllFieldList()) {
			if(field.getFortPresent()) {
				if(field.getX() <= 7 && field.getY() <= 3) mapNormalPos = true;
				else mapNormalPos = false; 

				findNotVisitedFields();
				notVisitedGrassField.remove(field);
				break;
			}		
		}	
	}


 
	/**
	 * Determines the Fields that were originally sent by the player before the treasure is found.
	 * After the treasure is found, determines the fields that were originally from the enemy halfMap.
	 */
	private void findNotVisitedFields() {
		
			if(treasureCollected) notVisitedGrassField.clear();
				
			for(Field field: map.getAllFieldList()) {
				if(!treasureCollected && !mapNormalPos 
						&& (field.getY() > 3 || field.getX() > 7)
						&& field.isEqualTerrain(Terrain.GRASS)) notVisitedGrassField.add(field);
				if(!treasureCollected && mapNormalPos 
						&& field.getY() <= 3 && field.getX() <= 7
						&& field.isEqualTerrain(Terrain.GRASS)) notVisitedGrassField.add(field);
				if(treasureCollected && mapNormalPos 
						&& (field.getY() > 3 || field.getX() > 7)
						&& field.isEqualTerrain(Terrain.GRASS)) notVisitedGrassField.add(field);
				if(treasureCollected && !mapNormalPos 
						&& field.getY() <=3 && field.getX() <= 7
						&& field.isEqualTerrain(Terrain.GRASS)) notVisitedGrassField.add(field);
				
			}
		}
	
}
