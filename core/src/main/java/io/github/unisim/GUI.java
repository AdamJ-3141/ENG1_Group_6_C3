package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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

        Table buildingButtonTable = new Table();

        ImageButton accommodationButton = getBuildingButton(
                "Accommodation_Button_Up.png",
                "Accommodation_Button_Down.png",
                "Accommodation"
                );
        buildingButtonTable.add(accommodationButton).width(75).height(75).uniform().pad(5);
        ImageButton foodButton = getBuildingButton(
                "Food_Button_Up.png",
                "Food_Button_Down.png",
                "Food"
        );
        buildingButtonTable.add(foodButton).width(75).height(75).uniform().pad(5);
        ImageButton lectureButton = getBuildingButton(
                "Lecture_Button_Up.png",
                "Lecture_Button_Down.png",
                "Lecture"
        );
        buildingButtonTable.add(lectureButton).width(75).height(75).uniform().pad(5);
        ImageButton libraryButton = getBuildingButton(
                "Library_Button_Up.png",
                "Library_Button_Down.png",
                "Library"
        );
        buildingButtonTable.add(libraryButton).width(75).height(75).uniform().pad(5);
        table.add(buildingButtonTable).colspan(4);
    }

    private ImageButton getBuildingButton(String upTexture, String downTexture, String buildingType) {
        Texture UButtonTexture = new Texture(Gdx.files.internal(upTexture));
        Texture DButtonTexture = new Texture(Gdx.files.internal(downTexture));
        ImageButton buildingButton = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(UButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(DButtonTexture)));
        buildingButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                switch (buildingType) {
                    case "Accommodation":
                        screen.addBuilding(new Accommodation(screen.viewport, screen.getTiledMap()));
                        break;
                    case "Library":
                        screen.addBuilding(new Library(screen.viewport, screen.getTiledMap()));
                        break;
                    case "Food":
                        screen.addBuilding(new Food(screen.viewport, screen.getTiledMap()));
                        break;
                    case "Lecture":
                        screen.addBuilding(new Lecture(screen.viewport, screen.getTiledMap()));
                        break;
                    default:
                        System.out.println("Unknown building type: " + buildingType);
                }
            }
        });
        return buildingButton;
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
