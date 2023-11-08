package com.csse3200.game.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.files.SaveGame.GameState;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.achievements.Achievement;
import com.csse3200.game.missions.achievements.PlantCropsAchievement;
import com.csse3200.game.missions.quests.FertiliseCropTilesQuest;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.SaveLoadService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

public class SaveGameTest {

	@Test
	public void SetDay() {
		GameState gameState = new SaveGame.GameState();

		gameState.setDay(5);
		assertEquals(5, gameState.getDay());

		gameState.setDay(-1);
		assertEquals(-1, gameState.getDay());

		gameState.setDay(2147483647);
		assertEquals(2147483647, gameState.getDay());
	}

	@Test
	public void setHour() {
		GameState gameState = new SaveGame.GameState();

		gameState.setHour(17);
		assertEquals(17, gameState.getHour());

		gameState.setHour(-1);
		assertEquals(-1, gameState.getHour());

		gameState.setHour(2147483647);
		assertEquals(2147483647, gameState.getHour());
	}

	@Test
	public void setMinute() {
		GameState gameState = new SaveGame.GameState();

		gameState.setMinute(17);
		assertEquals(17, gameState.getMinute());

		gameState.setMinute(-1);
		assertEquals(-1, gameState.getMinute());

		gameState.setMinute(2147483647);
		assertEquals(2147483647, gameState.getMinute());
	}

	@Test
	public void setPlayer() {
		GameState gameState = new SaveGame.GameState();

		Entity playerTest = new Entity(EntityType.PLAYER);

		gameState.setPlayer(playerTest);
		assertEquals(playerTest, gameState.getPlayer());
	}

	@Test
	public void setEntities() {
		GameState gameState = new SaveGame.GameState();

		Entity[] entities = {new Entity(EntityType.ASTROLOTL),
				new Entity(EntityType.CHICKEN),
				new Entity(EntityType.COW),
				new Entity(EntityType.OXYGEN_EATER),
				new Entity(EntityType.SHIP_DEBRIS),
				new Entity(EntityType.FIRE_FLIES),
				new Entity(EntityType.SHIP),
				new Entity()};

		Array<Entity> entityTest = new Array<Entity>(entities);

		gameState.setEntities(entityTest);
		assertEquals(7, gameState.getEntities().size);
	}

	@Test
	public void setTiles() {
		GameState gameState = new SaveGame.GameState();

		Entity[] tiles = {new Entity(EntityType.TILE),
				new Entity(EntityType.TILE),
				new Entity(EntityType.TILE),
				new Entity(EntityType.TILE),};

		Array<Entity> tileTest = new Array<Entity>(tiles);

		gameState.setTiles(tileTest);
		assertEquals(tileTest, gameState.getTiles());
	}

	@Test
	public void testFilterTiles() {
		Array<Entity> entities = new Array<Entity>();
		Entity tile = new Entity(EntityType.TILE);
		entities.add(new Entity(EntityType.PLANT), new Entity(), tile, new Entity(EntityType.SHIP_PART_TILE));
		GameState state = new GameState();
		state.setTiles(entities);
		assertEquals(2, state.getTiles().size);
		assertEquals(tile, state.getTiles().get(0));
	}

	@Test
	public void testFilterEntities() {
		Array<Entity> entities = new Array<Entity>();
		Entity tile = new Entity(EntityType.TILE);
		Entity chicken = new Entity(EntityType.CHICKEN);
		entities.add(new Entity(), tile, chicken);
		GameState state = new GameState();
		state.setEntities(entities);
		assertEquals(1, state.getEntities().size);
		assertEquals(chicken, state.getEntities().get(0));
	}

	@Test
	public void getTractor() {
		GameState gameState = new SaveGame.GameState();
		Entity tractor = new Entity(EntityType.TRACTOR);
		gameState.setTractor(tractor);
		assertEquals(tractor, gameState.getTractor());
		assertEquals(EntityType.TRACTOR, gameState.getTractor().getType());
	}

	@Test
	public void setTractor() {
		GameState gameState = new SaveGame.GameState();
		Entity tractor = new Entity(EntityType.TRACTOR);
		Vector2 position = new Vector2(1, 1);
		tractor.setPosition(position);
		gameState.setTractor(tractor);
		assertEquals(tractor, gameState.getTractor());
		assertEquals(position, gameState.getTractor().getPosition());
	}

	@Test
	public void getClimate() {
		GameState state = new GameState();
		ClimateController mockClimate = mock(ClimateController.class);
		when(mockClimate.getCurrentWeatherEvent()).thenReturn(null);
		mockClimate.setCurrentWeatherEvent(null);
		state.setClimate(mockClimate);
		assertEquals(mockClimate.getCurrentWeatherEvent(), state.getClimate().getCurrentWeatherEvent());
	}

	@Test
	public void getMissions() {
		ServiceLocator.registerTimeService(new TimeService());
		GameState state = new GameState();
		ArrayList missions = new ArrayList<>();
		missions.add(new FertiliseCropTilesQuest("asd", null, 2, 1));
		Achievement[] achievements = new Achievement[]{
				new PlantCropsAchievement("asd", 2)
		};
		MissionManager missionManager = mock(MissionManager.class);
		when(missionManager.getActiveQuests()).thenReturn(missions);
		when(missionManager.getSelectableQuests()).thenReturn(missions);
		when(missionManager.getAchievements()).thenReturn(achievements);
		MissionManager mm = new MissionManager();
		state.setMissions(mm);
		assertNotEquals(missionManager.getAchievements(), state.getMissions().getAchievements());
		assertNotEquals(missionManager.getActiveQuests(), state.getMissions().getActiveQuests());
		assertNotEquals(missionManager.getSelectableQuests(), state.getMissions().getSelectableQuests());
		state.setMissions(missionManager);
		assertEquals(missionManager.getAchievements(), state.getMissions().getAchievements());
		assertEquals(missionManager.getActiveQuests(), state.getMissions().getActiveQuests());
		assertEquals(missionManager.getSelectableQuests(), state.getMissions().getSelectableQuests());
	}

	@Test
	public void testPlaceables() {
		Entity[] entities = {new Entity(EntityType.SPRINKLER),
				new Entity(EntityType.CHEST),
				new Entity(EntityType.FENCE),
				new Entity(EntityType.LIGHT),
				new Entity(EntityType.GOLDEN_STATUE),
				new Entity(EntityType.PUMP),
				new Entity(EntityType.GATE),
				new Entity()};
		Array<Entity> entityTest = new Array<Entity>(entities);
		GameState state = new GameState();
		state.setPlaceables(entityTest);
		assertEquals(7, state.getPlaceables().size);
	}

	@Test
	public void testSet() {
		ServiceLocator.registerTimeSource(new GameTime());
		ServiceLocator.registerTimeService(new TimeService());
		ServiceLocator.registerMissionManager(new MissionManager());
		ServiceLocator.registerEntityService(new EntityService());
		ServiceLocator.registerSaveLoadService(new SaveLoadService());
		SpaceGameArea mockArea = mock(SpaceGameArea.class);
		ServiceLocator.registerGameArea(mockArea);
		GameMap mockMap = mock(GameMap.class);
		doReturn(mockMap).when(mockArea).getMap();
		doReturn(new ClimateController()).when(mockArea).getClimateController();
		TerrainTile mockTile = mock(TerrainTile.class);
		doReturn(mockTile).when(mockMap).getTile(any(Vector2.class));
		doReturn(null).when(mockTile).getOccupant();
		Entity player = new Entity(EntityType.PLAYER).addComponent(new InventoryComponent());
		for (int i = 0; i < 5; i++) {
			ServiceLocator.getEntityService().register(new Entity(EntityType.PUMP));
		}
		for (int i = 0; i < 5; i++) {
			ServiceLocator.getEntityService().register(new Entity(EntityType.BAT));
		}
		doReturn(null).when(mockArea).getTractor();
		doReturn(player).when(mockArea).getPlayer();
		Gdx.files = new HeadlessFiles();
		ServiceLocator.getSaveLoadService().save("test/files/saveFileTest.json");
	}
}