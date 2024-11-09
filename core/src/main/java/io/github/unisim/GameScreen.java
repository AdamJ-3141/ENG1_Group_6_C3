package io.github.unisim;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {
    final App game;

    private final TiledMap tiledMap;
    private final TiledMapRenderer tiledMapRenderer;

    public GUI guiHandler;
    public OrthographicCamera camera;
    public SpriteBatch spriteBatch;
    public FitViewport viewport;

    public FitViewport guiViewport;

    public Boolean gameRunning = false;
    public Boolean initialPause = true;
    public Boolean placingBuilding = false;
    public float gameTimer;

    ArrayList<Building> buildings;

    final float WORLD_WIDTH;
    final float WORLD_HEIGHT;

    /**
     * this method initialises the attributes required for the gameScreen.
     * set the viewport and camera for this screen
     * creates the tileMap
     * @param game
     */
    public GameScreen(final App game) {
        this.game = game;
        spriteBatch = new SpriteBatch();

        String mapPath = "defaultMap.tmx";
        tiledMap = new TmxMapLoader().load(mapPath);

        float unitScale = 1 / 32f;
        WORLD_WIDTH = tiledMap.getProperties().get("width", Integer.class);
        WORLD_HEIGHT = tiledMap.getProperties().get("height", Integer.class);

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT + 12, camera);

        guiViewport = new FitViewport(20 * WORLD_WIDTH, 20 * (WORLD_HEIGHT + 12));
        guiHandler = new GUI(guiViewport, this);

        gameTimer = 300f; // run time for the game, current 5 mins

        buildings = new ArrayList<>();
    }

    /**
     * adds the building passed to the list of buildings currently on the map
     *
     * @param building - The building object to be added to the map
     */
    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    /**
     * deletes any buildings that are currently moving
     */
    public void removeMovingBuilding(){
        for (Building building : buildings) {
            if (building.isMoving()) {
                buildings.remove(building);
                placingBuilding = false;
                break;
            }
        }
    }

    @Override
    public void show() {
    }

    /**
     * this method controls how the game runs every frame
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
        drawGui();
    }

    /**
     * this method ensures that any inputs since the last frame are handled
     */
    private void input() {
        for (Building building : buildings) {
            building.input();
        }
    }

    /**
     * in this method the gameTimer is updated to and the current amount of buildings on the map
     * as well as calling the building object's logic method
     */
    private void logic() {
        if (gameRunning) {
            gameTimer -= Gdx.graphics.getDeltaTime();
        }
        for (Building building : buildings) {
            building.logic();
        }
        guiHandler.updateTimeRemaining((int) gameTimer);
        int buildingAmount = 0;
        for (Building building : buildings) {
            if (!building.isMoving()) {
                buildingAmount += 1;
            }
        }
        guiHandler.setBuildingCount(buildingAmount);
    }

    /**
     * this method will draw the playable map to the screen. rendering both the tileMap and the buildings that are on
     * the screen
     */
    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        camera.update();
        tiledMapRenderer.setView(camera.combined, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        tiledMapRenderer.render();

        viewport.apply();
        spriteBatch.setProjectionMatrix(camera.combined); // magic dont remove
        spriteBatch.begin();
        for (Building building : buildings) {
            building.draw(spriteBatch);
        }
        spriteBatch.end();
    }

    /**
     * draws the GUI for this screen
     */
    private void drawGui() {
        guiHandler.drawGUI();
    }

    /**
     * this method updates the camera and viewports when the window is resized to make sure everything scales correctly
     *
     * @param width - width of current window
     * @param height - height of current window
     */
    @Override
    public void resize(int width, int height) {
        camera.update();
        viewport.update(width, height, true);
        guiViewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        tiledMap.dispose();

    }
}
