package com.csse3200.game.components.player;

import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

@ExtendWith(GameExtension.class)
class InventoryComponentTest {


	private InventoryComponent inventoryComponent;
	private Entity item1;
	private Entity item2;
	private Entity player;

	@BeforeEach
	public void setUp() {
		ServiceLocator.registerResourceService(new ResourceService());
		ServiceLocator.getResourceService().loadTextures(new String[]{"images/tool_shovel.png"});
		ServiceLocator.getResourceService().loadAll();
		ServiceLocator.registerTimeService(new TimeService());
		ServiceLocator.registerMissionManager(new MissionManager());
		// Set up the inventory with two initial items
		List<Entity> items = new ArrayList<>();
		inventoryComponent = spy(new InventoryComponent(new ArrayList<>()));
		player = new Entity().addComponent(inventoryComponent);
		player.create();
		item1 = new Entity(EntityType.ITEM);
		item2 = new Entity(EntityType.ITEM);
//    items.put(item1.getComponent(ItemComponent.class).getItemName(),item1);
//    items.put(item2.getComponent(ItemComponent.class).getItemName(),item2);
		ItemComponent itemComponent1 = new ItemComponent("itemTest1", ItemType.HOE,
				"images/tool_shovel.png"); // Texture is not used...
		ItemComponent itemComponent2 = new ItemComponent("itemTest2", ItemType.SCYTHE,
				"images/tool_shovel.png"); // Texture is not used...
		item1.addComponent(itemComponent1);
		item2.addComponent(itemComponent2);
		inventoryComponent.addItem(item1);
		inventoryComponent.addItem(item2);
	}

	/**
	 * Test case for the getInventory() method.
	 */
  /*
  @Test
  public void testGetInventory() {
    // Retrieve the inventory from the component
    List<Entity> inventory = inventoryComponent.getInventory();
    // Ensure that the inventory contains both items
    assertEquals(2, inventory.size());
    assertTrue(inventory.contains(item1));
    assertTrue(inventory.contains(item2));
  }
  */

	/**
	 * Test case for the hasItem() method.
	 */
	@Test
	void testHasItem() {
		// Check if an item is present in the inventory

		assertTrue(player.getComponent(InventoryComponent.class).hasItem(item1));
		// Check if a non-existent item is not in the inventory
		assertTrue(player.getComponent(InventoryComponent.class).hasItem("itemTest1"));
		assertFalse(player.getComponent(InventoryComponent.class).hasItem(new Entity()));
	}

	/**
	 * Test case for the addItem() method.
	 */

	@Test
	void testAddItem() {
		// Create a new item
		Entity newItem = new Entity(EntityType.ITEM);
		ItemComponent itemComponent3 = new ItemComponent("itemTest3", ItemType.SCYTHE,
				"images/tool_shovel.png"); // Texture is not used...
		// Add the new item to the inventory
		newItem.addComponent(itemComponent3);
		assertTrue(inventoryComponent.addItem(newItem));
		// Check if the new item is now in the inventory
		assertTrue(inventoryComponent.hasItem(newItem));
	}

	/**
	 * Test case for the removeItem() method.
	 */
	@Test
	void testRemoveItem() {
		// Remove an item from the inventory
		Entity newItem = new Entity(EntityType.ITEM);
		ItemComponent itemComponent3 = new ItemComponent("itemTest3", ItemType.SCYTHE,
				"images/tool_shovel.png"); // Texture is not used...
		// Add the new item to the inventory
		newItem.addComponent(itemComponent3);
		if (inventoryComponent.getItemCount(item1) == 0) {
			inventoryComponent.addItem(item1);
		}
		assertTrue(inventoryComponent.removeItem(item1));
		// Check that the removed item is no longer in the inventory
		assertFalse(inventoryComponent.hasItem(item1));
		// Check that removing a non-existent item does not affect the inventory
		assertFalse(inventoryComponent.removeItem(new Entity()));
		assertFalse(inventoryComponent.removeItem(new Entity(EntityType.ITEM)));

		// testing RemoveAll
		inventoryComponent.addItem(item1);
		inventoryComponent.addItem(item1);
		inventoryComponent.addItem(item1);
		assertEquals(3, inventoryComponent.getItemCount(item1));
		inventoryComponent.removeAll(item1);
		assertEquals(0, inventoryComponent.getItemCount(item1));

	}

	@Test
	void testGetItemCount() {
		assertEquals(1, inventoryComponent.getItemCount(item1));
		inventoryComponent.addItem(item1);
		assertEquals(2, inventoryComponent.getItemCount(item1));
		inventoryComponent.removeItem(item1);
		inventoryComponent.removeItem(item1);
		assertEquals(0, inventoryComponent.getItemCount(item1));

	}


	@Test
	void testSetInventorySize() {
		assertEquals(30, inventoryComponent.getInventorySize());
		assertTrue(inventoryComponent.setInventorySize(10));
		assertEquals(10, inventoryComponent.getInventorySize());
		assertFalse(inventoryComponent.setInventorySize(-1));
	}

	@Test
	void testSwapPosition() {
		String pos1 = inventoryComponent.getItemName(0);
		String pos2 = inventoryComponent.getItemName(1);
		assertTrue(inventoryComponent.swapPosition(0, 1));
		assertEquals(inventoryComponent.getItemName(0), pos2);
		assertEquals(inventoryComponent.getItemName(1), pos1);
		int inventorySize = inventoryComponent.getInventorySize();
		inventorySize = inventorySize + 10;
		assertFalse(inventoryComponent.swapPosition(0, inventorySize));
		assertFalse(inventoryComponent.swapPosition(0, -10));
	}

	@Test
	void setPosition() {
		String pos1 = inventoryComponent.getItemName(0);
		String pos2 = inventoryComponent.getItemName(1);
		Entity testPos = new Entity(EntityType.ITEM);
		ItemComponent itemComponent3 = new ItemComponent("itemTest3", ItemType.SCYTHE,
				"images/tool_shovel.png"); // Texture is not used...
		testPos.addComponent(itemComponent3);
		assertFalse(inventoryComponent.setPosition(item1, 0));
		assertFalse(inventoryComponent.setPosition(item1, -1));
		assertFalse(inventoryComponent.setPosition(testPos, 0));
		assertTrue(inventoryComponent.setPosition(testPos, 5));

	}

/*  @Test
  void testRemoveTools(){
    Entity itemTest = new Entity(EntityType.ITEM);
    ItemComponent itemComponent3 = new ItemComponent("shovel", ItemType.SCYTHE,
            "images/tool_shovel.png"); // Texture is not used...
    itemTest.addComponent(itemComponent3);
    inventoryComponent.addItem(itemTest);
    assertFalse(inventoryComponent.removeItem(itemTest));
  }*/
  /*
  @Test
  void testGetItemPosition() {
    assertEquals(inventoryComponent.getItemPosition(item1), new Point(0, 0));
  }

  @Test
  void testSetItemPosition() {
    assertEquals(inventoryComponent.getItemPosition(item1), new Point(0, 0));
    inventoryComponent.setItemPosition(item1,new Point(2,2));
    assertEquals(inventoryComponent.getItemPosition(item1), new Point(2,2));
  }

   */

}

