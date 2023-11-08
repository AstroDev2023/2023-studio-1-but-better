package com.csse3200.game.components.player;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.combat.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.PlayerHungerService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class HungerComponentTest {
	HungerComponent hungerComponent;
	PlayerHungerService mockPlayerHungerService;
	EventHandler mockEventHandler;
	int INITIAL_HUNGER_LEVEL = 30;

	@BeforeEach
	void beforeEach() {
		ServiceLocator.registerGameArea(mock(GameArea.class));

		hungerComponent = new HungerComponent(INITIAL_HUNGER_LEVEL);

		mockPlayerHungerService = mock(PlayerHungerService.class);

		ServiceLocator.registerPlayerHungerService(mockPlayerHungerService);

		mockEventHandler = mock(EventHandler.class);

		when(ServiceLocator.getPlayerHungerService().getEvents()).thenReturn(mockEventHandler);
		when(ServiceLocator.getGameArea().getPlayer()).thenReturn(mock(Entity.class));
		when(ServiceLocator.getGameArea().getPlayer().getComponent(CombatStatsComponent.class)).thenReturn(mock(CombatStatsComponent.class));
	}

	@Test
	void shouldSetGetHunger() {

		assertEquals(INITIAL_HUNGER_LEVEL, hungerComponent.getHungerLevel());

		hungerComponent.setHungerLevel(5);
		assertEquals(5, hungerComponent.getHungerLevel());
	}

	@Test
	void checkIfStarving() {
		hungerComponent.setHungerLevel(0);
		assertTrue(hungerComponent.checkIfStarving());

		hungerComponent.setHungerLevel(66);
		assertFalse(hungerComponent.checkIfStarving());
	}

	@Test
	void testIncreaseHungerLevel() {
		hungerComponent.increaseHungerLevel(1);
		assertEquals(INITIAL_HUNGER_LEVEL - 1, hungerComponent.getHungerLevel());

		hungerComponent.setHungerLevel(INITIAL_HUNGER_LEVEL);
		hungerComponent.increaseHungerLevel(-1);
		assertEquals(INITIAL_HUNGER_LEVEL + 1, hungerComponent.getHungerLevel());

		hungerComponent.setHungerLevel(0);
		hungerComponent.increaseHungerLevel(1);
		assertEquals(0, hungerComponent.getHungerLevel());

		hungerComponent.setHungerLevel(100);
		hungerComponent.increaseHungerLevel(-1);
		assertEquals(100, hungerComponent.getHungerLevel());
	}
}
