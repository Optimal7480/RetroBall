package org.bytefire.ld28.core;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import org.bytefire.ld28.core.asset.SpriteHandler;
import org.bytefire.ld28.core.asset.AudioHandler;
import org.bytefire.ld28.core.screen.AbstractScreen;
import org.bytefire.ld28.core.screen.SplashScreen;
//import org.bytefire.ld28.core.screen.GameScreen;

public class ld28 extends Game {
    private final SpriteHandler texture;
    private final AudioHandler sfx; 

    public ld28(){
        super();
        texture = new SpriteHandler();
        sfx = new AudioHandler();
    }

    public AbstractScreen getSplashScreen(){ 
        return new SplashScreen(this);
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
