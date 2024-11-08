package io.github.unisim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Library extends Building {
    public Library(FitViewport viewport, TiledMap tiledMap) {
        super(new Texture("library.png"), viewport, tiledMap);
    }
}
