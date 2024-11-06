package io.github.unisim;

import com.badlogic.gdx.graphics.Texture;

public class Library extends Building {
    public Library() {
        super(new Texture("library.png"));
        buildingSprite.setCenter(1,3);
    }
}
