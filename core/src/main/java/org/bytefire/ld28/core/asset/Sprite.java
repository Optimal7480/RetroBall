package org.bytefire.ld28.core.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public enum Sprite {

    SPLASH(Gdx.files.internal("studio.png"), 0, 0, 0),
    PLAYER(Gdx.files.internal("spritesheet.png"), 16, 16, 0),
    LINE(Gdx.files.internal("spritesheet.png"), 16, 8, 1),
    LINECAP(Gdx.files.internal("spritesheet.png"), 16, 8, 7),
    BOUNCE_ON(Gdx.files.internal("spritesheet.png"), 16, 16, 2),
    BOUNCE_OFF(Gdx.files.internal("spritesheet.png"), 16, 16, 7),
    MAGNET_ON(Gdx.files.internal("spritesheet.png"), 16, 16, 3),
    MAGNET_OFF(Gdx.files.internal("spritesheet.png"), 16, 16, 8),
    FLY_ON(Gdx.files.internal("spritesheet.png"), 16, 16, 4),
    FLY_OFF(Gdx.files.internal("spritesheet.png"), 16, 16, 9),
    GRAV_ON(Gdx.files.internal("spritesheet.png"), 16, 16, 5),
    GRAV_OFF(Gdx.files.internal("spritesheet.png"), 16, 16, 10),
    SIZE_ON(Gdx.files.internal("spritesheet.png"), 16, 16, 6),
    SIZE_OFF(Gdx.files.internal("spritesheet.png"), 16, 16, 11);

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
