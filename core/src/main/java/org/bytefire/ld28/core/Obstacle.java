/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bytefire.ld28.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import java.util.Random;
import org.bytefire.ld28.core.asset.Sprite;
import org.bytefire.ld28.core.screen.AbstractScreen;
import org.bytefire.ld28.core.screen.GameScreen;

/**
 *
 * @author timn
 */
public class Obstacle extends Actor implements CollisionManager{
    
    private final LD28 game;
    private final Body body;
    private Random rand;
    private final TextureRegion tex;
    
    public Obstacle(LD28 game){
        this.game = game;
        
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        rand = new Random();
        bodyDef.position.set(((GameScreen) game.getScreen()).getPlayer().getX() + GameScreen.WINDOW_WIDTH, ((GameScreen) game.getScreen()).getPlayer().getY() + (rand.nextInt(150)));
        body = ((GameScreen) game.getScreen()).getWorld().createBody(bodyDef);
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        polygonShape.//set(new Vector2(getX(), getY()), new Vector2(getX(), getY() + 22));
            set(new Vector2[]{ new Vector2(getX()-2, getY()-11),
            new Vector2(getX() + 2, getY()-11),
            new Vector2(getX() + 2, getY() + 11),
            new Vector2(getX() - 2, getY() + 11)});
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 1.0f;
        fixtureDef.restitution = 2.0f;
        body.createFixture(fixtureDef);
        
        body.setUserData(this);
        
        setTouchable(Touchable.enabled);
        tex = game.getSpriteHandler().getRegion(Sprite.WALL);
        
        ((AbstractScreen) game.getScreen()).getStage().addActor(this);
        polygonShape.dispose();
    }
    
    @Override
    public void draw (SpriteBatch batch, float parentAlpha) {
        Color color = ((GameScreen) game.getScreen()).getGlobalColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(tex, getX() - (tex.getRegionWidth() / 2), getY() - (tex.getRegionHeight() / 2));
    }

    @Override
    public void act(float delta){
        setX(body.getPosition().x);
        setY(body.getPosition().y);
        }

    @Override
    public void beginContact(Contact contact) {}

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    @Override
    public Class getType() {
        return this.getClass();
    }
    
}
