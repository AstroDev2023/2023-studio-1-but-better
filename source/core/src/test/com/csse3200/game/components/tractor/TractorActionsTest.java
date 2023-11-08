package com.csse3200.game.components.tractor;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class TractorActionsTest {
	@Test
	void muted() {
		TractorActions tractorActions = new TractorActions();
		assertTrue(tractorActions.isMuted());
		tractorActions.setMuted(true);
		assertTrue(tractorActions.isMuted());
		tractorActions.setMuted(false);
		assertFalse(tractorActions.isMuted());
	}

	@Test
	void testMove() {
		Entity tractor = new Entity(EntityType.TRACTOR);
		ServiceLocator.registerPhysicsService(new PhysicsService());
		TractorActions tractorActions = new TractorActions();
		tractor.addComponent(tractorActions).addComponent(new PhysicsComponent());
		assertFalse(tractorActions.isMoving()); // Initial state

		Vector2 direction = new Vector2(1, 1);
		tractorActions.move(direction);
		assertTrue(tractorActions.isMoving()); // Check if moving after move() is called
		tractorActions.stopMoving();
		assertFalse(tractorActions.isMoving()); // Check if moving after stopMoving() is called
	}

	@Test
	void testGetMode() {
		Entity tractor = new Entity(EntityType.TRACTOR).addComponent(new TractorActions());
		assertEquals(TractorMode.NORMAL, tractor.getComponent(TractorActions.class).getMode());
		tractor.getComponent(TractorActions.class).setMode(TractorMode.HARVESTING);
		assertEquals(TractorMode.HARVESTING, tractor.getComponent(TractorActions.class).getMode());
		tractor.getComponent(TractorActions.class).setMode(TractorMode.TILLING);
		assertEquals(TractorMode.TILLING, tractor.getComponent(TractorActions.class).getMode());
	}
}
