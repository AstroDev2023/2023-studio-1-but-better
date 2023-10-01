package com.csse3200.game.entities;

/**
 * An enum of all the entity types in the game.
 * Feel free to add yours here. Read Documentation for SaveLoad and follow procedure there
 */
public enum EntityType {
    Player(0),
    Tractor(0),
    Plant(10),
    Tile(0),  // This is team 7 stuff
    Cow(0),
    Chicken(0),
    Astrolotl(0),
    OxygenEater(-10),
    Dragonfly(0),
    Bat(0),
    Item(0),
    Questgiver(0),
    QuestgiverIndicator(0),
    Chest(0),
    Fence(0),
    Gate(0),
    Sprinkler(0),
    Light(0),
    Ship(0),
    ShipDebris(0),
    FireFlies(0);


    // Negative rate for consumption, positive for production of oxygen
    private final float hourlyOxygenRate;

    EntityType(float hourlyOxygenRate) {
        this.hourlyOxygenRate = hourlyOxygenRate;
    }

    /**
     * Getter method for the oxygen consumption/production rate
     * of a given entity type.
     *
     * @return the hourly oxygen rate of the entity type.
     */
    public float getOxygenRate() {
        return hourlyOxygenRate;
    }
}


