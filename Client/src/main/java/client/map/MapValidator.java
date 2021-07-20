package client.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.enums.FortState;
import client.enums.Terrain;

public class MapValidator {
	
	static Logger logger = LoggerFactory.getLogger(MapValidator.class);
	
	private Map map;
	private Set<Field> checkedFields;
	
	public boolean validateMap(Map map) {
		this.map = map;	
		if(!checkFieldTypes()) return false;
		if(!checkEdges()) return false;
		if(!checkIslands()) return false;
		
		return true;
	}
	
	

	/**
	 * Checks if the generated Map has the minimal number of Terrain types.
	 * Returns true if the minimal number of individual terrain types was reached, otherwise false.
	 */
	private boolean checkFieldTypes() {
		int countWater = 0, countGrass = 0, countMountain = 0;
		
		for(Field field: map.getAllFieldList()) {
			if(field.isEqualTerrain(Terrain.WATER)) countWater++;
			if(field.isEqualTerrain(Terrain.GRASS))  countGrass++;
			if(field.isEqualTerrain(Terrain.MOUNTAIN))  countMountain++;
		}
		
		if(countWater < 4 || countMountain < 3 || countGrass < 15) {
			logger.info("Insufficient Individual Terrain Types!");
			return false;
		}
		
		return true;
	}

	
	/**
	 * Checks if the number of fields reached by the recursive method checkReachability
	 * equal the number of fields of the type Grass and Mountain. If the numbers are equal
	 * true is return, otherwise false.
	 */
	private boolean checkIslands() {
		this.checkedFields = new HashSet<Field>();
		
		for(Field field: map.getAllFieldList()) {
			if(field.getFortState().equals(FortState.MYFORTPRESENT)) {
				 checkReachability(field);
				 if(map.getVisitableFields().size() != checkedFields.size()) {
					 logger.info("Map With Island was Generated!");
						map.printMap();
					 return false;
				 }
			}
		}
		return true; 
	}
	
	
	
	/**
	 * Recursive Method which is invoked on all neighboring Fields, of the passed field, which are not Water type,
	 * and were not already visited. When the method is invoked, the Field passed to the method is added into a list
	 * of checkedField - a list of already reachable fields.
	 * @param field
	 */
	private void checkReachability(Field field) {
		
		checkedFields.add(field);
		
		List<Field> neighbours = new ArrayList<Field>();
		neighbours = map.getNeighbors(field);
		
		for(Field neighbour: neighbours) 
			if(!checkedFields.contains(neighbour) 
					&& !neighbour.isEqualTerrain(Terrain.WATER))
				checkReachability(neighbour);
		
	}
	
	

	/**
	 * Checks the number of water field on the sides of the generated Map.
	 * If the number is higher than allowed and Exception is thrown.
	 * @return
	 */
	private boolean checkEdges(){
		int countLeftWater = 0, countRightWater = 0, countBottomWater = 0, countTopWater = 0;
		
		for(Field field: map.getAllFieldList()) {
			if(field.getX() == 0 && field.getTerrain() == Terrain.WATER) countLeftWater++;
			if(field.getX() == 7 && field.getTerrain() == Terrain.WATER) countRightWater++;
			if(field.getY() == 0 && field.getTerrain() == Terrain.WATER) countTopWater++;
			if(field.getY() == 3 && field.getTerrain() == Terrain.WATER) countBottomWater++;
		}
		
		if(countLeftWater > 1 || countRightWater > 1 || countTopWater >3 || countBottomWater > 3) {
			logger.info("Too much Water on the edges!");
			map.printMap();
			return false; 
		}
		
		return true;
	}
}
