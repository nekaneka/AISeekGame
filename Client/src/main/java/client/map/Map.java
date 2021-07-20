package client.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import client.enums.FortState;
import client.enums.Terrain;

public class Map {
	
	private List<Field> allFieldList; 
	private List<Field> visitableFields;
	private LinkedList<Terrain> terrains; 
	private boolean horizontalMap = false;
	
	public Map() {
		this.allFieldList = new ArrayList<Field>();
		this.visitableFields = new ArrayList<Field>();
		this.terrains = new LinkedList<>();

	}
	
	public List<Field> getAllFieldList() {
		return allFieldList;
	}
	public void setAllFieldList(List<Field> allFieldList) {
		this.allFieldList = allFieldList;
	}
	public List<Field> getVisitableFields() {
		return visitableFields;
	}
	
	public void setNotVisitedGrassFields(List<Field> notVisitedGrassFields) {
		this.visitableFields = notVisitedGrassFields;
	} 
	
	public void setHorizontal(boolean b) {
		this.horizontalMap = b;
	}
	
	

	/**
	 * Method that starts the Map generation process. After the fields are generated,
	 * one random field, of the Grass terrain type, is chosen and the Fort placed on it
	 * 
	 */
	public void generateMap() {
		
		generateTerrains();
				
		for (int j = 0; j < 4; j++) {
			for(int i = 0; i < 8; i++) {
				Field field = new Field(i, j, terrains.poll());
				allFieldList.add(field);
				if(!field.isEqualTerrain(Terrain.WATER)) visitableFields.add(field);
			}
		}
		
		boolean flag = false;
		while(!flag) {
			int random = new Random().nextInt(allFieldList.size());
			
			if(allFieldList.get(random).isEqualTerrain(Terrain.GRASS)) {
				allFieldList.get(random).setFortState(FortState.MYFORTPRESENT);
				flag = true;
			}
		}
	}
	
	
	
	/**
	 * The method first generated the minimal number of water(4), grass(15), and mountain(3) Terrain types.
	 * After the minimal number of was reaches, the types are generated randomly.
	 * All the generated Terrain types are added into a LinkedList and used for the generation of fields.
	 */
	private void generateTerrains() {
		int waterMin = 4, grassMin = 15, montainMin = 3; 
		
		while(terrains.size() != 32) {
			
			if(waterMin != 0) {
				terrains.add(Terrain.WATER);
				waterMin--; 
			}
			else if (montainMin != 0) {
				terrains.add(Terrain.MOUNTAIN); 
				montainMin--;
			}
			else if (grassMin != 0) {
				terrains.add(Terrain.GRASS); 
				grassMin--;
			}
			else terrains.add(Terrain.randomTerrain());
		}
		
		Collections.shuffle(terrains);
	}	
	
	
	
	/**
	 * Returns a Field based on the X and Y passed to the method
	 * If the Field doesn't exist null is returned
	 * @param x
	 * @param y
	 * @return Field or null
	 */
	public Field getFieldByXY(int x, int y) {
		for (Field field: allFieldList) {
			if(field.getX() == x && field.getY() == y)
				return field;
		}
		return null;
	}
	
	
	
	/**
	 * Returns a List of Fields which contains the fields next to the passed field Left Right Up and Down if they exist
	 * @param field
	 */
	public List<Field> getNeighbors(Field field) {
		List<Field> neighbours = new ArrayList<Field>();
		
		neighbours.add(getFieldByXY(field.getX()-1, field.getY()));
		neighbours.add(getFieldByXY(field.getX(), field.getY()-1));
		neighbours.add(getFieldByXY(field.getX()+1, field.getY()));
		neighbours.add(getFieldByXY(field.getX(), field.getY()+1));
		
		while (neighbours.remove(null));
		
		return neighbours;
	}
	
	
	
	/**
	 * Method used for the visualization of the map. If it is a full map the map will be printed based on the
	 * position of the 2 half maps, side by side(Horizontal) or on top each other(Vertical).
	 */
	public void printMap() {
		int x = 8, y = 4;
		
		if(allFieldList.size() == 64) {
			if(horizontalMap) x = 16;
			else y = 8;
		}
		
		for(int i = 0; i < y; i++) {
			for(int j = 0; j < x; j++) 
				System.out.print("[ " + getFieldByXY(j, i).printField() +" ] ");
		
			System.out.println("");
		}
	}

}
