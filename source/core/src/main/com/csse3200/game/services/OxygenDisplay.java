package com.csse3200.game.services;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A UI component for displaying the current oxygen level on the Main Game Screen.
 */
public class OxygenDisplay extends UIComponent {

	private static final Logger logger = LoggerFactory.getLogger(OxygenDisplay.class);
	Table table = new Table();
	Group group = new Group();
	private Image oxygenFrame;
	private Image oxygenFill;
	private Image oxygenIcon;

	/**
	 * Creates reusable ui styles and adds actors to the stage.
	 */
	@Override
	public void create() {
		super.create();

		createTexture();

		logger.debug("Adding listener to oxygenUpdate event");
		// Adds a listener to check for oxygen updates
		ServiceLocator.getPlanetOxygenService().getEvents()
				.addListener("oxygenUpdate", this::updateDisplay);
		ServiceLocator.getUIService().getEvents()
				.addListener("toggleUI", this::toggleDisplay);

		// Initial update
		updateDisplay();
	}

	private void toggleDisplay(boolean isDisplayed) {
		table.setVisible(isDisplayed);
	}

	/**
	 * Initialises all the possible images and labels that will be used by
	 * the class, and stores them in an array.
	 */
	public void createTexture() {
		logger.debug("Oxygen display texture being created");

		oxygenFrame = new Image(ServiceLocator.getResourceService().getAsset(
				"images/status_ui/status_frame.png", Texture.class));
		oxygenFill = new Image(ServiceLocator.getResourceService().getAsset(
				"images/status_ui/oxygen_fill.png", Texture.class));
		oxygenIcon = new Image(ServiceLocator.getResourceService().getAsset(
				"images/status_ui/oxygen_icon.png", Texture.class));

		// Set oxygenFill to the initial starting one
		oxygenFill.setScaleY(0.1f);
	}

	/**
	 * Updates the display, showing the oxygen bar in the top of the main game screen.
	 */
	public void updateDisplay() {

		int newOxygenPercent = ServiceLocator.getPlanetOxygenService().getOxygenPercentage();
		float scaling = (float) newOxygenPercent / 100;

		oxygenFill.setY(oxygenFill.getImageY() + 48 * (1 - scaling));

		logger.debug("Oxygen display updated");

		oxygenFill.addAction(Actions.scaleTo(1.0f, scaling, 1.0f, Interpolation.pow2InInverse));

		ServiceLocator.getPlanetOxygenService().addOxygen(100);
	}

	/**
	 * Draws the table and group onto the main game screen. Adds the oxygen elements onto the stage.
	 *
	 * @param batch Batch to render to.
	 */
	@Override
	public void draw(SpriteBatch batch) {
		table.clear();
		group.clear();
		table.top();
		table.setFillParent(true);

		table.padTop(-50f).padLeft(110f);

		group.addActor(oxygenFill);
		group.addActor(oxygenFrame);
		group.addActor(oxygenIcon);

		table.add(group).size(200);
		stage.addActor(table);
	}

	/**
	 * Removes all entities from the screen and releases all resources from this class.
	 */
	@Override
	public void dispose() {
		super.dispose();
		oxygenFrame.remove();
		oxygenFill.remove();
		oxygenIcon.remove();
	}
}
