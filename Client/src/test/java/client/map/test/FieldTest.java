package client.map.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import client.enums.FortState;
import client.enums.PlayerPositionState;
import client.enums.Terrain;
import client.enums.TreasureState;
import client.map.Field;

public class FieldTest {
	
	@Test
	public void FieldCreated_callPrintField_FieldReturnedForDispaly() {
		
		Field testField = new Field(0, 0, Terrain.WATER); 
		assertThat(testField.printField(), is(equalTo(" W ")));
		
		assertThat(testField.isEqualTerrain(Terrain.WATER), is(equalTo(true)));
		assertThat(testField.isEqualTerrain(Terrain.MOUNTAIN), is(equalTo(false)));
		
		testField.setTerrain(Terrain.MOUNTAIN);
		assertThat(testField.printField(), is(equalTo(" M ")));
		
		testField.setTerrain(Terrain.GRASS);
		assertThat(testField.printField(), is(equalTo(" G ")));
		
		testField.setPlayerPositionState(PlayerPositionState.MyPosition);
		assertThat(testField.printField(), is(equalTo(" P1")));
		
		testField.setPlayerPositionState(PlayerPositionState.EnemyPlayerPosition);
		assertThat(testField.printField(), is(equalTo(" P2")));
		
		testField.setPlayerPositionState(PlayerPositionState.BothPlayerPosition);
		assertThat(testField.printField(), is(equalTo("P12")));
		
		testField.setFortState(FortState.ENEMYFORTPRESENT);
		assertThat(testField.printField(), is(equalTo(" ~~")));
		
		testField.setFortState(FortState.MYFORTPRESENT);
		assertThat(testField.printField(), is(equalTo(" ##")));
		
		testField.setTreasureState(TreasureState.MYTREASUREISPRESENT);
		assertThat(testField.printField(), is(equalTo(" **")));
	}
}
