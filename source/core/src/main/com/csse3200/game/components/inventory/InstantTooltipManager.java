package com.csse3200.game.components.inventory;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;

import static com.badlogic.gdx.math.Interpolation.fade;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/* Credit: LibGDX TooltipManager
 * */
public class InstantTooltipManager extends com.badlogic.gdx.scenes.scene2d.ui.TooltipManager {
	public boolean animations = true;
	float time = initialTime;
	public Stage stage;

	@Override
	protected void showAction(Tooltip tooltip) {
		float actionTime = animations ? (time > 0 ? 0.5f : 0.15f) : 0.1f;
		tooltip.getContainer().addAction(parallel(fadeIn(actionTime, fade), scaleTo(1, 1, actionTime, Interpolation.fade)));

	}

	@Override
	protected void hideAction(Tooltip tooltip) {
		tooltip.getContainer()
				.addAction(sequence(parallel(scaleTo(0.05f, 0.05f, 0, Interpolation.fade)), removeActor()));
	}

}
