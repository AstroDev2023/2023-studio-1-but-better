package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the current oxygen level on the Main Game Screen.
 */
public class OxygenDisplay extends UIComponent{
    
    private static final Logger logger = LoggerFactory.getLogger(OxygenDisplay.class);
    Table table = new Table();
    Group group = new Group();
    private Image oxygenOutline;
    private Image oxygenFill;
    private Image oxygenHealthy;
    private Image oxygenDanger;
    private Array<Label> oxygenLabels;
    private Label oxygenLabel;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
    
        logger.debug("Adding listener to oxygenUpdate event");
        // Adds a listener to check for oxygen updates
        ServiceLocator.getPlanetOxygenService().getEvents()
                .addListener("oxygenUpdate", this::updateDisplay);

        updateDisplay();
    }

    /**
     * Initialises all the possible images and labels that will be used by
     * the class, and stores them in an array to be called when needed.
     */
    public void createTexture() {
        logger.debug("Oxygen display texture being created");
        Skin oxygenSkin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        oxygenOutline = new Image(ServiceLocator.getResourceService().getAsset(
            "images/bars_ui/bar_outline.png", Texture.class));
        oxygenHealthy = new Image(ServiceLocator.getResourceService().getAsset(
            "images/bars_ui/healthy_fill.png", Texture.class));
        oxygenDanger = new Image(ServiceLocator.getResourceService().getAsset(
            "images/bars_ui/danger_fill.png", Texture.class));

        oxygenLabels = new Array<>();
        for (int i = 0; i <= 100; i++) {
            oxygenLabels.add(new Label(String.format("Oxygen: %d%%", i), oxygenSkin));
        }
    }

    /**
     * Updates the display, showing the oxygen bar in the top of the main game screen.
     */
    public void updateDisplay() {
        if (oxygenLabels == null) {
            createTexture();
        }

        int oxygenPercent = ServiceLocator.getPlanetOxygenService().getOxygenPercentage();
        float scaling = (float) oxygenPercent / 100;

        // Accounts for scaling of the oxygen bar due to the oxygen percent
        if (oxygenPercent <= 25) {
            oxygenFill = oxygenDanger;
            oxygenFill.setX(oxygenFill.getImageX() + 14 * (1 - scaling));
            oxygenFill.setScaleX(scaling);
        } else {
            oxygenFill = oxygenHealthy;
            oxygenFill.setX(oxygenFill.getImageX() + 14 * (1 - scaling));
            oxygenFill.setScaleX(scaling);
        }


        // Add a safety check to ensure that the array is always accessed at a possible index
        if (0 <= oxygenPercent && oxygenPercent <= 100) {
            logger.debug("Oxygen display updated");
            oxygenLabel = oxygenLabels.get(oxygenPercent);
            oxygenLabel.setPosition(oxygenOutline.getImageX() + 125f, oxygenOutline.getImageY() + 8.5f);
        }

        // Uncomment line below to test that oxygen percent decreases by 1% per hour (till endgame condition reached).
        ServiceLocator.getPlanetOxygenService().addOxygen(160);
    }

    /**
     * Draws the table and group onto the main game screen. Adds the oxygen elements onto the stage.
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        table.clear();
        group.clear();

        table.top();
        table.setFillParent(true);
        table.padTop(-130f).padLeft(-520f);

        group.addActor(oxygenOutline);
        group.addActor(oxygenFill);
        group.addActor(oxygenLabel);

        table.add(group).size(200);
        stage.addActor(table);
    }

    /**
     * Removes all entities from the screen. Releases all resources from this class.
     */
    @Override
    public void dispose() {
        super.dispose();
        oxygenOutline.remove();
        oxygenFill.remove();
        oxygenLabel.remove();
    }
}
