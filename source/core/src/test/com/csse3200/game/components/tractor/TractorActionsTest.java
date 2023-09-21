package com.csse3200.game.components.tractor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

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
    ServiceLocator.registerPhysicsService(new PhysicsService());
    TractorActions tractorActions = new TractorActions();
    tractorActions.setPhysicsComponent(new PhysicsComponent());
    assertFalse(tractorActions.isMoving()); // Initial state

    Vector2 direction = new Vector2(1, 1);
    tractorActions.move(direction);
    assertTrue(tractorActions.isMoving()); // Check if moving after move() is called
    tractorActions.stopMoving();
    assertFalse(tractorActions.isMoving()); // Check if moving after stopMoving() is called
  }

}
