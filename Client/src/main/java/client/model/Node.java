package client.model;

import java.util.LinkedList;
import client.enums.CPlayerMove;
import client.map.Field;

/**
 * Used for route calculation.
 * @author Adem
 *
 */
public class Node {

	/**
	 * Represents the field that the Player wants to go to
	 */
	private Field field; 
	
	/**
	 * Represents the number of turns needed to reach the targeted field
	 */
	private int cost; 
	
	/**
	 * List consisting of enum CPlayerMove moves needed to reach the targeted field
	 */
	private LinkedList<CPlayerMove> moveList;
	
	public Node(Field field, int cost) {
		this.field = field; 
		this.cost = cost; 
		this.moveList = new LinkedList<CPlayerMove>(); 
	}
	
	public Node(Field field, int cost, LinkedList<CPlayerMove> list) {
		this.field = field; 
		this.moveList = list;
		setCost(cost);
	}
	
	public Field getField() {
		return field;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public int getCost() {
		return cost;
	}
	
	public void addMove(CPlayerMove move) {
		this.moveList.add(move); 
	}
	
	public void updateMoveList(LinkedList<CPlayerMove> list) {
		this.moveList = new LinkedList<CPlayerMove>();
		this.moveList = list; 
	}
	
	public CPlayerMove getMove() {
		return moveList.pollFirst();
	}
	
	public LinkedList<CPlayerMove> getMoveList() {
		return moveList;
	}
	
	
}
