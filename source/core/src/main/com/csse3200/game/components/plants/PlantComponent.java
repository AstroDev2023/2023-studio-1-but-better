package com.csse3200.game.components.plants;
import com.csse3200.game.components.Component;

/**
 * Class for all plants in the game.
 */
public class PlantComponent extends Component {
    private int plantHealth;            // Initial plant health
    private String plantName;           // User facing plant name
    private String plantType;           // Type of plant (food, health, repair, defence, production)
    private String plantDescription;    // User facing description of the plant
    private int healingRadius;          // Property of repair plants

    private int damageRadius;           // Property of deadly plants
    private int effectRadius;           // Radius defence plant covers

    /**
     * Constructor used for plant types that have no extra properties.
     *
     * @param health
     * @param name
     * @param plantType
     * @param plantDescription
     */
    public PlantComponent(int health, String name, String plantType, String plantDescription) {
        this.plantHealth = health;
        this.plantName = name;
        this.plantType = plantType;
        this.plantDescription = plantDescription;
    }

    /**
     * Constructor for plant types that have an extra property that is an integer.
     * @param health
     * @param name
     * @param plantType
     * @param plantDescription
     * @param number - A number corresponding to a specialised plant property
     */
    public PlantComponent(int health, String name, String plantType, String plantDescription, int number) {
        this.plantHealth = health;
        this.plantName = name;
        this.plantType = plantType;
        this.plantDescription = plantDescription;

        if (this.plantType.equals("REPAIR")) {
            this.healingRadius = number;
        } else if (this.plantType.equals("DEFENCE")) {
            this.effectRadius = number;
        } else if (this.plantType.equals("DEADLY")) {
            this.damageRadius = number;
        }
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
     * Returns the healing radius of repair plants
     *
     * @return healing radius of repair plants
     */
    public int getHealingRadius() {
        return this.healingRadius;
    }

    /**
     * Returns the damage radius of deadly plants
     *
     * @return damage radius of deadly plants
     */
    public int getDamageRadius() {
        return this.damageRadius;
    }

    /**
     * Returns the radius that a defence plant defends
     *
     * @return defence radius of defensive plants
     */
    public int getEffectRadius() {
        return this.effectRadius;
    }
}
