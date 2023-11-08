package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.entities.EntityType;
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

class ManageHostilesQuestTest {
	private ManageHostilesQuest MHQuest1, MHQuest2, MHQuest3, MHQuest4, MHQuest5, MHQuest6, MHQuest7;
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

		Set<EntityType> hostileTypes1 = new HashSet<>();
		Set<EntityType> hostileTypes2 = new HashSet<>();
		Set<EntityType> hostileTypes3 = new HashSet<>();
		hostileTypes1.add(EntityType.OXYGEN_EATER);

		hostileTypes2.add(EntityType.OXYGEN_EATER);
		hostileTypes2.add(EntityType.COW);

		hostileTypes3.add(EntityType.OXYGEN_EATER);
		hostileTypes3.add(EntityType.COW);
		hostileTypes3.add(EntityType.ASTROLOTL);

		MHQuest1 = new ManageHostilesQuest("Manage Hostiles Quest 1", r1, hostileTypes1, 10);
		MHQuest2 = new ManageHostilesQuest("Manage Hostiles Quest 2", r2, hostileTypes2, 0);
		MHQuest3 = new ManageHostilesQuest("Manage Hostiles Quest 3", r3, hostileTypes2, 10);
		MHQuest4 = new ManageHostilesQuest("Manage Hostiles Quest 4", r4, hostileTypes3, 10);
		MHQuest5 = new ManageHostilesQuest("Manage Hostiles Quest 5", r5, hostileTypes3, -1);
		MHQuest6 = new ManageHostilesQuest("Manage Hostiles Quest 6", r6, 10, hostileTypes3, 10);
		MHQuest7 = new ManageHostilesQuest("Manage Hostiles Quest 7", r7, hostileTypes3, 3);

		MHQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
		MHQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
		MHQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
		MHQuest4.registerMission(ServiceLocator.getMissionManager().getEvents());
		MHQuest5.registerMission(ServiceLocator.getMissionManager().getEvents());
		MHQuest6.registerMission(ServiceLocator.getMissionManager().getEvents());
		MHQuest7.registerMission(ServiceLocator.getMissionManager().getEvents());

	}

	@AfterEach
	public void reset() {
		ServiceLocator.clear();
	}

	@Test
	void testRegisterMission() {
		ServiceLocator.clear();
		assertFalse(MHQuest1.isCompleted());
		assertTrue(MHQuest2.isCompleted());
		assertFalse(MHQuest3.isCompleted());
		assertFalse(MHQuest4.isCompleted());
		assertTrue(MHQuest5.isCompleted());
		assertFalse(MHQuest6.isCompleted());
		assertFalse(MHQuest7.isCompleted());
		MHQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
		MHQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
		MHQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
		MHQuest4.registerMission(ServiceLocator.getMissionManager().getEvents());
		MHQuest5.registerMission(ServiceLocator.getMissionManager().getEvents());
		MHQuest6.registerMission(ServiceLocator.getMissionManager().getEvents());
		MHQuest7.registerMission(ServiceLocator.getMissionManager().getEvents());
		assertFalse(MHQuest1.isCompleted());
		assertTrue(MHQuest2.isCompleted());
		assertFalse(MHQuest3.isCompleted());
		assertFalse(MHQuest4.isCompleted());
		assertTrue(MHQuest5.isCompleted());
		assertFalse(MHQuest6.isCompleted());
		assertFalse(MHQuest7.isCompleted());
	}

	@Test
	void testIsCompleted() {
		for (int i = 0; i < 10; i++) {
			ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.COMBAT_ACTOR_DEFEATED.name(),
					EntityType.ASTROLOTL);
		}
		assertFalse(MHQuest1.isCompleted());
		assertFalse(MHQuest3.isCompleted());
		assertTrue(MHQuest4.isCompleted());
		assertTrue(MHQuest6.isCompleted());
		assertTrue(MHQuest7.isCompleted());
		MHQuest4.resetState();
		MHQuest6.resetState();
		MHQuest7.resetState();
		assertFalse(MHQuest4.isCompleted());
		assertFalse(MHQuest6.isCompleted());
		assertFalse(MHQuest7.isCompleted());
		for (int i = 0; i < 10; i++) {
			ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.COMBAT_ACTOR_DEFEATED.name(),
					EntityType.COW);
		}
		assertFalse(MHQuest1.isCompleted());
		assertTrue(MHQuest3.isCompleted());
		assertTrue(MHQuest4.isCompleted());
		assertTrue(MHQuest6.isCompleted());
		assertTrue(MHQuest7.isCompleted());
		MHQuest3.resetState();
		MHQuest4.resetState();
		MHQuest6.resetState();
		MHQuest7.resetState();
		assertFalse(MHQuest3.isCompleted());
		assertFalse(MHQuest4.isCompleted());
		assertFalse(MHQuest6.isCompleted());
		assertFalse(MHQuest7.isCompleted());
		for (int i = 0; i < 10; i++) {
			ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.COMBAT_ACTOR_DEFEATED.name(),
					EntityType.OXYGEN_EATER);
		}
		assertTrue(MHQuest1.isCompleted());
		assertTrue(MHQuest2.isCompleted());
		assertTrue(MHQuest3.isCompleted());
		assertTrue(MHQuest4.isCompleted());
		assertTrue(MHQuest5.isCompleted());
		assertTrue(MHQuest6.isCompleted());
		assertTrue(MHQuest7.isCompleted());
	}

	@Test
	void testGetDescription() {
		String desc1 = "Manage the presence of hostile creatures on your farm.\n" +
				"Deal with hostile creatures by defeating them with weapons, or let your Space Snappers eat them.\n" +
				"Deal with %d creatures of type Oxygen Eater.\n" +
				"%d out of %d hostiles dealt with.";
		String desc2 = "Manage the presence of hostile creatures on your farm.\n" +
				"Deal with hostile creatures by defeating them with weapons, or let your Space Snappers eat them.\n" +
				"Deal with %d creatures of type COW, Oxygen Eater.\n" +
				"%d out of %d hostiles dealt with.";
		String desc3 = "Manage the presence of hostile creatures on your farm.\n" +
				"Deal with hostile creatures by defeating them with weapons, or let your Space Snappers eat them.\n" +
				"Deal with %d creatures of type COW, ASTROLOTL, Oxygen Eater.\n" +
				"%d out of %d hostiles dealt with.";
		for (int i = 0; i < 10; i++) {
			int min7 = Math.min(i, 3);
			String formatted1 = String.format(desc1, 10, i, 10);
			String formatted2 = String.format(desc2, 0, 0, 0);
			String formatted7 = String.format(desc3, 3, min7, 3);
			assertEquals(formatted1, MHQuest1.getDescription());
			assertEquals(formatted2, MHQuest2.getDescription());
			assertEquals(formatted7, MHQuest7.getDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.COMBAT_ACTOR_DEFEATED.name(), EntityType.OXYGEN_EATER);
		}
	}

	@Test
	void testGetShortDescription() {
		String desc = "%d out of %d hostiles dealt with";
		for (int i = 0; i < 10; i++) {
			int min7 = Math.min(i, 3);
			String formatted1 = String.format(desc, i, 10);
			String formatted2 = String.format(desc, 0, 0);
			String formatted7 = String.format(desc, min7, 3);
			assertEquals(formatted1, MHQuest1.getShortDescription());
			assertEquals(formatted2, MHQuest2.getShortDescription());
			assertEquals(formatted7, MHQuest7.getShortDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.COMBAT_ACTOR_DEFEATED.name(), EntityType.OXYGEN_EATER);
		}
		MHQuest1.resetState();
		MHQuest2.resetState();
		MHQuest3.resetState();
		MHQuest4.resetState();
		MHQuest5.resetState();
		MHQuest6.resetState();
		MHQuest7.resetState();
		for (int i = 0; i < 10; i++) {
			int min7 = Math.min(i, 3);
			String formatted1 = String.format(desc, 0, 10);
			String formatted2 = String.format(desc, 0, 0);
			String formatted7 = String.format(desc, min7, 3);
			assertEquals(formatted1, MHQuest1.getShortDescription());
			assertEquals(formatted2, MHQuest2.getShortDescription());
			assertEquals(formatted7, MHQuest7.getShortDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.COMBAT_ACTOR_DEFEATED.name(), EntityType.COW);
		}
		MHQuest1.resetState();
		MHQuest2.resetState();
		MHQuest3.resetState();
		MHQuest4.resetState();
		MHQuest5.resetState();
		MHQuest6.resetState();
		MHQuest7.resetState();
		for (int i = 0; i < 10; i++) {
			int min7 = Math.min(i, 3);
			String formatted1 = String.format(desc, 0, 10);
			String formatted2 = String.format(desc, 0, 0);
			String formatted7 = String.format(desc, min7, 3);
			assertEquals(formatted1, MHQuest1.getShortDescription());
			assertEquals(formatted2, MHQuest2.getShortDescription());
			assertEquals(formatted7, MHQuest7.getShortDescription());
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.COMBAT_ACTOR_DEFEATED.name(), EntityType.ASTROLOTL);
		}
	}

	@Test
	void testReadProgress() {
		int progressInt = 3;
		JsonValue progress = new JsonValue(progressInt);
		String desc1 = "Manage the presence of hostile creatures on your farm.\n" +
				"Deal with hostile creatures by defeating them with weapons, or let your Space Snappers eat them.\n" +
				"Deal with %d creatures of type Oxygen Eater.\n" +
				"%d out of %d hostiles dealt with.";
		String desc2 = "Manage the presence of hostile creatures on your farm.\n" +
				"Deal with hostile creatures by defeating them with weapons, or let your Space Snappers eat them.\n" +
				"Deal with %d creatures of type COW, Oxygen Eater.\n" +
				"%d out of %d hostiles dealt with.";
		String desc3 = "Manage the presence of hostile creatures on your farm.\n" +
				"Deal with hostile creatures by defeating them with weapons, or let your Space Snappers eat them.\n" +
				"Deal with %d creatures of type COW, ASTROLOTL, Oxygen Eater.\n" +
				"%d out of %d hostiles dealt with.";
		String shortDesc = "%d out of %d hostiles dealt with";
		MHQuest1.readProgress(progress);
		MHQuest2.readProgress(progress);
		MHQuest3.readProgress(progress);
		MHQuest4.readProgress(progress);
		MHQuest5.readProgress(progress);
		MHQuest6.readProgress(progress);
		MHQuest7.readProgress(progress);
		String formatted1 = String.format(desc1, 10, progressInt, 10);
		String formatted2 = String.format(desc2, 0, progressInt, 0);
		String formatted7 = String.format(desc3, 3, progressInt, 3);
		String shortFormatted1 = String.format(shortDesc, progressInt, 10);
		String shortFormatted2 = String.format(shortDesc, progressInt, 0);
		String shortFormatted7 = String.format(shortDesc, progressInt, 3);
		assertEquals(formatted1, MHQuest1.getDescription());
		assertEquals(formatted2, MHQuest2.getDescription());
		assertEquals(formatted7, MHQuest7.getDescription());
		assertEquals(shortFormatted1, MHQuest1.getShortDescription());
		assertEquals(shortFormatted2, MHQuest2.getShortDescription());
		assertEquals(shortFormatted7, MHQuest7.getShortDescription());
	}

	@Test
	void testGetProgress() {
		assertEquals(0, MHQuest1.getProgress());
		assertEquals(0, MHQuest2.getProgress());
		assertEquals(0, MHQuest3.getProgress());
		assertEquals(0, MHQuest4.getProgress());
		assertEquals(0, MHQuest5.getProgress());
		assertEquals(0, MHQuest6.getProgress());
		assertEquals(0, MHQuest7.getProgress());
		for (int i = 0; i < 10; i++) {
			ServiceLocator.getMissionManager().getEvents().trigger(
					MissionManager.MissionEvent.COMBAT_ACTOR_DEFEATED.name(), EntityType.OXYGEN_EATER);
		}
		assertEquals(10, MHQuest1.getProgress());
		assertEquals(0, MHQuest2.getProgress());
		assertEquals(10, MHQuest3.getProgress());
		assertEquals(10, MHQuest4.getProgress());
		assertEquals(0, MHQuest5.getProgress());
		assertEquals(10, MHQuest6.getProgress());
		assertEquals(3, MHQuest7.getProgress());
	}
}