package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@ExtendWith(GameExtension.class)
public class PlayerMovementIntegrationTest {
    private Entity player;

    @BeforeEach
    void initialiseTest() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerRenderService(new RenderService());

        ResourceService resourceService = new ResourceService();
        resourceService.loadTextureAtlases(new String[]{"images/player.atlas"});
        resourceService.loadTextures(new String[]{"images/heart.png"});
        resourceService.loadAll();
        ServiceLocator.registerResourceService(resourceService);
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/player.atlas", TextureAtlas.class)
                );

        PlayerFactory.setupPlayerAnimator(animator);

        player = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PlayerActions())
                .addComponent(animator)
                .addComponent(new PlayerAnimationController());

        player.getComponent(AnimationRenderComponent.class).scaleEntity();
        player.create();
    }

    @ParameterizedTest(name = "{2} animation played correctly when {0}")
    @MethodSource({"shouldPlayCorrectMoveAnimationParams"})
    void shouldPlayCorrectMoveAnimation(Vector2 moveDirection, boolean isRunning, String expectedAnimationName) {
        AnimationRenderComponent animationRenderComponent = player.getComponent(AnimationRenderComponent.class);
        PlayerActions playerActionsComponent = player.getComponent(PlayerActions.class);

        if (isRunning) player.getEvents().trigger("run");
        player.getEvents().trigger("move", moveDirection);
        playerActionsComponent.update();
        assertEquals(expectedAnimationName, animationRenderComponent.getCurrentAnimation());
    }

    private static Stream<Arguments> shouldPlayCorrectMoveAnimationParams() {
        return Stream.of(
                // ((testDescription, moveDirection), isRunning, expectedAnimationName)
                arguments(named("walking up", Vector2Utils.UP), false, "walk_up"),
                arguments(named("walking left", Vector2Utils.LEFT), false, "walk_left"),
                arguments(named("walking down", Vector2Utils.DOWN), false, "walk_down"),
                arguments(named("walking right", Vector2Utils.RIGHT), false, "walk_right"),
                arguments(named("walking up & right", Vector2Utils.UP.add(Vector2Utils.RIGHT)), false, "walk_up"),
                arguments(named("walking up & left", Vector2Utils.UP.add(Vector2Utils.LEFT)), false, "walk_up"),
                arguments(named("walking down & right", Vector2Utils.DOWN.add(Vector2Utils.RIGHT)), false, "walk_down"),
                arguments(named("walking down & left", Vector2Utils.DOWN.add(Vector2Utils.LEFT)), false, "walk_down"),
                arguments(named("running up", Vector2Utils.UP), true, "run_up"),
                arguments(named("running left", Vector2Utils.LEFT), true, "run_left"),
                arguments(named("running down", Vector2Utils.DOWN), true, "run_down"),
                arguments(named("running right", Vector2Utils.RIGHT), true, "run_right"),
                arguments(named("running up & right", Vector2Utils.UP.add(Vector2Utils.RIGHT)), true, "run_up"),
                arguments(named("running up & left", Vector2Utils.UP.add(Vector2Utils.LEFT)), true, "run_up"),
                arguments(named("running down & right", Vector2Utils.DOWN.add(Vector2Utils.RIGHT)), true, "run_down"),
                arguments(named("running down & left", Vector2Utils.DOWN.add(Vector2Utils.LEFT)), true, "run_down")
        );
    }

    @ParameterizedTest(name = "default animation played correctly when stopped after {0}")
    @MethodSource({"shouldReturnToDefaultAnimationOnStopParams"})
    void shouldReturnToDefaultAnimationOnStop(String prevDirection, String expectedAnimation) {
        AnimationRenderComponent animationRenderComponent = player.getComponent(AnimationRenderComponent.class);
        player.getEvents().trigger("animationWalkStop",prevDirection );
        assertEquals(expectedAnimation, animationRenderComponent.getCurrentAnimation());
    }

    private static Stream<Arguments> shouldReturnToDefaultAnimationOnStopParams() {
        return Stream.of(
                // ((testDescription, prev moveDirection), isRunning, expectedAnimationName)
                arguments("right", "idle_right"),
                arguments("down", "idle_down"),
                arguments("up", "idle_up"),
                arguments("right", "idle_right")
        );
    }
}