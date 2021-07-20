package client.map.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.enums.FortState;
import client.enums.Terrain;
import client.map.Field;
import client.map.Map;
import client.map.MapValidator;

public class MapValTest {

	static Map testMap = null;
	MapValidator validator;
	
	@BeforeEach
	public void buildUp() {
		testMap = new Map();
		validator = new MapValidator();
	}
	
	@AfterAll
	public static void clearUp() {
		testMap = null;
	}

	@Test
	void HalfMap_ValidateMap_TerrainNumberIsNotEnough() {
		  List<Field> testList = new ArrayList<>();
		  
	      Field fiel1 = new Field(0, 0, Terrain.GRASS);
	      fiel1.setFortState(FortState.MYFORTPRESENT);
	      
	      Field fiel2 = new Field(0, 0, Terrain.GRASS);
	      Field fiel3 = new Field(0, 0, Terrain.WATER);
	      
	      testList.add(fiel1);
	      testList.add(fiel2);
	      testList.add(fiel3);
	      
	      
	      testMap.setAllFieldList(testList); 
	      		 
	      
	      
	      assertThat(validator.validateMap(testMap), is(equalTo(false)));
	      
	      
	      testList.add(fiel3);
	      testList.add(fiel3);
	      testList.add(fiel3);
	      testMap.setAllFieldList(testList); 
	      
	      assertThat(validator.validateMap(testMap), is(equalTo(false)));
	      
	      Field fiel4 = new Field(0, 0, Terrain.MOUNTAIN);
	      testList.add(fiel4);
	      testList.add(fiel4);
	      testList.add(fiel4);
	      
	      assertThat(validator.validateMap(testMap), is(equalTo(false)));
	}
	
	
	
	private void createMapWithWaterEdgeForTest(){
		int cnt = 0;
		List<Field> list = new ArrayList<>();
		for (int j = 0; j < 4; j++) {
			for(int i = 0; i < 8; i++) {
				cnt++;
				Field field = null;
				if(cnt < 5) field = new Field(i, j, Terrain.WATER);
				else if(cnt < 12) field = new Field(i, j, Terrain.MOUNTAIN);
				else field = new Field(i, j, Terrain.GRASS);
				
				
				list.add(field);
			}
		}
		testMap.setAllFieldList(list);
	}
	
	
	@Test
	void HalfMap_ValidateMapEdge_MapEdgeExceptionForWater() {
		createMapWithWaterEdgeForTest();
	      
		assertThat(validator.validateMap(testMap), is(equalTo(false)));
	}
	
	
	
	private void createMapDataForTest(){
		int cnt = 0;
		List<Field> list = new ArrayList<>();
		for (int j = 0; j < 4; j++) {
			for(int i = 0; i < 8; i++) {
				cnt++;
				Field field = null;
				if(j == 0 && i == 0) {
					field = new Field(i, j, Terrain.GRASS);
					field.setFortState(FortState.MYFORTPRESENT);
				}
				else if(j == 0 && i == 1) field = new Field(i, j, Terrain.WATER);
				else if(j == 1 && i == 0) field = new Field(i, j, Terrain.WATER);
				else {
					if(cnt < 7) field = new Field(i, j, Terrain.MOUNTAIN);
					else if(cnt < 11) field = new Field(i, j, Terrain.WATER);
					else field = new Field(i, j, Terrain.GRASS);
				}
				
				list.add(field);
				
				if(!field.isEqualTerrain(Terrain.WATER)) testMap.getVisitableFields().add(field);
			}
		}
		testMap.setAllFieldList(list);
	}
	
	@Test
	void HalfMap_ValidateMap_HasIsland() {
		createMapDataForTest();
	    
		assertThat(validator.validateMap(testMap), is(equalTo(false)));
	}
	
}
