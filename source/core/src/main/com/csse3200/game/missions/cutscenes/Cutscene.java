package com.csse3200.game.missions.cutscenes;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.components.cutscenes.CutsceneDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cutscene{
    private static final Logger logger = LoggerFactory.getLogger(Cutscene.class);

    /**
     * Stores the dialogue text
     */
    private final String dialogue;

    // creates an instance of the cutscene class and assigns all variables as they need to be assigned - DOES NOT SPAWN THE ACTUAL CUTSCENE
    public Cutscene(String dialogue) {
        super();
        this.dialogue = dialogue;
    }

    // Creates the whole cutscene - to call other methods below this method
    public void spawnCutscene() {
        System.out.println("CUTSCENE SPAWNED");
        logger.debug("Cutscene spawned");

        this.pauseGame();
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity cutsceneEntity = new Entity();
        cutsceneEntity.addComponent(new CutsceneDisplay(dialogue));
        cutsceneEntity.addComponent(new InputDecorator(stage, 10));
        ServiceLocator.getEntityService().register(cutsceneEntity);
    }

    // pauses the game
    public void pauseGame() {

    }


    // Ends the cutscene
    public void endCutscene() {
        this.unPauseGame();
    }

    // unpauses the game
    public void unPauseGame() {
        logger.debug("Setting paused state to: 1");
        // 1 is for delta time to run in normal speed
        ServiceLocator.getTimeSource().setTimeScale(1);
    }

}
