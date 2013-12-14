package org.bytefire.ld28.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.bytefire.ld28.core.ld28;

public abstract class AbstractScreen implements Screen{

    protected final ld28 game;
    protected final Stage stage;

    public AbstractScreen(ld28 game){
        this.game = game;
        this.stage = new Stage();
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0F, 0F, 0F, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(width, height, false);
    }

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
    
    public Stage getStage(){
        return stage;
    }
}
