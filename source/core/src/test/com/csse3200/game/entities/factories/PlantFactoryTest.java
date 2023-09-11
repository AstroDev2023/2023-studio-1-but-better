package com.csse3200.game.entities.factories;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.plants.BasePlantConfig;
import com.csse3200.game.entities.configs.plants.PlantConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * This test class aims to test the PlantFactory which is responsible for
 * creating different plants in the game.
 */
class PlantFactoryTest {
    PlantConfigs stats;

    // Mocked dependencies
    @Mock
    static CropTileComponent mockCropTile;
    @Mock
    ResourceService mockResourceService;
    @Mock
    Texture mockTexture;
    @Mock
    Entity mockEntity;
    @Mock
    TextureRenderComponent mockRenderComponent;
    @Mock
    BasePlantConfig mockConfig;

    /**
     * Set up required objects and mocked dependencies before each test.
     */
    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        setupMocks();
        setupGdxFiles();
    }

    /**
     * Sets up mock objects required for the test.
     */
    void setupMocks() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerResourceService(mockResourceService);
        when(mockEntity.getComponent(TextureRenderComponent.class)).thenReturn(mockRenderComponent);
        when(mockCropTile.getEntity()).thenReturn(mockEntity);
        when(mockEntity.getPosition()).thenReturn(new Vector2(5, 5));
    }

    /**
     * Mocks the file operations for the GDX framework.
     *
     * @throws IOException if there is an error accessing or reading the file.
     */
    void setupGdxFiles() throws IOException {
        Gdx.files = mock(Files.class);
        FileHandle mockFileHandle = mock(FileHandle.class);
        String filePath = "configs/plant.json";
        InputStream inputStream = new FileInputStream(filePath);
        String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        when(mockFileHandle.reader("UTF-8")).thenReturn(new StringReader(jsonContent));
        when(Gdx.files.internal(anyString())).thenReturn(mockFileHandle);

        stats = FileLoader.readClass(PlantConfigs.class, filePath);
        PlantFactory.setStats(stats);
    }

    /**
     * Test to ensure plant configurations are loaded correctly.
     *
     * @param id           The unique identifier for the plant.
     * @param health       The health value of the plant.
     * @param name         The name of the plant.
     * @param type         The type category of the plant.
     * @param description  A brief description of the plant's characteristics or purpose.
     * @param water        The ideal water level for the plant.
     * @param life         The expected adult lifespan of the plant.
     * @param maxHealth    The maximum possible health value of the plant.
     */
    @ParameterizedTest
    @MethodSource("plantConfigProvider")
    void shouldLoadPlantConfigs(String id, int health, String name, String type,
                                                String description, float water, int life,
                                                int maxHealth) {
        BasePlantConfig actualValues = getActualValue(id);
        String errMsg = "Mismatched value for plant " + id + ": %s";

        assertTrue(health >= actualValues.health, String.format(errMsg, "health"));
        assertEquals(name, actualValues.name, String.format(errMsg, "name"));
        assertEquals(type, actualValues.type, String.format(errMsg, "type"));
        assertEquals(description, actualValues.description, String.format(errMsg, "description"));
        assertEquals(water, actualValues.idealWaterLevel, String.format(errMsg, "water level"));
        assertEquals(life, actualValues.adultLifeSpan, String.format(errMsg, "life span"));
        assertTrue(maxHealth >= actualValues.maxHealth, String.format(errMsg, "max health"));
    }

    /**
     * Data provider for plant configurations.
     *
     * @return Stream of Arguments containing plant data.
     */
    static Stream<Arguments> plantConfigProvider() {
        return Stream.of(
                Arguments.of("cosmicCob", 10, "Cosmic Cob", "FOOD",
                        "A nutritious snack with everything a human needs to survive, the local " +
                                "fauna won’t touch it though. Suspiciously high in protein and fat…",
                        (float) 0.7, 5, 400),
                Arguments.of("aloeVera", 10, "Aloe Vera", "HEALTH",
                        "A unique plant that once ground down to a chunky red paste can be used " +
                                "to heal significant wounds, it’s a miracle!", (float) 0.7, 5, 400),
                Arguments.of("hammerPlant", 10, "Hammer Plant", "REPAIR",
                        "A useful plant resembling a hand holding a hammer that repairs the " +
                                "other nearby plants, maybe they were friends!", (float) 0.7, 5, 400),
                Arguments.of("venusFlyTrap", 10, "Space Snapper", "DEFENCE",
                        "A hangry plant that will gobble any nasty pests nearby. Keep small pets " +
                                "and children out of snapping distance!", (float) 0.7, 5, 400),
                Arguments.of("waterWeed", 10, "Atomic Algae", "PRODUCTION",
                        "A highly efficient oxygen-producing plant.", (float) 0.7, 5, 400),
                Arguments.of("nightshade", 10, "Deadly Nightshade",
                        "DEADLY", "Grows deadly poisonous berries.", (float) 0.7, 5, 400)
        );
    }

    /**
     * Utility method to fetch the actual plant configuration based on the plant name.
     *
     * @param id    The unique identifier of the plant.
     * @return      The actual plant configuration.
     */
    BasePlantConfig getActualValue(String id) {
        return switch (id) {
            case "cosmicCob" -> stats.cosmicCob;
            case "aloeVera" -> stats.aloeVera;
            case "hammerPlant" -> stats.hammerPlant;
            case "venusFlyTrap" -> stats.venusFlyTrap;
            case "waterWeed" -> stats.waterWeed;
            case "nightshade" -> stats.nightshade;
            default -> throw new IllegalArgumentException("Unknown plant name: " + id);
        };
    }

    /**
     * Test for the creation of a base plant. Ensures that all necessary components
     * are properly attached to the created plant entity.
     */
    @Test
    void shouldCreateBasePlantWithExpectedComponents() {
        when(mockResourceService.getAsset("images/plants/Corn.png", Texture.class)).thenReturn(mockTexture);
        Entity plant = PlantFactory.createBasePlant(mockConfig, mockCropTile);
        assertNotNull(plant, "Plant Entity should not be null");

        PhysicsComponent physicsComponent = plant.getComponent(PhysicsComponent.class);
        assertNotNull(physicsComponent, "Plant PhysicsComponent should not be null");
        assertEquals(BodyDef.BodyType.StaticBody, physicsComponent.getBody().getType(),
                "Plant physics body type should be StaticBody");

        ColliderComponent colliderComponent = plant.getComponent(ColliderComponent.class);
        colliderComponent.create();
        assertNotNull(colliderComponent, "Plant ColliderComponent should not be null");
        assertTrue(colliderComponent.getFixture().isSensor(),
                "Plant collider should be a sensor");

        HitboxComponent hitboxComponent = plant.getComponent(HitboxComponent.class);
        hitboxComponent.create();
        assertNotNull(hitboxComponent, "Plant HitboxComponent should not be null");
        assertTrue(hitboxComponent.getFixture().isSensor(),
                "Plant Hitbox should be a sensor");
        assertEquals(PhysicsLayer.OBSTACLE, hitboxComponent.getLayer(),
                "Plant Hitbox layer should be OBSTACLE");
    }

    /**
     * Provides plant statistics for parameterized tests.
     *
     * @return Stream of Arguments containing plant data.
     */
    static Stream<Arguments> plantStatsProvider() {
        return Stream.of(
                Arguments.of("cosmicCob", "images/plants/Corn.png",
                        (Callable<Entity>) () -> PlantFactory.createCosmicCob(mockCropTile)),
                Arguments.of("aloeVera", "images/plants/Corn.png",
                        (Callable<Entity>) () -> PlantFactory.createAloeVera(mockCropTile)),
                Arguments.of("hammerPlant", "images/plants/Corn.png",
                        (Callable<Entity>) () -> PlantFactory.createHammerPlant(mockCropTile)),
                Arguments.of("nightshade", "images/plants/Corn.png",
                        (Callable<Entity>) () -> PlantFactory.createNightshade(mockCropTile)),
                Arguments.of("waterWeed", "images/plants/Corn.png",
                        (Callable<Entity>) () -> PlantFactory.createWaterWeed(mockCropTile)),
                Arguments.of("venusFlyTrap", "images/plants/Corn.png",
                        (Callable<Entity>) () -> PlantFactory.createVenusFlyTrap(mockCropTile))
        );
    }

    /**
     * Verifies if plants are associated with the correct texture paths.
     *
     * @param id      The unique identifier for the plant.
     * @param path    The path of the plant's texture in the asset directory.
     * @param createPlant   The method to create the specific plant.
     * @throws Exception    If there's an error during plant creation or verification.
     */
    @ParameterizedTest
    @MethodSource("plantStatsProvider")
    void verifyPlantTexturePath(String id, String path, Callable<Entity> createPlant)
            throws Exception {
        when(mockResourceService.getAsset(path, Texture.class)).thenReturn(mockTexture);
        assertNotNull(createPlant.call(), id + " entity is null!");
        verify(mockResourceService).getAsset(path, Texture.class);
    }

    /**
     * Tests if the properties of a created plant match expected values.
     *
     * @param id   The unique identifier for the plant.
     * @param path The path of the plant's texture in the asset directory.
     * @param createPlant The method to create the specific plant.
     * @throws Exception If there's an error during plant creation or verification.
     */
    @ParameterizedTest
    @MethodSource("plantStatsProvider")
    void plantsShouldSetCorrectProperties(String id, String path, Callable<Entity> createPlant)
            throws Exception {
        BasePlantConfig expectedValues = getActualValue(id);
        String errMsg = "Mismatched value for plant " + id + ": %s";

        when(mockResourceService.getAsset(path, Texture.class)).thenReturn(mockTexture);
        Entity plant = createPlant.call();
        PlantComponent plantComponent = plant.getComponent(PlantComponent.class);

        assertEquals(expectedValues.health, plantComponent.getPlantHealth(),
                String.format(errMsg, "health"));
        assertEquals(expectedValues.name, plantComponent.getPlantName(),
                String.format(errMsg, "name"));
        assertEquals(expectedValues.type, plantComponent.getPlantType(),
                String.format(errMsg, "type"));
        assertEquals(expectedValues.description, plantComponent.getPlantDescription(),
                String.format(errMsg, "description"));
        assertEquals(expectedValues.idealWaterLevel, plantComponent.getIdealWaterLevel(),
                String.format(errMsg, "idealWaterLevel"));
        assertEquals(expectedValues.adultLifeSpan, plantComponent.getAdultLifeSpan(),
                String.format(errMsg, "adultLifeSpan"));
        assertEquals(expectedValues.maxHealth, plantComponent.getMaxHealth(),
                String.format(errMsg, "maxHealth"));
    }

    /**
     * Tests if the positions of the created plants are correct.
     *
     * @param id   The unique identifier for the plant.
     * @param path The path of the plant's texture in the asset directory.
     * @param createPlant The method to create the specific plant.
     * @throws Exception If there's an error during plant creation or verification.
     */
    @ParameterizedTest
    @MethodSource("plantStatsProvider")
    void shouldSetCorrectPlantPosition(String id, String path, Callable<Entity> createPlant)
            throws Exception {
        String errMsg = "Mismatched value for plant " + id + ": %s";

        when(mockResourceService.getAsset(path, Texture.class)).thenReturn(mockTexture);
        Entity plant = createPlant.call();

        Vector2 expectedPos = new Vector2(5, 5.5f);
        assertEquals(expectedPos, plant.getPosition(), String.format(errMsg, "position"));

        assertEquals(1f, plant.getScale().y, 0.001, String.format(errMsg, "height"));
    }

    /**
     * Tests that the setScaledCollider sets the collider of the plant entity correctly.
     */
    @Test
    void testSetScaledCollider() {
        Entity plant = new Entity();
        plant.setScale(2f, 2f);
        ColliderComponent mockCollider = mock(ColliderComponent.class);
        plant.addComponent(mockCollider);

        PhysicsUtils.setScaledCollider(plant, 0.5f, 0.2f);

        Vector2 expectedScale = new Vector2(1f, 0.4f);
        verify(mockCollider, times(1)).setAsBoxAligned(
                eq(expectedScale),
                eq(PhysicsComponent.AlignX.CENTER),
                eq(PhysicsComponent.AlignY.BOTTOM)
        );
    }
}
