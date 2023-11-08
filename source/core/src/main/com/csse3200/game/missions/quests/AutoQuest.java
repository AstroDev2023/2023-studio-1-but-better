package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.Mission;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.ServiceLocator;

/**
 * AutoQuests are {@link Quest}s whose {@link Reward} is collected automatically, after a certain number of in-game
 * minutes.
 */
public class AutoQuest extends Quest {

	/**
	 * Description of the Quest
	 */
	private final String description;

	/**
	 * The delay, in in-game minutes, before the {@link AutoQuest}'s {@link Reward} should be collected
	 */
	private final int collectDelay;

	/**
	 * The number of minutes before the {@link Reward} is collected
	 */
	private int minutesToReward;

	/**
	 * Creates an {@link AutoQuest}, whose {@link Reward} will be collected in 1 minute.
	 *
	 * @param name        - name of the AutoQuest
	 * @param reward      - the {@link Reward} to be collected
	 * @param description - description of the AutoQuest
	 */
	public AutoQuest(String name, Reward reward, String description) {
		this(name, reward, description, 1);
	}

	/**
	 * Creates an {@link AutoQuest}
	 *
	 * @param name         - name of the AutoQuest
	 * @param reward       - the {@link Reward} to be collected
	 * @param description  - description of the AutoQuest
	 * @param collectDelay - the number of minutes before this {@link AutoQuest}'s {@link Reward} is to be collected
	 */
	public AutoQuest(String name, Reward reward, String description, int collectDelay) {
		super(name, reward);
		this.description = description;

		this.collectDelay = collectDelay;
		this.minutesToReward = this.collectDelay;
	}

	/**
	 * On registering this mission the {@link Reward} is collected.
	 *
	 * @param missionManagerEvents A reference to the {@link EventHandler} on the
	 *                             {@link MissionManager}, with which relevant events should be
	 *                             listened to.
	 */
	@Override
	public void registerMission(EventHandler missionManagerEvents) {
		ServiceLocator.getTimeService().getEvents().addListener("minuteUpdate", this::updateState);
	}

	private void updateState() {
		if (--minutesToReward <= 0) {
			minutesToReward = 0;
			collectReward();
		}
	}

	/**
	 * Always returns true as there is no dynamic progress.
	 *
	 * @return - true
	 */
	@Override
	public boolean isCompleted() {
		return minutesToReward <= 0;
	}

	/**
	 * Returns the description specified in the constructor.
	 *
	 * @return - description of the {@link AutoQuest}
	 */
	@Override
	public String getDescription() {
		return getShortDescription();
	}

	/**
	 * Returns the description as in {@link #getDescription()} method.
	 *
	 * @return - description of the {@link AutoQuest}
	 */
	@Override
	public String getShortDescription() {
		return description;
	}

	/**
	 * Does nothing as {@link AutoQuest}s have no savable progress
	 *
	 * @param progress The {@link JsonValue} representing the progress of the {@link Mission} as determined by the value
	 *                 returned in {@link #getProgress()}.
	 */
	@Override
	public void readProgress(JsonValue progress) {
		minutesToReward = progress.asInt();
	}

	/**
	 * Gets the {@link AutoQuest}'s progress.
	 *
	 * @return - always 0 as there is no progress in an {@link AutoQuest}
	 */
	@Override
	public Object getProgress() {
		return minutesToReward;
	}

	/**
	 * Does nothing as {@link AutoQuest}s have no state to reset.
	 */
	@Override
	protected void resetState() {
		minutesToReward = collectDelay;
	}

}
