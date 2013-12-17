package org.bytefire.ld28.core;

import com.badlogic.gdx.Game;
import org.bytefire.ld28.core.asset.SpriteHandler;
import org.bytefire.ld28.core.asset.AudioHandler;
import org.bytefire.ld28.core.screen.AbstractScreen;
import org.bytefire.ld28.core.screen.GameScreen;
import org.bytefire.ld28.core.screen.SplashScreen;
//import org.bytefire.LD28.core.screen.GameScreen;

public class LD28 extends Game {
    private static final boolean DEBUG_MODE = false;
    private final SpriteHandler texture;
    private final AudioHandler sfx;

    public LD28(){
        super();
        texture = new SpriteHandler();
        sfx = new AudioHandler();
    }

    public AbstractScreen getSplashScreen(){
        if (DEBUG_MODE) return new GameScreen(this);
        else return new SplashScreen(this);
    }

    public SpriteHandler getSpriteHandler(){
        return texture;
    }

    public AudioHandler getAudioHandler(){
        return sfx;
    }

    @Override
    public void create () {
        setScreen(getSplashScreen());
    }
}
