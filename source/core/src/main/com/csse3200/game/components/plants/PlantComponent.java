package com.csse3200.game.components.plants;
import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import java.util.Map;
import java.util.function.Supplier;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Class for all plants in the game.
 */
public class PlantComponent extends Component {

    /**
     * Initial plant health
     */
    private int plantHealth;

    /**
     * Maximum health that this plant can reach as an adult
     */
    private final int maxHealth;

    /**
     * User facing plant name
     */
    private final String plantName;

    /**
     * The type of plant (food, health, repair, defence, production, deadly)
     */
    private final String plantType;

    /**
     * User facing description of the plant
     */
    private final String plantDescription;

    /**
     * Ideal water level of the plant. A factor when determining the growth rate.
     */
    private final float idealWaterLevel;

    /**
     * The growth stage of the plant
     */
    public enum GrowthStage {
        SEEDLING(1),
        SPROUT(2),
        JUVENILE(3),
        ADULT(4),
        DECAYING(5),
        DEAD(6);

        private int value;

        GrowthStage(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private GrowthStage growthStages;

    /**
     * How long a plant lives as an adult before starting to decay from old age.
     */
    private int adultLifeSpan;

    /**
     * Used to determine when a plant enters a new growth stage (for growth stages 2, 3, 4)
     */
    private int currentGrowthLevel;

    /**
     * The crop tile on which this plant is planted on.
     */
    private final CropTileComponent cropTile;

    /**
     * The growth thresholds for different growth stages (Sprout Juvenile, Adult).
     */
    private final int[] growthStageThresholds = {0, 0, 0};

    /**
     * The paths to the sounds associated with the plant.
     */
    private String[] sounds;

    /**
     * The current max health. This limits the amount of health a plant can have at different growth stages.
     */
    private double currentMaxHealth = 0;

    /**
     * The maximum health a plant can have at different growth stages (stages 1, 2, 3).
     */
    private double[] maxHealthAtStages = {0, 0, 0};

    /**
     * The paths to the images associated with different growth stages (Seedling, Sprout, Juvenile, Adult, Decaying).
     */
    private String[] growthStageImagePaths = {"", "", "", "", ""};

    /**
     * Used to track how long a plant has been an adult.
     */
    private int numOfDaysAsAdult;

    /**
     * The current texture based on the growth stage.
     */
    private DynamicTextureRenderComponent currentTexture;

    /**
     * The yield to be dropped when this plant is harvested as a map of item names to drop
     * quantities.
     */
    private Map<String, Integer> harvestYields;

    /**
     * Constructor used for plant types that have no extra properties. This is just used for testing.
     *
     * @param health - health of the plant
     * @param name - name of the plant
     * @param plantType - type of the plant
     * @param plantDescription - description of the plant
     * @param idealWaterLevel - The ideal water level for a plant
     * @param adultLifeSpan - How long a plant will live for once it becomes an adult
     * @param maxHealth - The maximum health a plant can reach as an adult
     * @param cropTile - The cropTileComponent where the plant will be located.
     */
    public PlantComponent(int health, String name, String plantType, String plantDescription,
                          float idealWaterLevel, int adultLifeSpan, int maxHealth,
                          CropTileComponent cropTile) {
        this.plantHealth = health;
        this.plantName = name;
        this.plantType = plantType;
        this.plantDescription = plantDescription;
        this.idealWaterLevel = idealWaterLevel;
        this.adultLifeSpan = adultLifeSpan;
        this.maxHealth = maxHealth;
        this.cropTile = cropTile;
        this.currentGrowthLevel = 0;
        this.growthStages = GrowthStage.SEEDLING;
        this.numOfDaysAsAdult = 0;
        this.currentMaxHealth = maxHealth;

        // Initialise default values for growth stage thresholds.
        this.growthStageThresholds[0] = 11;
        this.growthStageThresholds[1] = 21;
        this.growthStageThresholds[2] = 41;

        // Initialise max health values to be changed at each growth stage
        this.maxHealthAtStages[0] = 0.05 * this.maxHealth;
        this.maxHealthAtStages[1] = 0.1 * this.maxHealth;
        this.maxHealthAtStages[2] = 0.3 * this.maxHealth;

        // Initialise the image paths for growth stages
        // ALl pointing to Corn for now.
        this.growthStageImagePaths[0] = "images/plants/seedling.png";
        this.growthStageImagePaths[1] = "images/plants/corn_sprout.png";
        this.growthStageImagePaths[2] = "images/plants/corn_juvenile.png";
        this.growthStageImagePaths[3] = "images/plants/corn_adult.png";
        this.growthStageImagePaths[4] = "images/plants/corn_decaying.png";
    }

    /**
     * Constructor used for plant types that have growthStageThresholds different from the default
     * values.
     *
     * @param health - health of the plant
     * @param name - name of the plant
     * @param plantType - type of the plant
     * @param plantDescription - description of the plant
     * @param idealWaterLevel - The ideal water level for a plant
     * @param adultLifeSpan - How long a plant will live for once it becomes an adult
     * @param maxHealth - The maximum health a plant can reach as an adult
     * @param cropTile - The cropTileComponent where the plant will be located.
     * @param growthStageThresholds - A list of three integers that represent the growth thresholds.
     * @param soundsArray - A list of all sound files filepaths as strings
     * @param growthStageImagePaths - image paths for the different growth stages.
     */
    public PlantComponent(int health, String name, String plantType, String plantDescription,
                          float idealWaterLevel, int adultLifeSpan, int maxHealth,
                          CropTileComponent cropTile, int[] growthStageThresholds,
                          String[] soundsArray, String[] growthStageImagePaths) {
        this.plantHealth = health;
        this.plantName = name;
        this.plantType = plantType;
        this.plantDescription = plantDescription;
        this.idealWaterLevel = idealWaterLevel;
        this.adultLifeSpan = adultLifeSpan;
        this.maxHealth = maxHealth;
        this.cropTile = cropTile;
        this.currentGrowthLevel = 0;
        this.sounds = soundsArray;
        this.growthStages = GrowthStage.SEEDLING;
        this.numOfDaysAsAdult = 0;
        this.currentMaxHealth = maxHealth;


        // Initialise growth stage thresholds for specific plants.
        this.growthStageThresholds[0] = growthStageThresholds[0];
        this.growthStageThresholds[1] = growthStageThresholds[1];
        this.growthStageThresholds[2] = growthStageThresholds[2];

        // Initialise max health values to be changed at each growth stage
        this.maxHealthAtStages[0] = 0.05 * maxHealth;
        this.maxHealthAtStages[1] = 0.1 * maxHealth;
        this.maxHealthAtStages[2] = 0.3 * maxHealth;

        // Initialise the image paths for the growth stages
        this.growthStageImagePaths[0] = growthStageImagePaths[0];
        this.growthStageImagePaths[1] = growthStageImagePaths[1];
        this.growthStageImagePaths[2] = growthStageImagePaths[2];
        this.growthStageImagePaths[3] = growthStageImagePaths[3];
        this.growthStageImagePaths[4] = growthStageImagePaths[4];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create() {
        super.create();

        // Initialise event listeners.
        entity.getEvents().addListener("harvest", this::harvest);
        entity.getEvents().addListener("destroyPlant", this::destroyPlant);
        entity.getEvents().addListener("attack", this::attack);
        ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateGrowthStage);
        ServiceLocator.getTimeService().getEvents().addListener("dayUpdate", this::beginDecay);
        this.currentTexture = entity.getComponent(DynamicTextureRenderComponent.class);
    }

    /**
     * Returns the current plant health
     *
     * @return current plant health
     */
    public int getPlantHealth() {
        return this.plantHealth;
    }

    /**
     * Set the current plant health
     * @param health - current plant health
     */
    public void setPlantHealth(int health) {
        this.plantHealth = health;
    }

    /**
     * Returns the max health of the plant
     *
     * @return current max health
     */

    public int getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * Increase (or decrease) the plant health by some value
     * @param plantHealthIncrement - plant health affected value
     */
    public void increasePlantHealth(int plantHealthIncrement) {
        this.plantHealth += plantHealthIncrement;
    }

    /**
     * Returns the name of the plant
     *
     * @return name of the plant
     */
    public String getPlantName() {
        return this.plantName;
    }

    /**
     * Returns the type of the plant
     *
     * @return type of the plant
     */
    public String getPlantType() {
        return this.plantType;
    }

    /**
     * Returns the plant description
     *
     * @return plant description
     */
    public String getPlantDescription() {
        return this.plantDescription;
    }

    /**
     * Set the plant to decaying stage.
     */
    public void setDecay() {
        this.growthStages = GrowthStage.DECAYING;
    }

    /**
     * Find out if the plant is decaying or not.
     * @return If the plant is in a state of decay or not
     */
    public boolean isDecay() {
        return this.growthStages == GrowthStage.DECAYING;
    }

    /**
     * Get the ideal water level of a plant
     * @return ideal water level
     */
    public float getIdealWaterLevel() {
        return this.idealWaterLevel;
    }

    /**
     * Get the current growth stage of a plant
     * @return current growth stage
     */
    public GrowthStage getGrowthStage() {
        return this.growthStages;
    }

    /**
     * Set the growth stage of a plant.
     *
     * @param newGrowthStage - The updated growth stage of the plant, between 1 and 6.
     */
    public void setGrowthStage(int newGrowthStage) {
        if (newGrowthStage >= 1 && newGrowthStage <= GrowthStage.values().length) {
            this.growthStages = GrowthStage.values()[newGrowthStage - 1];
        } else {
            throw new IllegalArgumentException("Invalid growth stage value.");
        }
    }

    /**
     * get the adult life span of a plant
     *
     * @return adult life span
     */
    public int getAdultLifeSpan() {
        return this.adultLifeSpan;
    }

    /**
     * Set the adult life span of a plant.
     * @param adultLifeSpan The number of days the plant will exist as an adult plant
     */
    public void setAdultLifeSpan(int adultLifeSpan) {
        this.adultLifeSpan = adultLifeSpan;
    }

    /**
     * Increment the current growth stage of a plant by 1
     *
     * @param growthIncrement The number of growth stages the plant will increase by
     */
    public void increaseGrowthStage(int growthIncrement) {
        this.growthStages.value += growthIncrement;
    }

    /**
     * Return the current growth level of the plant.
     * @return The current growth level
     */
    public int getCurrentGrowthLevel() {
        return this.currentGrowthLevel;
    }

    /**
     * Retrieves the current maximum health value of the plant.
     * @return The current maximum health of the plant.
     */
    public double getCurrentMaxHealth() {
        return this.currentMaxHealth;
    }

    /**
     * Retrieves the number of days the plant has been an adult.
     * @return The number of days the plant has been in its adult stage.
     */
    public int getNumOfDaysAsAdult() {
        return numOfDaysAsAdult;
    }

    /**
     * Sets the number of days the plant has been an adult.
     * @param numOfDaysAsAdult The number of days to set the plant's adult stage duration to.
     */
    public void setNumOfDaysAsAdult(int numOfDaysAsAdult) {
        this.numOfDaysAsAdult = numOfDaysAsAdult;
    }

    /**
     * Retrieves the current texture of the plant.
     * @return The current texture of the plant.
     */
    public DynamicTextureRenderComponent getTexture() {
        return this.currentTexture;
    }

    /**
     * Sets the harvest yield of this plant.
     *
     * <p>This will store an unmodifiable copy of the given map. Modifications made to the given
     * map after being set will not be reflected in the plant.
     *
     * @param harvestYields Map of item names to drop quantities.
     */
    public void setHarvestYields(Map<String, Integer> harvestYields) {
        this.harvestYields = Map.copyOf(harvestYields);
    }

    /**
     * Increase the currentGrowthLevel based on the growth rate provided by the CropTileComponent where the
     * plant is located.
     * If the growthRate received is negative, this indicates that the conditions are inhospitable for the
     * plant, and it will lose some health.
     * currentGrowthLevel can not increase once a plant is an adult, but the tile can become inhospitable and
     * decrease the adult plants' health.
     * If the plant is already decaying, do nothing.
     */
    void increaseCurrentGrowthLevel() {
        double growthRate = this.cropTile.getGrowthRate(this.idealWaterLevel);

        // Check if the growth rate is negative
        // That the plant is not decaying
        if ((growthRate < 0) && !isDecay() && (getGrowthStage().getValue() < GrowthStage.ADULT.value)) {
            increasePlantHealth(-10);
        } else if (!isDecay() && !(getGrowthStage().getValue() >= GrowthStage.ADULT.value)) {
            this.currentGrowthLevel += (int)(this.cropTile.getGrowthRate(this.idealWaterLevel) * 10);
        }
    }

    /**
     * Check if a plant is dead
     *
     * @return if the plant is fully decayed
     */
    public boolean isDead() {
        return this.growthStages == GrowthStage.DEAD;
    }

    /**
     * Harvests this plant, causing it to drop its produce.
     *
     * <p>If this plant is not in a growth stage where is can be harvested, nothing will happen.
     */
    private void harvest() {
        if (growthStages != GrowthStage.ADULT) {
            // Cannot harvest when not an adult (or decaying).
            return;
        }

        playSound("harvest");
        harvestYields.forEach((itemName, quantity) -> {
            Supplier<Entity> itemSupplier = ItemFactory.getItemSupplier(itemName);
            for (int i = 0; i < quantity; i++) {
                Entity item = itemSupplier.get();
                item.setPosition(entity.getPosition());
                ServiceLocator.getEntityService().register(item);
            }
        });
        destroyPlant();
    }

    /**
     * Destroys this plant and clears the crop tile.
     */
    private void destroyPlant() {
        cropTile.setUnoccupied();
        entity.dispose();
    }

    /**
     * To attack plants and damage their health.
     */
    private void attack() {
        int attackDamage = 10;
        increasePlantHealth(-attackDamage);
    }

    /**
     * Update the growth stage of a plant based on its current growth level and its adult life span.
     * This method is called every (in game) day at midday (12pm).
     * If the currentGrowthLevel exceeds the corresponding growth threshold, then the plant will
     * advance to the next growth stage.
     * When a plant becomes an adult, it has an adult life span. When the number of days since a
     * plant becomes and adult exceeds adult life span of a plant, then it starts to decay.
     */
    public void updateGrowthStage() {
        int time = ServiceLocator.getTimeService().getHour();
        if (time == 12) {
            this.increaseCurrentGrowthLevel();
            if (getGrowthStage().getValue() <= GrowthStage.ADULT.value) {
                if (this.currentGrowthLevel >= this.growthStageThresholds[getGrowthStage().getValue() - 1]) {
                    setGrowthStage(getGrowthStage().getValue() + 1);
                    updateMaxHealth();
                    updateTexture();
                }
            }
            if (getGrowthStage() == GrowthStage.ADULT) {
                beginDecay();
            }
        }
    }

    /**
     * Update the maximum health of the plant based on its current growth stage.
     * Called whenever the growth stage is changed
     */
    public void updateMaxHealth() {
        switch (getGrowthStage()) {
            case SEEDLING -> this.currentMaxHealth = this.maxHealthAtStages[0];
            case SPROUT -> this.currentMaxHealth = this.maxHealthAtStages[1];
            case JUVENILE -> this.currentMaxHealth = this.maxHealthAtStages[2];
            case ADULT -> this.currentMaxHealth = this.maxHealth;
            default -> throw new IllegalStateException("Unexpected value: " + getGrowthStage());
        };
    }

    /**
     * An adult plant will live for a certain number of days (adultLifeSpan). Once a plant has
     * exceeded its adultLifeSpan it will begin to decay.
     */
    public void beginDecay() {
        if (getGrowthStage() == GrowthStage.ADULT) {
            this.numOfDaysAsAdult += 1;
            if (getNumOfDaysAsAdult() > getAdultLifeSpan()) {
                setGrowthStage(getGrowthStage().getValue() + 1);
                setDecay();
                updateTexture();
                playSound("decays");
            }
        }
    }

    /**
     * Update the texture of the plant based on its current growth stage.
     */
    public void updateTexture() {
        if (getGrowthStage().getValue() <= GrowthStage.DECAYING.value) {
            String path = this.growthStageImagePaths[getGrowthStage().getValue() - 1];

            if (this.currentTexture != null) {
                this.currentTexture.setTexture(path);
            }
        }
    }

    /**
     * Determine what sound needs to be called based on the type of interaction.
     * The sounds are separated into tuple pairs for each event.
     *
     * @param functionCalled The type of interaction with the plant.
     */
    public void playSound(String functionCalled) {
        switch (functionCalled) {
            case "click" -> chooseSound(sounds[0], sounds[1]);
            case "decays" -> chooseSound(sounds[2], sounds[3]);
            case "destroy" -> chooseSound(sounds[4], sounds[5]);
            case "nearby" -> chooseSound(sounds[6], sounds[7]);
            default -> throw new IllegalStateException("Unexpected function: " + functionCalled);
        }
    }

    /**
     * Decide what sound to play. There is a small random chance that a lore sound will be selected, otherwise
     * the normal sound will be played.
     * @param lore Sound associated with the secret lore
     * @param notLore Normal sound that the player will be expecting.
     */
    void chooseSound(String lore, String notLore) {
        boolean playLoreSound = random.nextInt(100) <= 0; //Gives 1% chance of being true
        Sound soundEffect;

        if (playLoreSound) {
            soundEffect = ServiceLocator.getResourceService().getAsset(lore, Sound.class);
        } else {
            soundEffect = ServiceLocator.getResourceService().getAsset(notLore, Sound.class);
        }
        soundEffect.play();
    }
}
