package io.github.unisim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Library extends Building {
    public Library(FitViewport viewport) {
        super(new Texture("library.png"), viewport);
    }
}
