package client.network.test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import client.exceptions.PlayerRegistrationException;
import client.network.Network;

public class NetworkTest {
	
	static Network testNetwork; 
	
	@Test
	public void buildUp() {
		String testUrl = null, testGameId = null;
		testNetwork = new Network(testUrl, testGameId);
		
		Exception exception = assertThrows(PlayerRegistrationException.class, () -> {
			testNetwork.registerPlayer();
		});
	      
	      String expectedMessage = "Player registration FAILED!";
	      String actualMessage = exception.getMessage();
	      
	      assertEquals(expectedMessage, actualMessage);
		
	}
	
	

}
