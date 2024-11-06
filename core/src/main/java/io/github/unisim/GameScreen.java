package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final App game;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    public GameScreen(final App game) {
        this.game = game;

        // load images

        // load sounds

        tiledMap = new TmxMapLoader().load(game.mapPath);
        float unitScale = 1 / 32f;
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
    }

    private void input() {

    }

    private void logic() {
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        game.camera.update();
        tiledMapRenderer.setView(game.camera);
        tiledMapRenderer.render();

    }

    @Override
    public void resize(int width, int height) {
        game.camera.update();
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resume'");
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hide'");
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
    }
}
