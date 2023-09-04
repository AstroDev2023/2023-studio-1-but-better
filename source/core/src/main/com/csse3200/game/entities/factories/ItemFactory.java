package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.items.ItemActions;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.items.WateringCanLevelComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * Factory to create an item
 */
public class ItemFactory {
  public static Entity createBaseItem() {
    Entity item = new Entity(EntityType.Item)
        .addComponent(new PhysicsComponent())
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
        .addComponent(new ItemActions());
    return item;
  }

  /**
   * Creates a shovel item
   * 
   * @return shovel
   */
  public static Entity createShovel() {
    Entity shovel = createBaseItem()
        .addComponent(new TextureRenderComponent("images/tool_shovel.png"))
        .addComponent(new ItemComponent("shovel", ItemType.SHOVEL, "Shovel for removing items",
            new Texture("images/tool_shovel.png")));
    return shovel;
  }

  /**
   * Creates a hoe item
   * 
   * @return hoe
   */
  public static Entity createHoe() {
    Entity hoe = createBaseItem()
        .addComponent(new TextureRenderComponent("images/tool_hoe.png"))
        .addComponent(new ItemComponent("hoe", ItemType.HOE, new Texture("images/tool_hoe.png")));
    return hoe;
  }

  /**
   * Creates a watering-can item
   * 
   * @return watering can
   */
  public static Entity createWateringcan() {
    Entity watering_can = createBaseItem()
        .addComponent(new TextureRenderComponent("images/tool_watering_can.png"))
        .addComponent(
            new ItemComponent("watering can", ItemType.WATERING_CAN, new Texture("images/tool_watering_can.png")))
        .addComponent(new WateringCanLevelComponent(150));
    return watering_can;
  }

  /**
   * Creates a scythe item
   * 
   * @return scythe
   */
  public static Entity createScythe() {
    Entity scythe = createBaseItem()
        .addComponent(new TextureRenderComponent("images/tool_scythe.png"))
        .addComponent(new ItemComponent("watering can", ItemType.SCYTHE,
                new Texture("images/tool_scythe.png")));
    return scythe;
  }

  public static Entity createFertiliser() {
    Entity fertiliser = createBaseItem()
            .addComponent(new TextureRenderComponent("images/fertiliser.png"))
            .addComponent(new ItemComponent("fertiliser", ItemType.FERTILISER,
                    new Texture("images/fertiliser.png")));
    return fertiliser;
  }

  public static Entity createAloeVeraSeed() {
    Entity seed = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/aloe_vera_seed.png"))
            .addComponent(new ItemComponent("aloe vera seed", ItemType.SEED,
                    new Texture("images/images/plants/aloe_vera_seed.png")));
    return seed;
  }

  public static Entity createAtomicAlgaeSeed() {
    Entity seed = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/atomic_algae_seed.png"))
            .addComponent(new ItemComponent("atomic algae seed", ItemType.SEED,
                    new Texture("images/images/plants/atomic_algae_seed.png")));
    return seed;
  }

  public static Entity createCosmicCobSeed() {
    Entity seed = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/cosmic_cob_seed.png"))
            .addComponent(new ItemComponent("cosmic cob seed", ItemType.SEED,
                    new Texture("images/images/plants/cosmic_cob_seed.png")));
    return seed;
  }

  public static Entity createDeadlyNightshadeSeed() {
    Entity seed = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/deadly_nightshade_seed.png"))
            .addComponent(new ItemComponent("deadly nightshade seed", ItemType.SEED,
                    new Texture("images/images/plants/deadly_nightshade_seed.png")));
    return seed;
  }

  public static Entity createHammerPlantSeed() {
    Entity seed = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/hammer_plant_seed.png"))
            .addComponent(new ItemComponent("hammer plant seed", ItemType.SEED,
                    new Texture("images/images/plants/hammer_plant_seed.png")));
    return seed;
  }

  public static Entity createHorticulturalHeaterSeed() {
    Entity seed = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/horticultural_heater_seed.png"))
            .addComponent(new ItemComponent("horticultural heater seed", ItemType.SEED,
                    new Texture("images/images/plants/horticultural_heater_seed.png")));
    return seed;
  }

  public static Entity createSpaceSnapperSeed() {
    Entity seed = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/space_snapper_seed.png"))
            .addComponent(new ItemComponent("space snapper seed", ItemType.SEED,
                    new Texture("images/images/plants/space_snapper_seed.png")));
    return seed;
  }

  public static Entity createTobaccoSeed() {
    Entity seed = createBaseItem()
            .addComponent(new TextureRenderComponent("images/plants/tobacco_seed.png"))
            .addComponent(new ItemComponent("tobacco seed", ItemType.SEED,
                    new Texture("images/images/plants/tobacco_seed.png")));
    return seed;
  }
}
