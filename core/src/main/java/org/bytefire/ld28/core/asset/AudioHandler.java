package org.bytefire.ld28.core.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;

public class AudioHandler {

    HashMap<String, Sound> sounds;

    public AudioHandler(){
        sounds = new HashMap<String, Sound>();
    }

    public Sound get(Audio sfx){
        if (!sounds.containsKey(sfx.file.path())) sounds.put(sfx.file.path(), Gdx.audio.newSound(sfx.file));
        return sounds.get(sfx.file.path());
    }

    public long play(Audio sfx){
        return get(sfx).play();
    }

    public long loop(Audio sfx){
        return get(sfx).loop();
    }

    public void stop(Audio sfx){
        get(sfx).stop();
    }

    public void dispose(Audio sfx){
        get(sfx).dispose();
        sounds.remove(sfx.file.path());
    }

}
