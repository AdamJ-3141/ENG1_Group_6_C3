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

        gameTimer = 300f; // 5 mins

        buildings = new ArrayList<>();

        // buildings.add(new Library(viewport, tiledMap)); // for testing purposes
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
        drawGui();
    }

    private void input() {
        for (Building building : buildings) {
            building.input();
        }
    }

    private void logic() {
        gameTimer -= Gdx.graphics.getDeltaTime();
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

    private void drawGui() {
        guiHandler.drawGUI();
    }

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
