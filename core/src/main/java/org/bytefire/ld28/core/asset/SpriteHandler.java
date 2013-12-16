package org.bytefire.ld28.core.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteHandler {

    private Texture texture;
    private String ref;

    public SpriteHandler(){
        texture = null;
        ref = null;
    }

    public void load(String name){
        if (texture != null) texture.dispose();
        FileHandle file = Gdx.files.internal(name);
        ref = file.path();
        texture = new Texture(file);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public void load(FileHandle file){
        if (texture != null) texture.dispose();
        ref = file.path();
        texture = new Texture(file);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public TextureRegion getRegion(int width, int height, int id){
        int gridWidth = texture.getWidth() / width;
        int gridHeight = texture.getHeight() / height;
        return new TextureRegion(texture, (id % gridWidth) * width, (id / gridHeight) * height, width, height);
    }

    public TextureRegion getRegion(Sprite tex){
        if (!tex.file.path().equals(ref)) load(tex.file);
        return getRegion(tex.width, tex.height, tex.id);
    }

    public Texture getTexture(){
        return texture;
    }

    public Texture getTexture(String name){
        load(name);
        return getTexture();
    }

    public Texture getTexture(Sprite tex){
        if (!tex.file.path().equals(ref)) load(tex.file);
        return texture;
    }

    public void dispose(){
        texture.dispose();
    }
}
