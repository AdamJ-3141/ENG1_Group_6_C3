package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GUI {

    private final FitViewport viewport;
    private Stage stage;
    private Table table;
    private Skin skin;

    public GUI(FitViewport viewport) {
        this.viewport = viewport;
        stage = new Stage(viewport);
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
    }

    public void drawGUI() {
        viewport.apply();
        stage.draw();
    }
}
