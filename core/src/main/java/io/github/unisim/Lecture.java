package io.github.unisim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Lecture extends Building {
    /**
     * initialises the attributes
     *
     * @param viewport - the viewport for the game, sets the scale for the building's methods
     * @param tiledMap - the tileMap of the game allows for checks to be made on the types of tiles
     */
    public Lecture(FitViewport viewport, TiledMap tiledMap) {
        super(viewport, tiledMap);
    }

    /**
     * sets the texture of the building
     */
    @Override
    void setTexture() {
        buildingTexture = new Texture("lecture.png");
    }
}
