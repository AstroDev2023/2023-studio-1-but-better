package com.csse3200.game.components.items;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WateringCanLevelComponentTest {
	@Test
	void WateringCanLevelComponentConstructor() {
		WateringCanLevelComponent wateringCan = new WateringCanLevelComponent();
		assertEquals(50, wateringCan.getCapacity());
		assertEquals(50, wateringCan.getCurrentLevel());
	}

	@Test
	void WateringCanLevelComponentConstructorWithCapacity() {
		WateringCanLevelComponent wateringCan = new WateringCanLevelComponent(75);
		assertEquals(75, wateringCan.getCapacity());
		assertEquals(75, wateringCan.getCurrentLevel());
	}

	@Test
	void setCapacity() {
		WateringCanLevelComponent wateringCan = new WateringCanLevelComponent();
		wateringCan.setCurrentLevel(25);
		wateringCan.setCapacity(10);
		assertEquals(10, wateringCan.getCapacity());
		assertEquals(10, wateringCan.getCurrentLevel());
		wateringCan.setCapacity(50);
		assertEquals(50, wateringCan.getCapacity());
		assertEquals(10, wateringCan.getCurrentLevel());
	}

	@Test
	void setCurrentLevel() {
		WateringCanLevelComponent wateringCan = new WateringCanLevelComponent();
		wateringCan.setCurrentLevel(5);
		assertEquals(5, wateringCan.getCurrentLevel());
		wateringCan.setCurrentLevel(85);
		assertEquals(wateringCan.getCapacity(), wateringCan.getCurrentLevel());
		wateringCan.setCurrentLevel(-40);
		assertEquals(0, wateringCan.getCurrentLevel());
	}

	@Test
	void incrementLevel() {
		WateringCanLevelComponent wateringCan = new WateringCanLevelComponent();
		wateringCan.setCurrentLevel(0);
		wateringCan.incrementLevel(5);
		assertEquals(5, wateringCan.getCurrentLevel());
		wateringCan.incrementLevel(5);
		assertEquals(10, wateringCan.getCurrentLevel());
		wateringCan.incrementLevel(100);
		assertEquals(wateringCan.getCapacity(), wateringCan.getCurrentLevel());
		wateringCan.setCurrentLevel(5);
		wateringCan.incrementLevel(-10);
		assertEquals(0, wateringCan.getCurrentLevel());
	}

	@Test
	void empty() {
		WateringCanLevelComponent wateringCan = new WateringCanLevelComponent();
		wateringCan.empty();
		assertTrue(wateringCan.isEmpty());
	}

	@Test
	void fillToMax() {
		WateringCanLevelComponent wateringCan = new WateringCanLevelComponent();
		wateringCan.empty();
		assertFalse(wateringCan.isFull());
		wateringCan.fillToMax();
		assertTrue(wateringCan.isFull());
	}
}
