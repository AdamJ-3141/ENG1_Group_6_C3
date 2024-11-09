package io.github.unisim;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Accommodation extends Building {
    /**
     * initialises the attributes
     *
     * @param viewport - the viewport for the game, sets the scale for the building's methods
     * @param tiledMap - the tileMap of the game allows for checks to be made on the types of tiles
     */
    public Accommodation(FitViewport viewport, TiledMap tiledMap, GameScreen gameScreen) {
        super(viewport, tiledMap, gameScreen);
    }

    /**
     * sets the texture of the building
     */
    @Override
    void setTexture() {
        buildingTexture = new Texture("accommodation.png");
    }
}
