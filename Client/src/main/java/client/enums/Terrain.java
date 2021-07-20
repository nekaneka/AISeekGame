package client.enums;

import java.util.Random;

/**
 * Client side Terrain that the field can have.
 * 
 * @author Adem
 *
 */
public enum Terrain {

	GRASS(1), MOUNTAIN(2), WATER(10000); 
	
	public final int cost; 
	
	private Terrain(int cost) {
		this.cost = cost;
	}
	
	/**
	 * Return a random terrain type
	 * @return
	 */
	public static Terrain randomTerrain() {
		int random = new Random().nextInt(Terrain.values().length-1);
		return Terrain.values()[random];
	}
	
	public int getCost() {
		return cost;
	}
}
