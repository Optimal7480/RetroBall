package org.bytefire.ld28.core.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public enum Audio {

    SHOOT(Gdx.files.internal("shoot.wav"));
//    HIT(Gdx.files.internal("hit.wav")),
//    FLY(Gdx.files.internal("fly.wav")),
//    HEAVY_SHOOT(Gdx.files.internal("heavyshoot.wav")),
//    FLY_LOOP(Gdx.files.internal("flyloop.wav"));

    public final FileHandle file;

    private Audio(FileHandle file){
        this.file = file;
    }
}
