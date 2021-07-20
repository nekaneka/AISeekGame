package client.move.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.controller.MoveController;
import client.enums.PlayerPositionState;
import client.map.Field;
import client.map.Map;

public class MoveControllerTest {

static MoveController testController; 
	
@BeforeEach
private void setUp() {
	
	Map testMap = new Map(); 
	testMap.generateMap();
	for(Field field: testMap.getAllFieldList()) {
		if(field.getFortPresent())
			field.setPlayerPositionState(PlayerPositionState.MyPosition);
	}
	testController = new MoveController();
	testController.setMap(testMap);
	
}

@AfterAll
private static void clearUp() {
	testController = null;
}

@Test
public void mapSent_GenerateNextMove_SearchForTreasureDoesNotThrowException() {
	assertDoesNotThrow(() -> testController.getNextMove(false));
}

}
