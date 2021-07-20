package client.VisualisationTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.cli.GameVisualisation;
import client.enums.PlayerGameState;
import client.map.Map;
import client.model.ClientGameState;

public class GameModelVisualisationTest {
	
	//Listener
	static GameVisualisation cli; 
	
	// support
	static ClientGameState state; 
	
	@BeforeEach
	private void setUp() {
		state = new ClientGameState();
		cli = new GameVisualisation(state);
	}
	
	@AfterAll
	private static void cleanUp() {
		state = null;
		cli  = null;
	}
	
	
	@Test 
	public void OldGameState_updateGameState_NewGameStateAndTurnCounterVisualisation() {
		
		state.setGameStateId("newGameStateId");
		
		assertThat(state.getGameStateId(), is(equalTo(cli.getNewValue())));
		
		assertThat(state.setGameStateId("newGameStateId"), is(equalTo(false)));
		
		state.increaseTurnCounter();
		assertThat(cli.getNewValue(), is(equalTo(1)));
		
		state.increaseTurnCounter();
		assertThat(cli.getNewValue(), is(equalTo(2)));

	}
	
	@Test 
	public void OldMap_updateMap_NewMapStateAndMapVisualisation() {
		
		Map testMap = new Map();
		testMap.generateMap();
		
		state.setMap(testMap.getAllFieldList());
		assertThat(state.getMap(), is(equalTo(cli.getNewValue())));
	
	}
	

	@Test 
	public void OldPlayerState_updatePlayerState_NewPlayerStateAndStateVisualisation() {
		state.setMyEPlayerState(PlayerGameState.Won);
		assertThat(state.getMyEPlayerState(), is(equalTo(cli.getNewValue())));
		assertThat(state.gameFinished(), is(equalTo(true)));
		
		state.setMyEPlayerState(PlayerGameState.Lost);
		assertThat(state.getMyEPlayerState(), is(equalTo(cli.getNewValue())));
		assertThat(state.gameFinished(), is(equalTo(true)));

		
		state.setMyEPlayerState(PlayerGameState.ShouldActNext);
		assertThat(state.getMyEPlayerState(), is(equalTo(cli.getNewValue())));
		assertThat(state.gameFinished(), is(equalTo(false)));
	}
	
	
	@Test
	public void OldTreasureState_updateTreasureState_NewTreasureStateAndTreasureVisualisation() {
		state.setMyPTreasureState(false);
		assertThat(state.isMyPTreasureState(), is(equalTo(cli.getNewValue())));
		
		state.setMyPTreasureState(true);
		assertThat(state.isMyPTreasureState(), is(equalTo(cli.getNewValue())));
	}
	
	
	
	

}
