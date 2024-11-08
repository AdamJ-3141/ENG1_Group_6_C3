package io.github.unisim;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {
    final App game;

    private String mapPath;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    public OrthographicCamera camera;
    public SpriteBatch spriteBatch;
    public FitViewport viewport;

    public FitViewport guiViewport;
    public Stage stage;
    public Table table;
    public Skin skin;

    ArrayList<Building> buildings;

    final float WORLD_WIDTH;
    final float WORLD_HEIGHT;

    public GameScreen(final App game) {
        this.game = game;
        spriteBatch = new SpriteBatch();

        mapPath = "defaultMap.tmx";
        tiledMap = new TmxMapLoader().load(mapPath);

        float unitScale = 1 / 32f;
        WORLD_WIDTH = tiledMap.getProperties().get("width", Integer.class);
        WORLD_HEIGHT = tiledMap.getProperties().get("height", Integer.class);

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        guiViewport = new FitViewport(10*WORLD_WIDTH, 10*WORLD_HEIGHT);

        stage = new Stage(guiViewport);
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.setDebug(true); // turn on all debug lines (table, cell, and widget)
        table.top();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Label testLabel = new Label("Hello World", skin);
        table.add(testLabel);
        table.row();
        Label testLabel2 = new Label("Goodbye World", skin);
        table.add(testLabel2);

        buildings = new ArrayList<>();

        buildings.add(new Library(viewport)); // for testing purposes
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
        for (Building building : buildings) {
            building.logic();
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        camera.update();
        tiledMapRenderer.setView(camera);
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
        guiViewport.apply();
        stage.draw();
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
