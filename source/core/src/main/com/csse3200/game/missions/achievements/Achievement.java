package com.csse3200.game.missions.achievements;

import com.badlogic.gdx.utils.Json;
import com.csse3200.game.missions.Mission;

/**
 * Adding test achievement to fix broken build
 */
public abstract class Achievement extends Mission {

	/**
	 * Creates an {@link Achievement} with the given {@link String} name.
	 *
	 * @param name The {@link String} name of the {@link Achievement}, visible to the player
	 *             in-game.
	 */
	protected Achievement(String name) {
		super(name);
	}

	/**
	 * Used to write the {@link Achievement} for saving.
	 *
	 * @param json - Json object to write to
	 * @param i    - the index
	 */
	public void write(Json json, Integer i) {
		json.writeObjectStart("Achievement");
		json.writeValue("index", i);
		json.writeValue("progress", getProgress());
		json.writeObjectEnd();
	}
}
