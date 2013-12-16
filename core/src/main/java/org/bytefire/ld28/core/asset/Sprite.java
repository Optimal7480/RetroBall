package org.bytefire.ld28.core.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public enum Sprite {

    SPLASH(Gdx.files.internal("studio.png"), 0, 0, 0),
    PLAYER(Gdx.files.internal("spritesheet.png"), 16, 16, 0);

    public final FileHandle file;
    public final int width;
    public final int height;
    public final int id;

    private Sprite(FileHandle file, int width, int height, int id){
        this.file = file;
        this.width = width;
        this.height = height;
        this.id = id;
    }
}
