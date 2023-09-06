package com.csse3200.game.services;

import java.security.Provider.Service;

import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.files.SaveGame;
import com.csse3200.game.files.SaveGame.GameState;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.utils.math.GridPoint2Utils;

import static com.csse3200.game.services.ServiceLocator.getEntityService;


/* A note of the registering of this service:
 *  this service is currently only registered at MainMenuScreen,
 *  (source/core/src/main/com/csse3200/game/screens/MainMenuScreen.java)
 *  for the moment I am unsure if this is sufficient.
 *  Some other services are registered in multiple places such as MainGameScreen.
 */

/* Current functionality:
 * - prints save/load to system out depending on method call.
 * - Saves:
 *    - player pos.
 *    - player inventory (partially)
 *    - time/day.
 *    - entity (cows and stuff) location & tamed status.
 * - Loads:
 *    - player pos.
 *    - time/day.
 */
public class SaveLoadService {
  /**
   * Saves the current state of the game into a GameState
   */
  public void save() {
    // Make a new GameState
    SaveGame.GameState state = new GameState();

    state.setDay(ServiceLocator.getTimeService().getDay());
    state.setHour(ServiceLocator.getTimeService().getHour());

    state.setPlayer(ServiceLocator.getGameArea().getPlayer());
    state.setEntities(ServiceLocator.getEntityService().getEntities());
    // TODO: Broken map saving :(
    //state.setMap(ServiceLocator.getGameArea().getMap());

    // Write the state to a file
    SaveGame.set(state);

    System.out.println("save!");
  }



  /**
   * Load function which based on conents in saveFile.json
   * Makes the game state match saveFile.json 
   */
  public void load() {
    SaveGame.GameState state = SaveGame.get();
    if (state == null) {
      // TODO: Dialogue box or something?
      System.out.println("Couldn't read saveFile");
      return;
    }

    updateGame(state);
    System.out.println("load!");
  }

  /**
   * Update NPCs, player and time of the game based off the gamestate that was saved
   * @param state state of the game which was saved previously in saveFile.json
   */
  private void updateGame(GameState state) {
    try {
      updateNPCs(state);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    updatePlayer(state);
    updateTime(state);
  }

  /**
   * Updates the player entity position based off the saved gamestate
   * @param state gamestate of the entire game based off safeFile.json
   */
  private void updatePlayer(GameState state) {
    Entity currentPlayer = ServiceLocator.getGameArea().getPlayer();
    currentPlayer.setPosition(state.getPlayer().getPosition());
    System.out.println(currentPlayer.getPosition());
    System.out.println(state.getPlayer().getPosition());
  }

  /**
   * Destroys all NPCS in the map and then recreates them based off the gamestate
   * @param state gamestate of the entire game based off safeFile.json
   */
  private void updateNPCs(GameState state) throws ClassNotFoundException {
    //Entity temp = new Entity();
    Entity target = PlayerFactory.createPlayer();
    EntityService entityService = getEntityService();
    //entityService.disposeNPCs(); TODO: dispose doesn't work
    for (Entity entity : state.getEntities()) {
      //if (entity.getType() == EntityType.Chicken) {
      Entity e = NPCFactory.createNPC(entity.getType(), target);
      ServiceLocator.getGameArea().spawnEntity(e);
      ServiceLocator.getEntityService().getEntities().peek().setPosition(entity.getPosition());
      //}
    }
    /*
    Entity player = ServiceLocator.getGameArea().getPlayer();
    EntityService entityService = ServiceLocator.getEntityService();

    entityService.disposeNPCs();

    for (Entity entity : state.getEntities()) {
      if (entity.getType() == EntityType.Cow) {
        
        Entity cow = NPCFactory.createCow(player);
        cow.setPosition(entity.getPosition());
        GridPoint2 position = new GridPoint2((int)cow.getPosition().x, (int)cow.getPosition().y);
        ServiceLocator.getGameArea().spawnEntityAt(entity, position, true, true);
        //TODo fix this
        
      }
    }
     */
  }



  /**
   * Updates the time of the game based off the saved values in the gamestate
   * @param state the state of the saved game
   */
  private void updateTime(GameState state) {
    ServiceLocator.getTimeService().setDay(state.getDay());
    ServiceLocator.getTimeService().setHour(state.getHour());
  }
}
