package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MenuScreen implements Screen {

    final App game;
    public FitViewport viewport;
    private Stage stage;
    private Table table;
    private Skin skin;

    /**
     * initialises the attributes as required. sets the GUI's formatting
     *
     * @param game
     */
    public MenuScreen(App game) {
        this.game = game;
        viewport = new FitViewport(420, 200);
        stage = new Stage(viewport);
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.top();
        skin = new Skin(Gdx.files.internal("uiskin.json"));


        Image titleImage = new Image(new Texture(Gdx.files.internal("Title.png")));
        table.add(titleImage).width(407).height(83).padTop(10).padBottom(25);
        table.row();
        Label startLabel = new Label("Click to Start!", skin);
        startLabel.setFontScale(3);
        table.add(startLabel);
    }

    @Override
    public void show() {

    }

    /**
     * draws the screen every frame, and starts the game if the window is clicked
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.draw();
        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    /**
     * this method updates the camera and viewports when the window is resized to make sure everything scales correctly
     *
     * @param width - width of current window
     * @param height - height of current window
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
    }
}
