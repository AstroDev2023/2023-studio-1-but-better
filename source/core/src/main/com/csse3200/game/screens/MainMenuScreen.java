package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.maingame.mainmenu.MainMenuActions;
import com.csse3200.game.components.maingame.mainmenu.MainMenuDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the main menu.
 */
public class MainMenuScreen extends ScreenAdapter {
	private static final Logger logger = LoggerFactory.getLogger(MainMenuScreen.class);
	private final GdxGame game;
	private final Renderer renderer;
	public static final int FRAME_COUNT = 48;
	private static final String[] mainMenuTextures = {"images/wallpaper.png", "images/title.png"};
	private static final TextureRegionDrawable[] transitionTextures = new TextureRegionDrawable[FRAME_COUNT];
	private static final String ANIMATION_PREFIX = "images/menu_animations/menu_animations";

	public MainMenuScreen(GdxGame game) {
		this.game = game;
		logger.debug("Initialising main menu screen services");
		ServiceLocator.registerTimeSource(new GameTime());
		ServiceLocator.registerInputService(new InputService());
		ServiceLocator.registerResourceService(new ResourceService());
		ServiceLocator.registerEntityService(new EntityService());
		ServiceLocator.registerRenderService(new RenderService());
		ServiceLocator.registerTimeService(new TimeService());
		ServiceLocator.registerSaveLoadService(new SaveLoadService());
		renderer = RenderFactory.createRenderer();
		loadAssets();
		createUI();
	}

	@Override
	public void render(float delta) {
		ServiceLocator.getEntityService().update();
		ServiceLocator.getTimeService().update();
		renderer.render();
	}


	@Override
	public void resize(int width, int height) {
		renderer.resize(width, height);
		logger.trace("Resized renderer: ({} x {})", width, height);
	}

	@Override
	public void pause() {
		logger.info("Game paused");
	}

	@Override
	public void resume() {
		logger.info("Game resumed");
	}

	@Override
	public void dispose() {
		logger.debug("Disposing main menu screen");

		renderer.dispose();
		unloadAssets();
		ServiceLocator.getRenderService().dispose();
		ServiceLocator.getEntityService().dispose();

		ServiceLocator.clear();
	}

	private void loadAssets() {
		logger.debug("Loading assets");
		ResourceService resourceService = ServiceLocator.getResourceService();
		resourceService.loadTextures(mainMenuTextures);
		resourceService.loadTextureAtlases(new String[]{"images/wallpaper.atlas"});
		ServiceLocator.getResourceService().loadAll();
		TextureAtlas atlas = new TextureAtlas("images/wallpaper.atlas");

		for (int i = 0; i < FRAME_COUNT; i++) {
			transitionTextures[i] = new TextureRegionDrawable(atlas.findRegion("default", i));
		}
	}

	private void unloadAssets() {
		logger.debug("Unloading assets");
		ResourceService resourceService = ServiceLocator.getResourceService();
		resourceService.unloadAssets(mainMenuTextures);
		resourceService.unloadAssets(new String[]{"images/wallpaper.atlas"});
	}

	/**
	 * Creates the main menu's ui including components for rendering ui elements to the screen and
	 * capturing and handling ui input.
	 */
	private void createUI() {
		logger.debug("Creating ui");
		Stage stage = ServiceLocator.getRenderService().getStage();
		Entity ui = new Entity();
		ui.addComponent(new MainMenuDisplay())
				.addComponent(new InputDecorator(stage, 10))
				.addComponent(new MainMenuActions(game));
		ServiceLocator.getEntityService().register(ui);
	}

	/**
	 * Get the transition textures for control screen
	 *
	 * @return the transition textures
	 */
	public static TextureRegionDrawable[] getTransitionTextures() {
		return transitionTextures;
	}
}

