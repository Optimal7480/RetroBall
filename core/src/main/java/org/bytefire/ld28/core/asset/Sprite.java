package org.bytefire.ld28.core.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public enum Sprite {

    SPLASH(Gdx.files.internal("studio.png"), 0, 0, 0);
//    PLAYER(Gdx.files.internal("level.png"), 32, 32, 64),
//    ENEMY(Gdx.files.internal("level.png"), 32, 32, 65),
//    ALLY_HEAVY(Gdx.files.internal("level.png"), 64, 64, 17),
//    ENEMY_HEAVY(Gdx.files.internal("level.png"), 64, 64, 18),
//    ARM(Gdx.files.internal("level.png"), 32, 32, 80),
//    HEAVY_ARM(Gdx.files.internal("level.png"), 32, 32, 81),
//    SHOT(Gdx.files.internal("level.png"), 16, 16, 384),
//    HEAVY_SHOT(Gdx.files.internal("level.png"), 16, 16, 385),
//    ALLY_HEAD(Gdx.files.internal("level.png"), 16, 16, 386),
//    ENEMY_HEAD(Gdx.files.internal("level.png"), 16, 16, 387),
//    STARS(Gdx.files.internal("level.png"), 128, 128, 6),
//    STARS_ALT(Gdx.files.internal("level.png"), 128, 128, 7),
//    MOON(Gdx.files.internal("level.png"), 128, 128, 2),
//    MOON_ALT(Gdx.files.internal("level.png"), 128, 128, 3),
//    DEFENCE(Gdx.files.internal("level.png"), 176, 128, 0),
//    BASE(Gdx.files.internal("level.png"), 176, 128, 0);

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
