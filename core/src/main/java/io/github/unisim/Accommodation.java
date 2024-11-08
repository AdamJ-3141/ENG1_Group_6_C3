package io.github.unisim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Accommodation extends Building {
    public Accommodation(FitViewport viewport, TiledMap tiledMap) {
        super(new Texture("accommodation.png"), viewport, tiledMap);
    }
}
