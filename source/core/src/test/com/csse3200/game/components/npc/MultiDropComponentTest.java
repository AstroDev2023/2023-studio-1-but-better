package com.csse3200.game.components.npc;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.components.items.ItemActions;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;


@ExtendWith(GameExtension.class)
class MultiDropComponentTest {
	private Entity entity;
	private TimeService timeService;
	private int initialEntityCount;
	private MultiDropComponent multiDropComponent;
	String[] texturePaths = {"images/dont_delete_test_image.png"};

	private Entity createDummyItem() {
		return new Entity(EntityType.DUMMY)
				.addComponent(new PhysicsComponent())
				.addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
				.addComponent(new ItemActions())
				.addComponent(new ItemComponent("dummy", ItemType.FERTILISER,
						"images/dont_delete_test_image.png"));
	}

	private Entity createDummyEntity() {
		Entity dummyPlayer = new Entity();
		entity = new Entity();

		//Set up tamable component
		TamableComponent tamableComponent = new TamableComponent(dummyPlayer, 2, 1.1, "AFood");
		entity.addComponent(tamableComponent);

		//Set up multi-drop component
		List<SingleDropHandler> singleDropHandlers = new ArrayList<>();

		//Drop handler for untamed, 1 trigger to next drop, listen on entity
		singleDropHandlers.add(new SingleDropHandler(this::createDummyItem, 1,
				entity.getEvents()::addListener, "untamed-entity-1-trigger", false));

		//Drop handler for untamed, 2 triggers to next drop, listen on entity
		singleDropHandlers.add(new SingleDropHandler(this::createDummyItem, 2,
				entity.getEvents()::addListener, "untamed-entity-2-trigger", false));
		entity.addComponent(new MultiDropComponent(singleDropHandlers, false));

		//Drop handler for tamed, 1 trigger to next drop, listen on time service
		singleDropHandlers.add(new SingleDropHandler(this::createDummyItem, 1,
				timeService.getEvents()::addListener, "hourUpdate", true));

		multiDropComponent = new MultiDropComponent(singleDropHandlers, true);

		entity.addComponent(multiDropComponent);

		entity.create();
		return entity;
	}

	private int getDummyEntityCount() {
		Array<Entity> entities = ServiceLocator.getEntityService().getEntities();
		int count = 0;
		for (int i = 0; i < entities.size; i++) {
			if (entities.get(i).getType().equals(EntityType.DUMMY)) {
				count++;
			}
		}
		return count;
	}

	@BeforeEach
	void beforeEach() {
		ServiceLocator.registerResourceService(new ResourceService());
		ServiceLocator.getResourceService().loadTextures(texturePaths);
		ServiceLocator.registerPhysicsService(new PhysicsService());
		ServiceLocator.registerEntityService(new EntityService());
		ServiceLocator.registerRenderService(new RenderService());
		ServiceLocator.registerGameArea(new GameArea() {
			@Override
			public void create() {
				//Do nothing
			}

			@Override
			public Entity getPlayer() {
				return null;
			}

			@Override
			public ClimateController getClimateController() {
				return null;
			}

			@Override
			public Entity getTractor() {
				return null;
			}

			@Override
			public GameMap getMap() {
				return null;
			}
		});
		GameTime gameTime = mock(GameTime.class);
		ServiceLocator.registerTimeSource(gameTime);
		timeService = new TimeService();
		ServiceLocator.registerTimeService(timeService);
		ServiceLocator.getResourceService().loadAll();
		initialEntityCount = getDummyEntityCount();
	}

	@Test
	void checkItemDropsUntamed() {
		Entity entity = createDummyEntity();
		assertEquals(initialEntityCount, getDummyEntityCount());

		//Drop should not occur as requires two triggers
		entity.getEvents().trigger("untamed-entity-2-trigger");
		assertEquals(initialEntityCount, getDummyEntityCount());

		//Drop should not occur as entity not tamed
		timeService.setHour(1);
		assertEquals(initialEntityCount, getDummyEntityCount());

		//Drop should occur
		entity.getEvents().trigger("untamed-entity-1-trigger");
		assertEquals(initialEntityCount + 1, getDummyEntityCount());

	}

	@Test
	void checkItemDropsTamed() {
		Entity entity = createDummyEntity();
		assertEquals(initialEntityCount, getDummyEntityCount());

		entity.getComponent(TamableComponent.class).setTame(true);

		//Drop should not occur as requires two triggers
		entity.getEvents().trigger("untamed-entity-2-trigger");
		assertEquals(initialEntityCount, getDummyEntityCount());

		//Drop should occur as entity tamed
		timeService.setHour(1);
		assertEquals(initialEntityCount + 1, getDummyEntityCount());

		//Drop should occur
		entity.getEvents().trigger("untamed-entity-1-trigger");
		assertEquals(initialEntityCount + 2, getDummyEntityCount());
	}

	@Test
	void checkItemDropRate() {
		Entity entity = createDummyEntity();

		//Drop should not occur as requires two triggers
		entity.getEvents().trigger("untamed-entity-2-trigger");
		assertEquals(initialEntityCount, getDummyEntityCount());

		//Drop should occur as second trigger
		entity.getEvents().trigger("untamed-entity-2-trigger");
		assertEquals(initialEntityCount + 1, getDummyEntityCount());
	}

	@Test
	void handlesDeath() {
		Entity entity = createDummyEntity();
		assertTrue(multiDropComponent.getHandlesDeath());
		multiDropComponent.dispose();

		//Drop should not occur
		entity.getEvents().trigger("untamed-entity-1-trigger");
		assertEquals(initialEntityCount, getDummyEntityCount());
	}
}
