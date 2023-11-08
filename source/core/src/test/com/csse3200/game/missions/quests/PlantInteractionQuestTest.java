package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PlantInteractionQuestTest {
	private PlantInteractionQuest PIQuest1, PIQuest2, PIQuest3, PIQuest4, PIQuest5, PIQuest6, PIQuest7, PIQuest8, PIQuest9;
	private Reward r1, r2, r3, r4, r5, r6, r7;

	@BeforeEach
	public void init() {
		ServiceLocator.registerTimeSource(new GameTime());
		ServiceLocator.registerTimeService(new TimeService());
		ServiceLocator.registerMissionManager(new MissionManager());

		r1 = mock(Reward.class);
		r2 = mock(Reward.class);
		r3 = mock(Reward.class);
		r4 = mock(Reward.class);
		r5 = mock(Reward.class);
		r6 = mock(Reward.class);
		r7 = mock(Reward.class);


		Set<String> plantTypes1 = new HashSet<>();
		Set<String> plantTypes2 = new HashSet<>();
		plantTypes1.add("Cosmic Cob");

		plantTypes2.add("Aloe Vera");
		plantTypes2.add("Cosmic Cob");

		MissionManager.MissionEvent plant = MissionManager.MissionEvent.PLANT_CROP;
		MissionManager.MissionEvent harvest = MissionManager.MissionEvent.HARVEST_CROP;
		MissionManager.MissionEvent water = MissionManager.MissionEvent.WATER_CROP;
		MissionManager.MissionEvent bug = MissionManager.MissionEvent.COMBAT_ACTOR_DEFEATED;

		PIQuest1 = new PlantInteractionQuest("Plant Interaction Quest 1", r1, plant, plantTypes1, 10);
		PIQuest2 = new PlantInteractionQuest("Plant Interaction Quest 2", r2, plant, plantTypes1, 0);
		PIQuest3 = new PlantInteractionQuest("Plant Interaction Quest 3", r3, harvest, plantTypes1, 10);
		PIQuest4 = new PlantInteractionQuest("Plant Interaction Quest 4", r4, plant, plantTypes2, 10);
		PIQuest5 = new PlantInteractionQuest("Plant Interaction Quest 5", r5, harvest, plantTypes2, -1);
		PIQuest6 = new PlantInteractionQuest("Plant Interaction Quest 6", r6, 10, harvest, plantTypes2, 3);
		PIQuest7 = new PlantInteractionQuest("Plant Interaction Quest 7", r7, 10, plant, plantTypes2, 3);
		PIQuest8 = new PlantInteractionQuest("Plant Interaction Quest 8", r7, 10, bug, plantTypes1, 3);
		PIQuest9 = new PlantInteractionQuest("Plant Interaction Quest 9", r7, 10, water, plantTypes1, 3);

		PIQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
		PIQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
		PIQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
		PIQuest4.registerMission(ServiceLocator.getMissionManager().getEvents());
		PIQuest5.registerMission(ServiceLocator.getMissionManager().getEvents());
		PIQuest6.registerMission(ServiceLocator.getMissionManager().getEvents());
		PIQuest7.registerMission(ServiceLocator.getMissionManager().getEvents());
		PIQuest9.registerMission(ServiceLocator.getMissionManager().getEvents());

	}

	@AfterEach
	public void reset() {
		ServiceLocator.clear();
	}

	@Test
	void testRegisterMission() {
		ServiceLocator.clear();
		assertFalse(PIQuest1.isCompleted());
		assertTrue(PIQuest2.isCompleted());
		assertFalse(PIQuest3.isCompleted());
		assertFalse(PIQuest4.isCompleted());
		assertTrue(PIQuest5.isCompleted());
		assertFalse(PIQuest6.isCompleted());
		assertFalse(PIQuest7.isCompleted());
		PIQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
		PIQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
		PIQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
		PIQuest4.registerMission(ServiceLocator.getMissionManager().getEvents());
		PIQuest5.registerMission(ServiceLocator.getMissionManager().getEvents());
		PIQuest6.registerMission(ServiceLocator.getMissionManager().getEvents());
		PIQuest7.registerMission(ServiceLocator.getMissionManager().getEvents());
		assertFalse(PIQuest1.isCompleted());
		assertTrue(PIQuest2.isCompleted());
		assertFalse(PIQuest3.isCompleted());
		assertFalse(PIQuest4.isCompleted());
		assertTrue(PIQuest5.isCompleted());
		assertFalse(PIQuest6.isCompleted());
		assertFalse(PIQuest7.isCompleted());
	}

	@Test
	void testPlantIsCompleted() {
		assertFalse(PIQuest1.isCompleted());
		assertTrue(PIQuest2.isCompleted());
		assertFalse(PIQuest3.isCompleted());
		assertFalse(PIQuest4.isCompleted());
		assertTrue(PIQuest5.isCompleted());
		assertFalse(PIQuest6.isCompleted());
		assertFalse(PIQuest7.isCompleted());
		for (int i = 0; i < 10; i++) {
			ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.PLANT_CROP.name(),
					"Aloe Vera");
		}
		assertFalse(PIQuest1.isCompleted());
		assertTrue(PIQuest2.isCompleted());
		assertFalse(PIQuest3.isCompleted());
		assertTrue(PIQuest4.isCompleted());
		assertTrue(PIQuest5.isCompleted());
		assertFalse(PIQuest6.isCompleted());
		assertTrue(PIQuest7.isCompleted());
		PIQuest4.resetState();
		PIQuest7.resetState();
		for (int i = 0; i < 10; i++) {
			ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.PLANT_CROP.name(),
					"Cosmic Cob");
		}
		assertTrue(PIQuest1.isCompleted());
		assertTrue(PIQuest2.isCompleted());
		assertFalse(PIQuest3.isCompleted());
		assertTrue(PIQuest4.isCompleted());
		assertTrue(PIQuest5.isCompleted());
		assertFalse(PIQuest6.isCompleted());
		assertTrue(PIQuest7.isCompleted());
	}

	@Test
	void testHarvestIsCompleted() {
		assertFalse(PIQuest1.isCompleted());
		assertTrue(PIQuest2.isCompleted());
		assertFalse(PIQuest3.isCompleted());
		assertFalse(PIQuest4.isCompleted());
		assertTrue(PIQuest5.isCompleted());
		assertFalse(PIQuest6.isCompleted());
		assertFalse(PIQuest7.isCompleted());
		for (int i = 0; i < 10; i++) {
			ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.HARVEST_CROP.name(),
					"Aloe Vera");
		}
		assertFalse(PIQuest1.isCompleted());
		assertTrue(PIQuest2.isCompleted());
		assertFalse(PIQuest3.isCompleted());
		assertFalse(PIQuest4.isCompleted());
		assertTrue(PIQuest5.isCompleted());
		assertTrue(PIQuest6.isCompleted());
		assertFalse(PIQuest7.isCompleted());
		PIQuest6.resetState();
		for (int i = 0; i < 10; i++) {
			ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.HARVEST_CROP.name(),
					"Cosmic Cob");
		}
		assertFalse(PIQuest1.isCompleted());
		assertTrue(PIQuest2.isCompleted());
		assertTrue(PIQuest3.isCompleted());
		assertFalse(PIQuest4.isCompleted());
		assertTrue(PIQuest5.isCompleted());
		assertTrue(PIQuest6.isCompleted());
		assertFalse(PIQuest7.isCompleted());
	}

	@Test
	void testWaterIsCompleted() {
		assertFalse(PIQuest9.isCompleted());
		for (int i = 0; i < 3; i++) {
			ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.WATER_CROP.name(),
					"Cosmic Cob");
		}
		assertTrue(PIQuest9.isCompleted());
	}

	@Test
	void testPlantGetDescription() {
		String desc1 = "Plant %d crops of type Cosmic Cob.\n%d out of %d crops planted.";
		String desc2 = "Plant %d crops of type Aloe Vera, Cosmic Cob.\n%d out of %d crops planted.";
		String bugDesc = "%d crops of type Cosmic Cob.\n%d out of %d.";

		for (int i = 0; i < 10; i++) {
			int min7 = Math.min(i, 3);
			String formatted1 = String.format(desc1, 10, 0, 10);
			String formatted2 = String.format(desc1, 0, 0, 0);
			String formatted4 = String.format(desc2, 10, i, 10);
			String formatted7 = String.format(desc2, 3, min7, 3);
			String formatted8 = String.format(bugDesc, 3, 0, 3);
			assertEquals(formatted1, PIQuest1.getDescription());
			assertEquals(formatted2, PIQuest2.getDescription());
			assertEquals(formatted4, PIQuest4.getDescription());
			assertEquals(formatted7, PIQuest7.getDescription());
			assertEquals(formatted8, PIQuest8.getDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.PLANT_CROP.name(), "Aloe Vera");
		}
		PIQuest1.resetState();
		PIQuest2.resetState();
		PIQuest4.resetState();
		PIQuest7.resetState();
		for (int i = 0; i < 10; i++) {
			int min7 = Math.min(i, 3);
			String formatted1 = String.format(desc1, 10, i, 10);
			String formatted2 = String.format(desc1, 0, 0, 0);
			String formatted4 = String.format(desc2, 10, i, 10);
			String formatted7 = String.format(desc2, 3, min7, 3);
			assertEquals(formatted1, PIQuest1.getDescription());
			assertEquals(formatted2, PIQuest2.getDescription());
			assertEquals(formatted4, PIQuest4.getDescription());
			assertEquals(formatted7, PIQuest7.getDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.PLANT_CROP.name(), "Cosmic Cob");
		}
	}

	@Test
	void testHarvestGetDescription() {
		String desc3 = "Harvest %d crops of type Cosmic Cob.\n%d out of %d crops harvested.";
		String desc4 = "Harvest %d crops of type Aloe Vera, Cosmic Cob.\n%d out of %d crops harvested.";
		String bugDesc = "%d crops of type Cosmic Cob.\n%d out of %d.";


		for (int i = 0; i < 10; i++) {
			int min7 = Math.min(i, 3);
			String formatted3 = String.format(desc3, 10, 0, 10);
			String formatted5 = String.format(desc4, 0, 0, 0);
			String formatted6 = String.format(desc4, 3, min7, 3);
			assertEquals(formatted3, PIQuest3.getDescription());
			assertEquals(formatted5, PIQuest5.getDescription());
			assertEquals(formatted6, PIQuest6.getDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.HARVEST_CROP.name(), "Aloe Vera");
		}
		PIQuest3.resetState();
		PIQuest5.resetState();
		PIQuest6.resetState();
		for (int i = 0; i < 10; i++) {
			int min7 = Math.min(i, 3);
			String formatted3 = String.format(desc3, 10, i, 10);
			String formatted5 = String.format(desc4, 0, 0, 0);
			String formatted6 = String.format(desc4, 3, min7, 3);
			assertEquals(formatted3, PIQuest3.getDescription());
			assertEquals(formatted5, PIQuest5.getDescription());
			assertEquals(formatted6, PIQuest6.getDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.HARVEST_CROP.name(), "Cosmic Cob");
		}
	}

	@Test
	void testWaterGetDescription() {
		String desc5 = "Water %d crops of type Cosmic Cob.\n%d out of %d crops watered.";

		for (int i = 0; i < 3; i++) {
			String formatted = String.format(desc5, 3, i, 3);
			assertEquals(formatted, PIQuest9.getDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.WATER_CROP.name(), "Cosmic Cob");
		}
	}

	@Test
	void testPlantGetShortDescription() {
		String desc1 = "%d out of %d crops planted";
		for (int i = 0; i < 10; i++) {
			int min7 = Math.min(i, 3);
			String formatted1 = String.format(desc1, 0, 10);
			String formatted2 = String.format(desc1, 0, 0);
			String formatted4 = String.format(desc1, i, 10);
			String formatted7 = String.format(desc1, min7, 3);
			assertEquals(formatted1, PIQuest1.getShortDescription());
			assertEquals(formatted2, PIQuest2.getShortDescription());
			assertEquals(formatted4, PIQuest4.getShortDescription());
			assertEquals(formatted7, PIQuest7.getShortDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.PLANT_CROP.name(), "Aloe Vera");
		}
		PIQuest1.resetState();
		PIQuest2.resetState();
		PIQuest4.resetState();
		PIQuest7.resetState();
		for (int i = 0; i < 10; i++) {
			int min7 = Math.min(i, 3);
			String formatted1 = String.format(desc1, i, 10);
			String formatted2 = String.format(desc1, 0, 0);
			String formatted4 = String.format(desc1, i, 10);
			String formatted7 = String.format(desc1, min7, 3);
			assertEquals(formatted1, PIQuest1.getShortDescription());
			assertEquals(formatted2, PIQuest2.getShortDescription());
			assertEquals(formatted4, PIQuest4.getShortDescription());
			assertEquals(formatted7, PIQuest7.getShortDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.PLANT_CROP.name(), "Cosmic Cob");
		}
		PIQuest1.resetState();
		PIQuest2.resetState();
		PIQuest4.resetState();
		PIQuest7.resetState();
		for (int i = 0; i < 10; i++) {
			int min7 = Math.min(i, 3);
			String formatted1 = String.format(desc1, 0, 10);
			String formatted2 = String.format(desc1, 0, 0);
			String formatted4 = String.format(desc1, 0, 10);
			String formatted7 = String.format(desc1, 0, 3);
			assertEquals(formatted1, PIQuest1.getShortDescription());
			assertEquals(formatted2, PIQuest2.getShortDescription());
			assertEquals(formatted4, PIQuest4.getShortDescription());
			assertEquals(formatted7, PIQuest7.getShortDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.HARVEST_CROP.name(), "Aloe Vera");
		}
		PIQuest1.resetState();
		PIQuest2.resetState();
		PIQuest4.resetState();
		PIQuest7.resetState();
		for (int i = 0; i < 10; i++) {
			int min7 = Math.min(i, 3);
			String formatted1 = String.format(desc1, 0, 10);
			String formatted2 = String.format(desc1, 0, 0);
			String formatted4 = String.format(desc1, 0, 10);
			String formatted7 = String.format(desc1, 0, 3);
			assertEquals(formatted1, PIQuest1.getShortDescription());
			assertEquals(formatted2, PIQuest2.getShortDescription());
			assertEquals(formatted4, PIQuest4.getShortDescription());
			assertEquals(formatted7, PIQuest7.getShortDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.HARVEST_CROP.name(), "Cosmic Cob");
		}
	}

	@Test
	void testHarvestGetShortDescription() {
		String desc2 = "%d out of %d crops harvested";
		for (int i = 0; i < 10; i++) {
			String formatted3 = String.format(desc2, 0, 10);
			String formatted5 = String.format(desc2, 0, 0);
			String formatted6 = String.format(desc2, 0, 3);
			assertEquals(formatted3, PIQuest3.getShortDescription());
			assertEquals(formatted5, PIQuest5.getShortDescription());
			assertEquals(formatted6, PIQuest6.getShortDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.PLANT_CROP.name(), "Aloe Vera");
		}
		PIQuest3.resetState();
		PIQuest5.resetState();
		PIQuest6.resetState();
		for (int i = 0; i < 10; i++) {
			String formatted3 = String.format(desc2, 0, 10);
			String formatted5 = String.format(desc2, 0, 0);
			String formatted6 = String.format(desc2, 0, 3);
			assertEquals(formatted3, PIQuest3.getShortDescription());
			assertEquals(formatted5, PIQuest5.getShortDescription());
			assertEquals(formatted6, PIQuest6.getShortDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.PLANT_CROP.name(), "Cosmic Cob");
		}
		PIQuest1.resetState();
		PIQuest2.resetState();
		PIQuest3.resetState();
		PIQuest4.resetState();
		PIQuest5.resetState();
		PIQuest6.resetState();
		PIQuest7.resetState();
		for (int i = 0; i < 10; i++) {
			int min7 = Math.min(i, 3);
			String formatted3 = String.format(desc2, 0, 10);
			String formatted5 = String.format(desc2, 0, 0);
			String formatted6 = String.format(desc2, min7, 3);
			assertEquals(formatted3, PIQuest3.getShortDescription());
			assertEquals(formatted5, PIQuest5.getShortDescription());
			assertEquals(formatted6, PIQuest6.getShortDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.HARVEST_CROP.name(), "Aloe Vera");
		}
		PIQuest1.resetState();
		PIQuest2.resetState();
		PIQuest3.resetState();
		PIQuest4.resetState();
		PIQuest5.resetState();
		PIQuest6.resetState();
		PIQuest7.resetState();
		for (int i = 0; i < 10; i++) {
			int min7 = Math.min(i, 3);
			String formatted3 = String.format(desc2, i, 10);
			String formatted5 = String.format(desc2, 0, 0);
			String formatted6 = String.format(desc2, min7, 3);
			assertEquals(formatted3, PIQuest3.getShortDescription());
			assertEquals(formatted5, PIQuest5.getShortDescription());
			assertEquals(formatted6, PIQuest6.getShortDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.HARVEST_CROP.name(), "Cosmic Cob");
		}
	}

	@Test
	void testWaterGetShortDescription() {
		String desc5 = "%d out of %d crops watered";

		for (int i = 0; i < 3; i++) {
			String formatted = String.format(desc5, i, 3);
			assertEquals(formatted, PIQuest9.getShortDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.WATER_CROP.name(), "Cosmic Cob");
		}
	}

	@Test
	void testReadProgress() {
		int progressInt = 3;
		JsonValue progress = new JsonValue(progressInt);
		String desc1 = "Plant %d crops of type Cosmic Cob.\n%d out of %d crops planted.";
		String desc2 = "Plant %d crops of type Aloe Vera, Cosmic Cob.\n%d out of %d crops planted.";
		String desc3 = "Harvest %d crops of type Cosmic Cob.\n%d out of %d crops harvested.";
		String desc4 = "Harvest %d crops of type Aloe Vera, Cosmic Cob.\n%d out of %d crops harvested.";
		String shortDesc1 = "%d out of %d crops planted";
		String shortDesc2 = "%d out of %d crops harvested";
		PIQuest1.readProgress(progress);
		PIQuest2.readProgress(progress);
		PIQuest3.readProgress(progress);
		PIQuest4.readProgress(progress);
		PIQuest5.readProgress(progress);
		PIQuest6.readProgress(progress);
		PIQuest7.readProgress(progress);
		String formatted1 = String.format(desc1, 10, progressInt, 10);
		String formatted2 = String.format(desc1, 0, progressInt, 0);
		String formatted3 = String.format(desc3, 10, progressInt, 10);
		String formatted4 = String.format(desc2, 10, progressInt, 10);
		String formatted5 = String.format(desc4, 0, progressInt, 0);
		String formatted6 = String.format(desc4, 3, progressInt, 3);
		String formatted7 = String.format(desc2, 3, progressInt, 3);
		String shortFormatted1 = String.format(shortDesc1, progressInt, 10);
		String shortFormatted2 = String.format(shortDesc1, progressInt, 0);
		String shortFormatted3 = String.format(shortDesc2, progressInt, 10);
		String shortFormatted4 = String.format(shortDesc1, progressInt, 10);
		String shortFormatted5 = String.format(shortDesc2, progressInt, 0);
		String shortFormatted6 = String.format(shortDesc2, progressInt, 3);
		String shortFormatted7 = String.format(shortDesc1, progressInt, 3);
		assertEquals(formatted1, PIQuest1.getDescription());
		assertEquals(formatted2, PIQuest2.getDescription());
		assertEquals(formatted3, PIQuest3.getDescription());
		assertEquals(formatted4, PIQuest4.getDescription());
		assertEquals(formatted5, PIQuest5.getDescription());
		assertEquals(formatted6, PIQuest6.getDescription());
		assertEquals(formatted7, PIQuest7.getDescription());
		assertEquals(shortFormatted1, PIQuest1.getShortDescription());
		assertEquals(shortFormatted2, PIQuest2.getShortDescription());
		assertEquals(shortFormatted3, PIQuest3.getShortDescription());
		assertEquals(shortFormatted4, PIQuest4.getShortDescription());
		assertEquals(shortFormatted5, PIQuest5.getShortDescription());
		assertEquals(shortFormatted6, PIQuest6.getShortDescription());
		assertEquals(shortFormatted7, PIQuest7.getShortDescription());
	}

	@Test
	void testGetProgress() {
		assertEquals(0, PIQuest1.getProgress());
		assertEquals(0, PIQuest3.getProgress());
		assertEquals(0, PIQuest6.getProgress());
		assertEquals(0, PIQuest7.getProgress());
		for (int i = 0; i < 10; i++) {
			ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.PLANT_CROP.name(),
					"Cosmic Cob");
		}
		assertEquals(10, PIQuest1.getProgress());
		assertEquals(3, PIQuest7.getProgress());
		for (int i = 0; i < 10; i++) {
			ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.HARVEST_CROP.name(),
					"Cosmic Cob");
		}
		assertEquals(10, PIQuest3.getProgress());
		assertEquals(3, PIQuest6.getProgress());
	}

}