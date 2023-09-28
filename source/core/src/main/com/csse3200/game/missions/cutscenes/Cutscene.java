package com.csse3200.game.missions.cutscenes;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.screens.MainGameScreen.*;
import com.csse3200.game.services.ServiceLocator;
import net.dermetfan.gdx.CutsceneManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cutscene {
    private static final Logger logger = LoggerFactory.getLogger(Cutscene.class);

    // creates an instance of the cutscene class and assigns all variables as they need to be assigned - DOES NOT SPAWN THE ACTUAL CUTSCENE
    public Cutscene() {
        //
    }

    // Creates the whole cutscene - to call other methods below this method
    public void spawnCutscene() {
        System.out.println("CUTSCENE SPAWNED");
    }

    // pauses the game
    public void pauseGame() {

    }

    // Spawns the sprites/entities that will be on the left/right side of the screen
    public void spawnSprites() {

    }

    // Spawn the dialogue box and populate it with text
    public void spawnDialogueBox () {

    }

    // animate the sprites/entities on the left/right sides of the screen
    public void animateSprites() {

    }

    // Method to handle when the exit button is pressed. Will likely need some kind of event handler
    public void handleExitButtonPress() {

    }

    // Ends the cutscene, to call the other methods below
    public void endCutscene() {

    }

    // deletes/clears the sprites/entities spawned in
    public void clearSprites() {

    }

    // deletes/clears the dialogue box
    public void clearDialogueBox() {

    }

    // deletes/clears the exit button
    public void clearExitButton() {

    }

    // unpauses the game
    public void unPauseGame() {
        logger.debug("Setting paused state to: 1");
        // 1 is for delta time to run in normal speed
        ServiceLocator.getTimeSource().setTimeScale(1);
    }
}
