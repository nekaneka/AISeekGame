package client.marshaller.test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import MessagesBase.EMove;
import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesBase.PlayerMove;
import client.enums.CPlayerMove;
import client.exceptions.MarshallerException;
import client.map.Field;
import client.map.Map;
import client.marshaller.Marshaller;

public class MarshallerTest {
	
	Marshaller marshaller = null;
	
	@BeforeEach
	public void setUp() {
		marshaller = new Marshaller();
		marshaller.setPlayerId("afia-afc-asfdaf-assd");
	}

	@Test
	public void NextMoveReady_convertMoveToServerMove_ServerMoveReady() {
		PlayerMove move;
	    move =  marshaller.convertToPlayerMove(CPlayerMove.Down);
	    assertEquals(EMove.Down, move.getMove());
	    move =  marshaller.convertToPlayerMove(CPlayerMove.Up);
	    assertEquals(EMove.Up, move.getMove());
	    move =  marshaller.convertToPlayerMove(CPlayerMove.Left);
	    assertEquals(EMove.Left, move.getMove());
	    move =  marshaller.convertToPlayerMove(CPlayerMove.Right);
	    assertEquals(EMove.Right, move.getMove());
	    
	    
	    Exception exception = assertThrows(MarshallerException.class, () -> {
	    	marshaller.convertToPlayerMove(CPlayerMove.TestMove);
	      });
	      
	      String expectedMessage = "No Matching PlayerMove type on server side for: TestMove";
	      String actualMessage = exception.getMessage();
	      
	      assertEquals(expectedMessage, actualMessage);
	    
		
	}
	
	
	@Test
	public void MapGenerated_convertMapToHalfmap_MapConverted() {
		Map testMap = new Map();
		testMap.generateMap();
		
		HalfMap testHalfMap= marshaller.convertToHalfMapNode(testMap);
		
		assertEquals(testMap.getAllFieldList().size(), testHalfMap.getNodes().size());
		
		HalfMapNode testHalfNode = null; 
		Field testField = null; 
		for(Field field: testMap.getAllFieldList())
			if(field.getFortPresent()) {
				testField = field;
			}
		
		for(HalfMapNode node: testHalfMap.getNodes()) {
			if(node.isFortPresent()) {
				testHalfNode = node; 
			}
		}
		
		assertEquals(testField.getX(), testHalfNode.getX());
		assertEquals(testField.getY(), testHalfNode.getY());
		assertEquals(testField.getFortPresent(), testHalfNode.isFortPresent());
	}
}
