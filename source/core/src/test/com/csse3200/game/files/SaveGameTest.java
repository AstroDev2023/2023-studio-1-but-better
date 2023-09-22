package com.csse3200.game.files;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.files.SaveGame.GameState;
public class SaveGameTest {

  @Test
  public void SetDay() {
    GameState gameState = new SaveGame.GameState();

    gameState.setDay(5);
    assertEquals(5, gameState.getDay());

    gameState.setDay(-1);
    assertEquals(-1, gameState.getDay());

    gameState.setDay(2147483647);
    assertEquals(2147483647, gameState.getDay());
  }

  @Test
  public void setHour() {
    GameState gameState = new SaveGame.GameState();

    gameState.setHour(17);
    assertEquals(17, gameState.getHour());

    gameState.setHour(-1);
    assertEquals(-1, gameState.getHour());

    gameState.setHour(2147483647);
    assertEquals(2147483647, gameState.getHour());
  }

  @Test
  public void setMinute() {
    GameState gameState = new SaveGame.GameState();

    gameState.setHour(17);
    assertEquals(17, gameState.getHour());

    gameState.setHour(-1);
    assertEquals(-1, gameState.getHour());

    gameState.setHour(2147483647);
    assertEquals(2147483647, gameState.getHour());
  }

  @Test
  public void setPlayer() {
    GameState gameState = new SaveGame.GameState();

    Entity playerTest = new Entity(EntityType.Player);

    gameState.setPlayer(playerTest);
    assertEquals(playerTest, gameState.getPlayer());
  }

  @Test
  public void setEntities() {
    GameState gameState = new SaveGame.GameState();

    Entity[] entities = { new Entity(EntityType.Astrolotl),
        new Entity(EntityType.Chicken),
        new Entity(EntityType.Cow)};

    Array<Entity> entityTest = new Array<Entity>(entities);

    gameState.setEntities(entityTest);
    assertEquals(entityTest, gameState.getEntities());
  }

  @Test
  public void setTiles() {
    GameState gameState = new SaveGame.GameState();

    Entity[] tiles = { new Entity(EntityType.Tile),
        new Entity(EntityType.Tile),
        new Entity(EntityType.Tile),
        new Entity(EntityType.Tile), };

    Array<Entity> tileTest = new Array<Entity>(tiles);

    gameState.setTiles(tileTest);
    assertEquals(tileTest, gameState.getTiles());
  }

  @Test
  public void testFilterTiles() {
    Array<Entity> entities = new Array<Entity>();
    Entity tile = new Entity(EntityType.Tile);
    entities.add(new Entity(EntityType.Plant), new Entity(), tile, new Entity(EntityType.Chicken));
    GameState state = new GameState();
    state.setTiles(entities);
    assertEquals(1,state.getTiles().size);
    assertEquals(tile,state.getTiles().get(0));
  }

  @Test
  public void testFilterEntities() {
    Array<Entity> entities = new Array<Entity>();
    Entity tile = new Entity(EntityType.Tile);
    Entity chicken = new Entity(EntityType.Chicken);
    entities.add(new Entity(), tile, chicken);
    GameState state = new GameState();
    state.setEntities(entities);
    assertEquals(1,state.getEntities().size);
    assertEquals(chicken,state.getEntities().get(0));
  }

  @Test
  public void getTractor() {
    GameState gameState = new SaveGame.GameState();
    Entity tractor = new Entity(EntityType.Tractor);
    gameState.setTractor(tractor);
    assertEquals(tractor, gameState.getTractor());
    assertEquals(EntityType.Tractor, gameState.getTractor().getType());
  }

  @Test
  public void setTractor() {
    GameState gameState = new SaveGame.GameState();
    Entity tractor = new Entity(EntityType.Tractor);
    Vector2 position = new Vector2(1, 1);
    tractor.setPosition(position);
    gameState.setTractor(tractor);
    assertEquals(tractor, gameState.getTractor());
    assertEquals(position, gameState.getTractor().getPosition());
  }

  @Test
  public void getClimate() {
    GameState state = new GameState();
    ClimateController mockClimate = mock(ClimateController.class);
    when(mockClimate.getHumidity()).thenReturn(5f);
    when(mockClimate.getTemperature()).thenReturn(5f);
    when(mockClimate.getCurrentWeatherEvent()).thenReturn(null);
    mockClimate.setHumidity(5f);
    mockClimate.setTemperature(5f);
    mockClimate.setCurrentWeatherEvent(null);
    state.setClimate(mockClimate);
    assertEquals(mockClimate.getHumidity(),state.getClimate().getHumidity(), 0.1f);
    assertEquals(mockClimate.getTemperature(),state.getClimate().getTemperature(), 0.1f);
    assertEquals(mockClimate.getCurrentWeatherEvent(),state.getClimate().getCurrentWeatherEvent());
  }
}
