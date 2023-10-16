package com.csse3200.game.services.sound;

public enum EffectSoundFile implements SoundFile {
    // TODO: Add enum declarations for all effect sound tracks
    TRACTOR_HONK("sounds/car-horn-6408.mp3"),
    TRACTOR_START_UP("sounds/tractor-start-up.wav"),
    SHOVEL("sounds/shovel.wav"),
    HOE("sounds/hoe.wav"),
    SCYTHE("sounds/hoe.wav"),
    WATERING_CAN("sounds/watering-can.wav"),
    FISHING_CAST("sounds/fishing-cast.wav"),
    FISHING_CATCH("sounds/applause.wav"),
    PLACE("sounds/place.wav"),
    GATE_INTERACT("sounds/gate-interact.wav"),
    IMPACT("sounds/Impact4.ogg"),
    ATTACK_MISS("sounds/weapons/SwordSwing.mp3"),
    ATTACK_HIT("sounds/weapons/SwordHitEntity.mp3"),
    GUN_ATTACK("sounds/weapons/GunAttack.mp3"),
    INVENTORY_OPEN("sounds/open-bag-sound-effect.mp3"),
    HOTKEY_SELECT("sounds/take-item-sound-effect.mp3"),
    CHICKEN_FEED("sounds/animals/ChickenFeed.mp3"),
    COW_FEED("sounds/animals/CowFeed.mp3"),
    ASTROLOTL_FEED("sounds/animals/AstrolotlFeed.mp3"),
    TAMED_ANIMAL("sounds/animals/TamedAnimal.mp3"),
    COW_DEATH("sounds/animals/CowDeath.mp3"),
    OXYGEN_EAT_DEATH("sounds/animals/DeathOxygenEater.mp3"),
    DRAGONFLY_DEATH("sounds/animals/DeathDragonFly.mp3"),
    DEATH_BATS("sounds/animals/DeathBats.mp3"),
    PLAYER_DEATH("sounds/player/PlayerDeath.mp3"),
    GUN_RELOAD("sounds/weapons/GunReload.mp3"),
    PLAYER_DAMAGE("sounds/player/PlayerGetsHit.mp3"),
    BAT_ATTACK("sounds/animals/BatAttack.mp3"),
    OXYGEN_ATTACK("sounds/animals/OxygenEaterAttack.mp3"),
    DRAGONFLY_ATTACK_PLANT("sounds/animals/DragonflyAttackPlant.mp3"),
    DRAGONFLY_ATTACK_PLAYER("sounds/animals/DragonFlyAttackPlayer.mp3"),
    CHICKEN_DEATH("sounds/animals/ChickenDeath.mp3"),
    GOD_DID("sounds/god-did.mp3"),
    PLANT_CLICK("sounds/plants/click.wav"),
    PLANT_DECAY("sounds/plants/decay.wav"),
    PLANT_DESTROY("sounds/plants/destroy.wav"),
    PLANT_NEARBY("sounds/plants/nearby.wav"),
    ALOE_VERA_CLICK_LORE("sounds/plants/aloeVera/clickLore.wav"),
    ALOE_VERA_DECAY_LORE("sounds/plants/aloeVera/decayLore.wav"),
    ALOE_VERA_DESTROY_LORE("sounds/plants/aloeVera/destroyLore.wav"),
    ALOE_VERA_NEARBY_LORE("sounds/plants/aloeVera/nearbyLore.wav"),
    COSMIC_COB_CLICK_LORE("sounds/plants/cosmicCob/clickLore.wav"),
    COSMIC_COB_DECAY_LORE("sounds/plants/cosmicCob/decayLore.wav"),
    COSMIC_COB_DESTROY_LORE("sounds/plants/cosmicCob/destroyLore.wav"),
    COSMIC_COB_NEARBY_LORE("sounds/plants/cosmicCob/nearbyLore.wav"),
    HAMMER_PLANT_CLICK_LORE("sounds/plants/hammerPlant/clickLore.wav"),
    HAMMER_PLANT_DECAY_LORE("sounds/plants/hammerPlant/decayLore.wav"),
    HAMMER_PLANT_DESTROY_LORE("sounds/plants/hammerPlant/destroyLore.wav"),
    HAMMER_PLANT_NEARBY_LORE("sounds/plants/hammerPlant/nearbyLore.wav"),
    DEADLY_NIGHTSHADE_CLICK_LORE("sounds/plants/nightshade/clickLore.wav"),
    DEADLY_NIGHTSHADE_DECAY_LORE("sounds/plants/nightshade/decayLore.wav"),
    DEADLY_NIGHTSHADE_DESTROY_LORE("sounds/plants/nightshade/destroyLore.wav"),
    DEADLY_NIGHTSHADE_NEARBY_LORE("sounds/plants/nightshade/nearbyLore.wav"),
    SPACE_SNAPPER_CLICK_LORE("sounds/plants/spaceSnapper/clickLore.wav"),
    SPACE_SNAPPER_DECAY_LORE("sounds/plants/spaceSnapper/decayLore.wav"),
    SPACE_SNAPPER_DESTROY_LORE("sounds/plants/spaceSnapper/destroyLore.wav"),
    SPACE_SNAPPER_NEARBY_LORE("sounds/plants/spaceSnapper/nearbyLore.wav"),
    ATOMIC_ALGAE_CLICK_LORE("sounds/plants/atomicAlgae/clickLore.wav"),
    ATOMIC_ALGAE_DECAY_LORE("sounds/plants/atomicAlgae/decayLore.wav"),
    ATOMIC_ALGAE_DESTROY_LORE("sounds/plants/atomicAlgae/destroyLore.wav"),
    ATOMIC_ALGAE_NEARBY_LORE("sounds/plants/atomicAlgae/nearbyLore.wav"),
    ;

    private final String filePath;

    EffectSoundFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getFilePath() {
        return this.filePath;
    }
}
