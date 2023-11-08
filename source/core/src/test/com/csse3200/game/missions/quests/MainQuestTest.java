package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
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

class MainQuestTest {
	private MainQuest mainQuest1, mainQuest2, mainQuest3;
	private Reward r1, r2, r3;
	String requirement1 = "Requirement Quest 1";
	String requirement2 = "Requirement Quest 2";
	String requirement3 = "Requirement Quest 3";

	@BeforeEach
	public void init() {
		ServiceLocator.registerTimeSource(new GameTime());
		ServiceLocator.registerTimeService(new TimeService());
		ServiceLocator.registerMissionManager(new MissionManager());

		Set<String> requiredQuests1 = new HashSet<>();
		Set<String> requiredQuests2 = new HashSet<>();
		Set<String> requiredQuests3 = new HashSet<>();

		requiredQuests1.add(requirement1);

		requiredQuests2.add(requirement1);
		requiredQuests2.add(requirement2);

		requiredQuests3.add(requirement1);
		requiredQuests3.add(requirement2);
		requiredQuests3.add(requirement3);

		r1 = mock(Reward.class);
		r2 = mock(Reward.class);
		r3 = mock(Reward.class);

		mainQuest1 = new MainQuest("Main Quest 1", r1, 10, requiredQuests1, "Test 1");
		mainQuest2 = new MainQuest("Main Quest 2", r2, 10, requiredQuests2, "Test 2");
		mainQuest3 = new MainQuest("Main Quest 3", r3, 10, requiredQuests3, "Test 3");

		mainQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
		mainQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
		mainQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
	}

	@AfterEach
	public void reset() {
		ServiceLocator.clear();
	}

	@Test
	void testRegisterMission() {
		ServiceLocator.clear();
		assertFalse(mainQuest1.isCompleted());
		assertFalse(mainQuest2.isCompleted());
		assertFalse(mainQuest3.isCompleted());
		mainQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
		mainQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
		mainQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
		assertFalse(mainQuest1.isCompleted());
		assertFalse(mainQuest2.isCompleted());
		assertFalse(mainQuest3.isCompleted());
	}

	@Test
	void testIsCompleted() {
		assertFalse(mainQuest1.isCompleted());
		assertFalse(mainQuest2.isCompleted());
		assertFalse(mainQuest3.isCompleted());
		ServiceLocator.getMissionManager().getEvents().trigger(
				MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement1);
		assertTrue(mainQuest1.isCompleted());
		assertFalse(mainQuest2.isCompleted());
		assertFalse(mainQuest3.isCompleted());
		ServiceLocator.getMissionManager().getEvents().trigger(
				MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement2);
		assertTrue(mainQuest1.isCompleted());
		assertTrue(mainQuest2.isCompleted());
		assertFalse(mainQuest3.isCompleted());
		ServiceLocator.getMissionManager().getEvents().trigger(
				MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement3);
		assertTrue(mainQuest1.isCompleted());
		assertTrue(mainQuest2.isCompleted());
		assertTrue(mainQuest3.isCompleted());
	}

	@Test
	void testGetDescription() {
		String desc1 = "You must Test 1!\nComplete the quests: " + requirement1 + ".";
		String desc2 = "You must Test 2!\nComplete the quests: " + requirement1 + ", " + requirement2 + ".";
		String desc3 = "You must Test 3!\nComplete the quests: " + requirement1 + ", " + requirement2 + ", " +
				requirement3 + ".";
		String desc4 = "You must Test 1!\nComplete the quests: .";
		String desc5 = "You must Test 2!\nComplete the quests: " + requirement2 + ".";
		String desc6 = "You must Test 3!\nComplete the quests: " + requirement2 + ", " +
				requirement3 + ".";
		assertEquals(desc1, mainQuest1.getDescription());
		assertEquals(desc2, mainQuest2.getDescription());
		assertEquals(desc3, mainQuest3.getDescription());
		ServiceLocator.getMissionManager().getEvents().trigger(
				MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement1);
		assertEquals(desc4, mainQuest1.getDescription());
		assertEquals(desc5, mainQuest2.getDescription());
		assertEquals(desc6, mainQuest3.getDescription());
	}

	@Test
	void testGetShortDescription() {
		String desc = "%d required quests to be completed";
		assertEquals(String.format(desc, 1), mainQuest1.getShortDescription());
		assertEquals(String.format(desc, 2), mainQuest2.getShortDescription());
		assertEquals(String.format(desc, 3), mainQuest3.getShortDescription());
		ServiceLocator.getMissionManager().getEvents().trigger(
				MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement1);
		assertEquals(String.format(desc, 0), mainQuest1.getShortDescription());
		assertEquals(String.format(desc, 1), mainQuest2.getShortDescription());
		assertEquals(String.format(desc, 2), mainQuest3.getShortDescription());
		ServiceLocator.getMissionManager().getEvents().trigger(
				MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement2);
		assertEquals(String.format(desc, 0), mainQuest1.getShortDescription());
		assertEquals(String.format(desc, 0), mainQuest2.getShortDescription());
		assertEquals(String.format(desc, 1), mainQuest3.getShortDescription());
		ServiceLocator.getMissionManager().getEvents().trigger(
				MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement3);
		assertEquals(String.format(desc, 0), mainQuest1.getShortDescription());
		assertEquals(String.format(desc, 0), mainQuest2.getShortDescription());
		assertEquals(String.format(desc, 0), mainQuest3.getShortDescription());
	}

	@Test
	void testReadProgress() {
		Json json = new Json();
		JsonReader reader = new JsonReader();
		String[] progressArray1 = {requirement1};
		String[] progressArray2 = {requirement1, requirement2};
		String[] progressArray3 = {requirement1, requirement2, requirement3};
		JsonValue progress1 = reader.parse(json.toJson(progressArray1));
		JsonValue progress2 = reader.parse(json.toJson(progressArray2));
		JsonValue progress3 = reader.parse(json.toJson(progressArray3));
		mainQuest1.readProgress(progress1);
		mainQuest2.readProgress(progress1);
		mainQuest3.readProgress(progress1);
		assertTrue(mainQuest1.isCompleted());
		assertFalse(mainQuest2.isCompleted());
		assertFalse(mainQuest3.isCompleted());
		mainQuest1.readProgress(progress2);
		mainQuest2.readProgress(progress2);
		mainQuest3.readProgress(progress2);
		assertTrue(mainQuest1.isCompleted());
		assertTrue(mainQuest2.isCompleted());
		assertFalse(mainQuest3.isCompleted());
		mainQuest1.readProgress(progress3);
		mainQuest2.readProgress(progress3);
		mainQuest3.readProgress(progress3);
		assertTrue(mainQuest1.isCompleted());
		assertTrue(mainQuest2.isCompleted());
		assertTrue(mainQuest3.isCompleted());
	}

	@Test
	void testGetProgress() {
		String[] progressArray1 = {requirement1};
		String[] progressArray2 = {requirement1, requirement2};
		String[] progressArray3 = {requirement1, requirement2, requirement3};
		String[] progressArray0 = {};
		assertArrayEquals(progressArray0, (Object[]) mainQuest1.getProgress());
		ServiceLocator.getMissionManager().getEvents().trigger(
				MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement1);
		assertArrayEquals(progressArray1, (Object[]) mainQuest1.getProgress());
		assertArrayEquals(progressArray1, (Object[]) mainQuest2.getProgress());
		assertArrayEquals(progressArray1, (Object[]) mainQuest3.getProgress());
		ServiceLocator.getMissionManager().getEvents().trigger(
				MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement2);
		assertArrayEquals(progressArray1, (Object[]) mainQuest1.getProgress());
		assertArrayEquals(progressArray2, (Object[]) mainQuest2.getProgress());
		assertArrayEquals(progressArray2, (Object[]) mainQuest3.getProgress());
		ServiceLocator.getMissionManager().getEvents().trigger(
				MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement3);
		assertArrayEquals(progressArray1, (Object[]) mainQuest1.getProgress());
		assertArrayEquals(progressArray2, (Object[]) mainQuest2.getProgress());
		assertArrayEquals(progressArray3, (Object[]) mainQuest3.getProgress());
		assertTrue(mainQuest1.isCompleted());
		assertTrue(mainQuest2.isCompleted());
		assertTrue(mainQuest3.isCompleted());
	}

	@Test
	void testResetState() {
		ServiceLocator.getMissionManager().getEvents().trigger(
				MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement1);
		ServiceLocator.getMissionManager().getEvents().trigger(
				MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement2);
		ServiceLocator.getMissionManager().getEvents().trigger(
				MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement3);
		assertTrue(mainQuest1.isCompleted());
		assertTrue(mainQuest2.isCompleted());
		assertTrue(mainQuest3.isCompleted());
		mainQuest1.resetState();
		mainQuest2.resetState();
		mainQuest3.resetState();
		assertFalse(mainQuest1.isCompleted());
		assertFalse(mainQuest2.isCompleted());
		assertFalse(mainQuest3.isCompleted());
	}
}