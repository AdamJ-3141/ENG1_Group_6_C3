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
    private final Stage stage;

    private final Label buildingCount;
    private final Label timeRemaining;
    private final Label satisfaction;

    private final TextButton pauseButton;

    /**
     * initialises the attributes and sets the layout for the GUI
     *
     * @param viewport - the viewport for the GUI to use
     * @param screen - current screen for the game
     */
    public GUI(FitViewport viewport, GameScreen screen) {
        this.viewport = viewport;
        this.screen = screen;
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        //table.setDebug(true); // turn on all debug lines (table, cell, and widget)
        table.top();
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

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

        //  creates the pause button and makes it pause the game when clicked
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

        // creates the building buttons
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

    /**
     * this method will create the button for each type of building using the parameters to differ there functionality
     * each of the buttons created will have an up and down texture.
     * each of the buttons will have an associated functionality that will spawn the appropriate type of building unless
     * there is a building already being moved in existence. Then the method will delete that instead building.
     *
     * @param upTexture - texture to use for when the button is in the up state
     * @param downTexture - texture to use for when the button is in the down state
     * @param buildingType - the type of building the button is representing
     * @return ImageButton object that is the button
     */
    private ImageButton getBuildingButton(String upTexture, String downTexture, String buildingType) {
        Texture UButtonTexture = new Texture(Gdx.files.internal(upTexture));
        Texture DButtonTexture = new Texture(Gdx.files.internal(downTexture));
        ImageButton buildingButton = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(UButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(DButtonTexture)));
        buildingButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if (!screen.placingBuilding) {
                    screen.placingBuilding = true;
                    switch (buildingType) {
                        case "Accommodation":
                            screen.addBuilding(new Accommodation(screen.viewport, screen.getTiledMap(), screen));
                            break;
                        case "Library":
                            screen.addBuilding(new Library(screen.viewport, screen.getTiledMap(), screen));
                            break;
                        case "Food":
                            screen.addBuilding(new Food(screen.viewport, screen.getTiledMap(), screen));
                            break;
                        case "Lecture":
                            screen.addBuilding(new Lecture(screen.viewport, screen.getTiledMap(), screen));
                            break;
                        default:
                            System.out.println("Unknown building type: " + buildingType);
                    }
                } else {
                    screen.removeMovingBuilding();
                }
            }
        });
        return buildingButton;
    }

    /**
     * draws the GUI onto the screen, changing the pause button's text depending on the status of the game
     */
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

    /**
     * this method sets the number to be displayed on the GUI statistic for number of buildings
     *
     * @param buildingCount - current number of buildings placed
     */
    public void setBuildingCount(int buildingCount) {
        this.buildingCount.setText(String.valueOf(buildingCount));
    }

    /**
     * this method will convert the time remaining in seconds for the game and convert it to m:ss format and set this
     * as the value to be displayed in time remaining
     *
     * @param timeRemaining - time remaining in the game in seconds
     */
    public void updateTimeRemaining(int timeRemaining) {
        String minutes = String.valueOf(timeRemaining / 60);
        String seconds = String.valueOf(timeRemaining % 60);
        if (seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        this.timeRemaining.setText(minutes +":"+ seconds);
    }

    /**
     * this method sets the number to be displayed on the GUI statistic for satisfaction
     *
     * @param satisfaction - satisfaction
     */
    public void setSatisfaction(int satisfaction) {
        this.satisfaction.setText(String.valueOf(satisfaction));
    }

}
