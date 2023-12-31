package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.TestGameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class RunAwayTaskTest {
	//TestGameArea to register so GameMap can be accessed through the ServiceLocator
	private static final TestGameArea gameArea = new TestGameArea();

	@BeforeAll
	static void setupGameAreaAndMap() {
		ServiceLocator.clear();
		//necessary for allowing the Terrain factory to properly generate the map with correct tile dimensions
		ResourceService resourceService = new ResourceService();
		resourceService.loadTextures(TerrainFactory.getMapTextures());
		resourceService.loadAll();
		ServiceLocator.registerResourceService(resourceService);

		//Loads the test terrain into the GameMap
		TerrainComponent terrainComponent = mock(TerrainComponent.class);
		doReturn(TerrainFactory.WORLD_TILE_SIZE).when(terrainComponent).getTileSize();
		GameMap gameMap = new GameMap(new TerrainFactory(new CameraComponent()));
		gameMap.setTerrainComponent(terrainComponent);
		gameMap.loadTestTerrain("configs/TestMaps/allDirt20x20_map.txt");

		//Sets the GameMap in the TestGameArea
		gameArea.setGameMap(gameMap);

		//Only needed the assets for the map loading, can be unloaded
		resourceService.unloadAssets(TerrainFactory.getMapTextures());
		resourceService.dispose();
	}

	@BeforeEach
	void beforeEach() {
		// Mock rendering, physics, game time
		RenderService renderService = new RenderService();
		renderService.setDebug(mock(DebugRenderer.class));
		ServiceLocator.registerRenderService(renderService);
		GameTime gameTime = mock(GameTime.class);
		when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
		ServiceLocator.registerTimeSource(gameTime);
		ServiceLocator.registerPhysicsService(new PhysicsService());
		ServiceLocator.registerGameArea(gameArea);
	}

	@Test
	void shouldMoveAwayFromTarget() {
		Entity target = new Entity();
		target.setPosition(12f, 12f); // changed from 2,2 to 12,12 so that entity does not walk out of bounds

		Vector2 speed = new Vector2(3f, 3f);
		AITaskComponent ai = new AITaskComponent().addTask(new RunAwayTask(target, 10, 5, 10, speed));
		Entity entity = makePhysicsEntity().addComponent(ai);
		entity.create();
		entity.setPosition(10f, 10f);// changed from 0,0 to 10,10 so that entity does not walk out of bounds

		float initialDistance = entity.getPosition().dst(target.getPosition());
		// Run the game for a few cycles
		for (int i = 0; i < 3; i++) {
			entity.earlyUpdate();
			entity.update();
			ServiceLocator.getPhysicsService().getPhysics().update();
		}
		float newDistance = entity.getPosition().dst(target.getPosition());
		assertTrue(newDistance > initialDistance);
	}

	@Test
	void shouldMoveAwayOnlyWhenInDistance() {
		Entity target = new Entity();
		target.setPosition(0f, 6f);

		Entity entity = makePhysicsEntity();
		entity.create();
		entity.setPosition(0f, 0f);

		Vector2 speed = new Vector2(3f, 3f);
		RunAwayTask runTask = new RunAwayTask(target, 10, 5, 10, speed);
		runTask.create(() -> entity);

		// Not currently active, target is too far, should have negative priority
		assertTrue(runTask.getPriority() < 0);

		// When in view distance, should give higher priority
		target.setPosition(0f, 4f);
		assertEquals(10, runTask.getPriority());

		// When active, should run away if within max run distance
		target.setPosition(0f, 8f);
		runTask.start();
		assertEquals(10, runTask.getPriority());

		// When active, should not run away outside max run distance
		target.setPosition(0f, 12f);
		assertTrue(runTask.getPriority() < 0);
	}

	@Test
	void shouldRunWhenTargetIsVisible() {
		Entity target = new Entity();
		target.setPosition(0f, 8f);

		Entity entity = makePhysicsEntity();
		entity.create();
		entity.setPosition(0f, 4f);

		Entity tree = makePhysicsEntity();
		tree.addComponent(new PhysicsComponent());
		tree.addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
		tree.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
		tree.scaleHeight(2.5f);
		PhysicsUtils.setScaledCollider(tree, 0.5f, 0.2f);
		tree.setPosition(0f, 0f);

		Vector2 speed = new Vector2(3f, 3f);
		RunAwayTask runTask = new RunAwayTask(target, 10, 10, 10, speed);
		runTask.create(() -> entity);

		// Currently active as target is visible
		assertTrue(runTask.getPriority() > 0);
	}

	@Test
	void runAwayChangesEntitySpeed() {
		Entity target = new Entity();
		target.setPosition(0f, 8f);

		Entity entity = makePhysicsEntity();
		entity.create();
		entity.setPosition(0f, 4f);

		Vector2 speed = new Vector2(3f, 3f);
		RunAwayTask runTask = new RunAwayTask(target, 10, 10, 10, speed);
		runTask.create(() -> entity);
		runTask.start();

		// Speed should be increase as run away task is active
		MovementTask movementTask = runTask.getMovementTask();
		assertSame(movementTask.getSpeed(), speed);
	}

	@Test
	void entityStopsChase() {
		Entity target = new Entity();
		target.setPosition(0f, 0f);

		Vector2 speed = new Vector2(10f, 10f);
		AITaskComponent ai = new AITaskComponent().addTask(new RunAwayTask(target, 10, 3f, 0.5f, speed));
		Entity entity = makePhysicsEntity().addComponent(ai);
		entity.create();
		entity.setPosition(1f, 0f);

		// Run the game for a few cycles to get far enough away
		for (int i = 0; i < 40; i++) {
			entity.earlyUpdate();
			entity.update();
			ServiceLocator.getPhysicsService().getPhysics().update();
		}

		//Get current position
		float initialDistance = entity.getPosition().dst(target.getPosition());

		//Check if position changes after stop
		for (int i = 0; i < 10; i++) {
			entity.earlyUpdate();
			entity.update();
			ServiceLocator.getPhysicsService().getPhysics().update();
		}

		float newDistance = entity.getPosition().dst(target.getPosition());
		assertTrue(Math.abs(newDistance - initialDistance) < 0.001);
	}

	private Entity makePhysicsEntity() {
		return new Entity()
				.addComponent(new PhysicsComponent())
				.addComponent(new PhysicsMovementComponent());
	}

	@AfterEach
	public void cleanUp() {
		// Clears all loaded services
		ServiceLocator.clear();
	}
}
