package com.csse3200.game.missions.rewards;

import com.csse3200.game.missions.cutscenes.Cutscene;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DialogueRewardTest {
	DialogueReward dialogueReward, dialogueReward2;

	@Test
	void testCollect() {
		Cutscene cutscene = mock(Cutscene.class);
		dialogueReward = new DialogueReward(cutscene);

		assertFalse(dialogueReward.isCollected());
		dialogueReward.collect();
		assertTrue(dialogueReward.isCollected());
		verify(cutscene).spawnCutscene();
	}

	@Test
	void testGetCutscene() {
		Cutscene cutscene = mock(Cutscene.class);
		dialogueReward2 = new DialogueReward(cutscene);

		assertEquals(cutscene, dialogueReward2.getCutscene());
	}
}