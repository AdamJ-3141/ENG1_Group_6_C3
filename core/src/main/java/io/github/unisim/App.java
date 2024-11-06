package io.github.unisim;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class App extends ApplicationAdapter {
    SpriteBatch spriteBatch;
    Building theAccom;
    FitViewport viewport;
    ArrayList<Building> buildings;

import com.badlogic.gdx.Game;


/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class App extends Game {
    @Override
    public void create() {
// start changes
        buildings = new ArrayList<>();
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
        buildings.add(new Accommodation());
        buildings.add(new Food());
        buildings.add(new Lecture());
        buildings.add(new Library());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
    }

    @Override
    public void render() {
        for (Building building : buildings) {
            building.input(viewport);
        }
        logic();
        draw();
    }

    public void logic() {
        for (Building building : buildings) {
            building.logic(viewport);
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.WHITE);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        for (Building building : buildings) {
            building.draw(spriteBatch);
        }

        spriteBatch.end();
// end changes
        this.setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

    }

}
