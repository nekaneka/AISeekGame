package client.map.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import client.enums.Terrain;
import client.map.Map;

public class MapGenTest {
	
	Map map = null;
	
	@BeforeEach
	public void setUp() {
		map = new Map();
		map.generateMap();
	}
		
	@Test
	public void Map_NewMap_ContainsEnoughGrass() {
		
		int mapFieldGrass = (int) map.getAllFieldList().stream().filter(f-> f.getTerrain() == Terrain.GRASS).count();
		assertThat(mapFieldGrass, is(greaterThan(14)));
	}
	
	@Test
	public void Map_NewMap_ContainsEnoughMountain() {
		
		int mapFieldGrass = (int) map.getAllFieldList().stream().filter(f-> f.getTerrain() == Terrain.MOUNTAIN).count();
		assertThat(mapFieldGrass, is(greaterThan(2)));
	}
	
	
	@Test
	public void Map_NewMap_ContainsEnoughWater() {
		
		int mapFieldGrass = (int) map.getAllFieldList().stream().filter(f-> f.getTerrain() == Terrain.WATER).count();
		assertThat(mapFieldGrass, is(greaterThan(3)));
	}
	
	
	@Test
	public void Map_GetFieldByXY_ContainsField() {
		
		
		assertNull(map.getFieldByXY(8, 1));
		assertNull(map.getFieldByXY(1, 4));
		assertNull(map.getFieldByXY(-1, -3));
	}
	
	
	@Test
	public void Map_getNeighbours_CornerFeld2Neighbours() {
		
		int fieldNeighbourSize = map.getNeighbors(map.getFieldByXY(0, 0)).size();
		assertThat(fieldNeighbourSize, is(equalTo(2)));
	}


}
