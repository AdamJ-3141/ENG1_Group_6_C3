package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GUI {

    private final FitViewport viewport;
    private final GameScreen screen;
    private Stage stage;
    private Table table;
    private Skin skin;

    private Label buildingCount;
    private Label timeRemaining;
    private Label satisfaction;

    private TextButton pauseButton;

    public GUI(FitViewport viewport, GameScreen screen) {
        this.viewport = viewport;
        this.screen = screen;
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        //table.setDebug(true); // turn on all debug lines (table, cell, and widget)
        table.top();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Image titleImage = new Image(new Texture(Gdx.files.internal("Title.png")));
        table.add(titleImage).width(407).height(83).padTop(10).padBottom(10).colspan(4);
        table.row();
        Label buildingCountLabel = new Label("Building Count:", skin);
        table.add(buildingCountLabel).right();
        buildingCount = new Label("0", skin);
        table.add(buildingCount).left();
        Label satisfactionLabel = new Label("Current Satisfaction:", skin);
        table.add(satisfactionLabel).right();
        satisfaction = new Label("0%", skin);
        table.add(satisfaction).left();
        table.row();
        Label timeRemainingLabel = new Label("Time Remaining:", skin);
        table.add(timeRemainingLabel).right();
        timeRemaining = new Label("05:00", skin);
        table.add(timeRemaining).left();


        pauseButton = new TextButton("Pause", skin);
        pauseButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if (screen.initialPause) {
                    screen.initialPause = false;
                }
                screen.gameRunning = !screen.gameRunning;
            }
        });
        table.add(pauseButton).width(200).height(30).colspan(2);
        table.row();



    }

    public void drawGUI() {
        viewport.apply();
        if (screen.gameRunning) {
            pauseButton.setText("Pause");
        } else {
            if (screen.initialPause) {
                pauseButton.setText("Start");
            } else {
                pauseButton.setText("Resume");
            }
        }
        stage.draw();
        stage.act();
    }

    public void setBuildingCount(int buildingCount) {
        this.buildingCount.setText(String.valueOf(buildingCount));
    }

    public void updateTimeRemaining(int timeRemaining) {
        this.timeRemaining.setText(String.valueOf(timeRemaining));
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction.setText(String.valueOf(satisfaction));
    }

}
