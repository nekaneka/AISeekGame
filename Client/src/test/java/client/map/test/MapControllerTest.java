package client.map.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.RepeatedTest;
import client.controller.MapController;
import client.map.Map;

public class MapControllerTest {

	MapController testController; 

	@RepeatedTest(value = 10)
	public void PlayerRegistered_generateMap_HalfMapGenerated() {
		testController = new MapController();
		Map testMap = testController.generateMap();
		
		assertThat(testMap.getAllFieldList().size(), is(equalTo(32)));
	}
}
