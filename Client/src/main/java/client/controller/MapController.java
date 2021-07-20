package client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.Map;
import client.map.MapValidator;

public class MapController {

	static Logger logger = LoggerFactory.getLogger(MapController.class);
	private Map map; 
	private MapValidator validator; 
	
	public MapController() { 
		this.map = new Map();
		this.validator = new MapValidator();
	}
	
	
	
	/**
	 * Method generates and validates the new Map. This process continues until a valid map is generated.
	 * 
	 * @return Map
	 */
	public Map generateMap() {	
		
		boolean validMapGenerated = false; 
		
		while(!validMapGenerated) {
			
			map = new Map();
			map.generateMap();		
			
			validMapGenerated =	validator.validateMap(map);
		}
		
		return map;
	}
}
