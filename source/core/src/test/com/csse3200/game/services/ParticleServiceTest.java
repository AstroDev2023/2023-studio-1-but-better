package com.csse3200.game.services;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.ParticleEffectComponent;
import com.csse3200.game.rendering.ParticleEffectWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticleServiceTest {


	ParticleEffect mockEffect;
	ResourceService resourceService;

	@BeforeEach
	void setUp() {
		ServiceLocator.clear();

		resourceService = mock(ResourceService.class);
		ServiceLocator.registerResourceService(resourceService);
		mockEffect = mock(ParticleEffect.class);
	}

	@AfterEach
	void cleanUp() {
		ServiceLocator.clear();
	}

	@Test
	void testConstructor() throws IllegalAccessException {
		ParticleService particleService = new ParticleService();
		// Tests whether they were all loaded
		verify(resourceService, times(1)).loadParticleEffects(any());
		verify(resourceService, times(1)).loadAll();
		// Tests if assets were retrieved after they were loaded
		verify(resourceService, times(ParticleService.ParticleEffectType.values().length)).getAsset(anyString(), any());
	}

	@Test
	void testRender() throws IllegalAccessException {
		@SuppressWarnings("unchecked")
		ArrayList<ParticleEffectWrapper> mockQueuedEffects = mock(ArrayList.class);

		ParticleService particleService = new ParticleService();
		Field queuedField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("queuedEffects"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);

		queuedField.setAccessible(true);
		queuedField.set(particleService, mockQueuedEffects);

		ParticleEffectWrapper wrapper = mock(ParticleEffectWrapper.class);
		ParticleEffectPool.PooledEffect pooledEffect = mock(ParticleEffectPool.PooledEffect.class);
		when(wrapper.getPooledEffect()).thenReturn(pooledEffect);

		@SuppressWarnings("unchecked")
		Iterator<ParticleEffectWrapper> iter = mock(Iterator.class);
		when(mockQueuedEffects.iterator()).thenReturn(iter);
		when(iter.hasNext()).thenReturn(true, false);
		when(iter.next()).thenReturn(wrapper);

		OrthographicCamera camera = new OrthographicCamera();
		CameraComponent cameraComponent = mock(CameraComponent.class);
		when(cameraComponent.getCamera()).thenReturn(camera);

		ServiceLocator.registerCameraComponent(cameraComponent);

		particleService.render(mock(SpriteBatch.class), 0f);

		verify(pooledEffect, times(1)).draw(any(SpriteBatch.class), anyFloat());
		verify(pooledEffect, times(1)).setPosition(anyFloat(), anyFloat());
	}

	@Test
	void testStartEffect() throws IllegalAccessException {
		@SuppressWarnings("unchecked")
		EnumMap<ParticleService.ParticleEffectType, ParticleEffectPool> mockPools = mock(EnumMap.class);

		@SuppressWarnings("unchecked")
		ArrayList<ParticleEffectWrapper> mockQueuedEffects = mock(ArrayList.class);

		ParticleService particleService = new ParticleService();
		Field poolsField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("particleEffectPools"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
		Field queuedField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("queuedEffects"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);

		poolsField.setAccessible(true);
		queuedField.setAccessible(true);
		poolsField.set(particleService, mockPools);
		queuedField.set(particleService, mockQueuedEffects);

		ParticleEffectPool pool = mock(ParticleEffectPool.class);
		ParticleEffectPool.PooledEffect pooledEffect = mock(ParticleEffectPool.PooledEffect.class);
		when(mockPools.get(any(ParticleService.ParticleEffectType.class))).thenReturn(pool);
		when(pool.obtain()).thenReturn(pooledEffect);

		particleService.startEffect(ParticleService.ParticleEffectType.RAIN);

		verify(mockQueuedEffects, times(1)).add(any(ParticleEffectWrapper.class));
		verify(pooledEffect, times(1)).start();
	}

	@Test
	void testStopEffect() throws IllegalAccessException {
		// Injecting mock into private field
		@SuppressWarnings("unchecked")
		EnumMap<ParticleService.ParticleEffectType, ParticleEffectPool> mockPools = mock(EnumMap.class);
		@SuppressWarnings("unchecked")
		ArrayList<ParticleEffectWrapper> mockQueuedEffects = mock(ArrayList.class);
		ParticleService particleService = new ParticleService();

		Field poolsField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("particleEffectPools"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
		Field queuedField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("queuedEffects"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);

		poolsField.setAccessible(true);
		queuedField.setAccessible(true);
		poolsField.set(particleService, mockPools);
		queuedField.set(particleService, mockQueuedEffects);

		ParticleEffectPool pool = mock(ParticleEffectPool.class);
		ParticleEffectPool.PooledEffect pooledEffect = mock(ParticleEffectPool.PooledEffect.class);
		when(mockPools.get(any(ParticleService.ParticleEffectType.class))).thenReturn(pool);
		when(pool.obtain()).thenReturn(pooledEffect);

		particleService.startEffect(ParticleService.ParticleEffectType.RAIN);
		assertFalse(mockQueuedEffects.isEmpty());


		particleService.stopEffect(ParticleService.ParticleEffectType.RAIN);
		assertEquals(0, mockQueuedEffects.size());
	}

	@Test
	void testStopEffectCategory() throws IllegalAccessException {
		// Injecting mock into private field
		@SuppressWarnings("unchecked")
		EnumMap<ParticleService.ParticleEffectType, ParticleEffectPool> mockPools = mock(EnumMap.class);
		@SuppressWarnings("unchecked")
		ArrayList<ParticleEffectWrapper> mockQueuedEffects = mock(ArrayList.class);
		ParticleService particleService = new ParticleService();

		Field poolsField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("particleEffectPools"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
		Field queuedField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("queuedEffects"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);

		poolsField.setAccessible(true);
		queuedField.setAccessible(true);
		poolsField.set(particleService, mockPools);
		queuedField.set(particleService, mockQueuedEffects);

		ParticleEffectPool pool = mock(ParticleEffectPool.class);
		ParticleEffectPool.PooledEffect pooledEffect = mock(ParticleEffectPool.PooledEffect.class);
		when(mockPools.get(any(ParticleService.ParticleEffectType.class))).thenReturn(pool);
		when(pool.obtain()).thenReturn(pooledEffect);

		particleService.startEffect(ParticleService.ParticleEffectType.SUCCESS_EFFECT);
		assertFalse(mockQueuedEffects.isEmpty());


		particleService.stopEffectCategory(ParticleService.ENTITY_EFFECT);
		assertEquals(0, mockQueuedEffects.size());
	}

	@Test
	void testGetCategory() {
		ParticleService.ParticleEffectType particleEffectType = ParticleService.ParticleEffectType.RAIN;
		assertEquals(ParticleService.WEATHER_EVENT, particleEffectType.getCategory());
	}

	@Test
	void testGetEffect() throws IllegalAccessException {
		@SuppressWarnings("unchecked")
		EnumMap<ParticleService.ParticleEffectType, ParticleEffectPool> mockPools = mock(EnumMap.class);

		@SuppressWarnings("unchecked")
		ArrayList<ParticleEffectWrapper> mockQueuedEffects = mock(ArrayList.class);

		ParticleService particleService = new ParticleService();
		Field poolsField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("particleEffectPools"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);

		poolsField.setAccessible(true);
		poolsField.set(particleService, mockPools);

		ParticleEffectPool pool = mock(ParticleEffectPool.class);
		ParticleEffectPool.PooledEffect pooledEffect = mock(ParticleEffectPool.PooledEffect.class);
		when(mockPools.get(any(ParticleService.ParticleEffectType.class))).thenReturn(pool);
		when(pool.obtain()).thenReturn(pooledEffect);

		assertEquals(pooledEffect, particleService.getEffect(ParticleService.ParticleEffectType.RAIN));
	}


	@Test
	void testAddComponent() throws IllegalAccessException {

		ParticleEffectComponent component = new ParticleEffectComponent();

		@SuppressWarnings("unchecked")
		ArrayList<ParticleEffectComponent> mockedComponents = mock(ArrayList.class);

		ParticleService particleService = new ParticleService();
		Field componentsField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("effectComponents"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);

		componentsField.setAccessible(true);
		componentsField.set(particleService, mockedComponents);

		particleService.addComponent(component);

		verify(mockedComponents, times(1)).add(component);
	}

	@Test
	void testRemoveComponent() throws IllegalAccessException {

		ParticleEffectComponent component = new ParticleEffectComponent();

		@SuppressWarnings("unchecked")
		ArrayList<ParticleEffectComponent> mockedComponents = mock(ArrayList.class);

		ParticleService particleService = new ParticleService();
		Field componentsField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("effectComponents"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);

		componentsField.setAccessible(true);
		componentsField.set(particleService, mockedComponents);

		particleService.removeComponent(component);

		verify(mockedComponents, times(1)).remove(component);
	}

	@Test
	void testStartEffectAtPosition() throws IllegalAccessException {
		@SuppressWarnings("unchecked")
		EnumMap<ParticleService.ParticleEffectType, ParticleEffectPool> mockPools = mock(EnumMap.class);

		@SuppressWarnings("unchecked")
		ArrayList<ParticleEffectWrapper> positionalEffects = mock(ArrayList.class);

		ParticleService particleService = new ParticleService();
		Field poolsField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("particleEffectPools"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
		Field positionalEffectsField = ReflectionUtils.findFields(ParticleService.class, f -> f.getName().equals("positionalEffects"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);

		poolsField.setAccessible(true);
		positionalEffectsField.setAccessible(true);
		poolsField.set(particleService, mockPools);
		positionalEffectsField.set(particleService, positionalEffects);

		ParticleEffectPool pool = mock(ParticleEffectPool.class);
		ParticleEffectPool.PooledEffect pooledEffect = mock(ParticleEffectPool.PooledEffect.class);
		when(mockPools.get(any(ParticleService.ParticleEffectType.class))).thenReturn(pool);
		when(pool.obtain()).thenReturn(pooledEffect);

		Vector2 position = new Vector2(1, 0);
		particleService.startEffectAtPosition(ParticleService.ParticleEffectType.RAIN, position);

		verify(pooledEffect, times(1)).setPosition(position.x, position.y);
		verify(positionalEffects, times(1)).add(any(ParticleEffectWrapper.class));
		verify(pooledEffect, times(1)).start();
	}
}
