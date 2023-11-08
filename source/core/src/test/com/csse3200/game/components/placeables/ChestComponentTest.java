package com.csse3200.game.components.placeables;

import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class ChestComponentTest {
	@Test
	void toggleChest() {
		ServiceLocator.registerResourceService(new ResourceService());

		Entity chest = new Entity();
		chest.addComponent(new InventoryComponent())
				.addComponent(new ChestComponent());
	}
}
