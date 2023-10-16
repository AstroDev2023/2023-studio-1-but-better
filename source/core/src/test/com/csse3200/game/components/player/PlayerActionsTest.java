package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.combat.CombatStatsComponent;
import com.csse3200.game.components.combat.ProjectileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlayerActionsTest {

    private List<Entity> areaEntities;

    private Entity player;
    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        PhysicsService physicsService = new PhysicsService();

        ServiceLocator.registerTimeSource(new GameTime());

        ServiceLocator.registerPhysicsService(physicsService);
        ServiceLocator.registerResourceService(mock(ResourceService.class));

        ServiceLocator.registerEntityService(mock(EntityService.class));
        ServiceLocator.registerTimeService(new TimeService());

        ServiceLocator.registerGameArea(mock(GameArea.class));

        areaEntities = new ArrayList<>();
        player = new Entity();
        player.addComponent(new PlayerActions());
        player.setPosition(0,0);
        Entity chicken = new Entity();
        chicken.addComponent(new CombatStatsComponent(10, 0));
        chicken.setPosition(1,1);
        areaEntities.add(player);
        areaEntities.add(chicken);


        when(ServiceLocator.getGameArea().getAreaEntities()).thenReturn(areaEntities);
        when(ServiceLocator.getResourceService().getAsset(any(), any())).thenReturn(mock(Sound.class));

        ServiceLocator.registerCameraComponent(mock(CameraComponent.class));
        when(ServiceLocator.getCameraComponent().screenPositionToWorldPosition(any())).thenReturn(new Vector2(2, 2));
    }

    @AfterEach
    void clear() {
        ServiceLocator.clear();
    }

    @Test
    void playerShouldAttackNPC() {
        PlayerActions playerActions = player.getComponent(PlayerActions.class);
        ServiceLocator.registerParticleService(mock(ParticleService.class));
        playerActions.attack(new Vector2(2,2));
        assertEquals(5, areaEntities.get(1).getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void playerShouldShootNPC() {
        Entity bullet = new Entity();
        bullet.addComponent(mock(ProjectileComponent.class));
        bullet.addComponent(new CombatStatsComponent(1, 1));
        mockStatic(ProjectileFactory.class);
        when(ProjectileFactory.createPlayerProjectile()).thenReturn(bullet);
        doAnswer((i) -> {
            areaEntities.add(bullet);
            return null;
        }).when(mock(GameArea.class)).spawnEntity(any());
        PlayerActions playerActions = player.getComponent(PlayerActions.class);
        playerActions.shoot(new Vector2(2,2));
        assertEquals(2, areaEntities.size());
    }

}
