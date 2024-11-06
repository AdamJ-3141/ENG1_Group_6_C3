package io.github.unisim;

import com.badlogic.gdx.graphics.Texture;

public class Lecture extends Building {
    public Lecture() {
        super(new Texture("lecture.png"));
        buildingSprite.setCenter(1,2);
    }
}
