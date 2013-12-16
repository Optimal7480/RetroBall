/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bytefire.ld28.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
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
public class Upgrade extends Actor implements CollisionManager{
    private final LD28 game;
    private final TextureRegion tex;
    private final Body body;
    public enum Type { GRAVITY, GROW, BOUNCE, MAGNET, FLY }
    private Type type;
    public Random rand;

    public Upgrade(LD28 game, Type type){
        rand = new Random(System.nanoTime());
        this.game = game;
        this.type = type;
        tex = game.getSpriteHandler().getRegion(Sprite.PLAYER);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(((GameScreen) game.getScreen()).getPlayer().getX() + GameScreen.WINDOW_WIDTH, ((GameScreen) game.getScreen()).getPlayer().getY());
        body = ((GameScreen) game.getScreen()).getWorld().createBody(bodyDef);
        CircleShape circle = new CircleShape();
        circle.setRadius(5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 1.0f;
        fixtureDef.restitution = 0.1f;
        body.createFixture(fixtureDef);
        circle.dispose();
        body.setUserData(this);
        ((AbstractScreen) game.getScreen()).getStage().addActor(this);
        setTouchable(Touchable.enabled);
    }

    @Override
    public void draw (SpriteBatch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(tex, getX() - (tex.getRegionWidth() / 2), getY() - (tex.getRegionHeight() / 2));
    }

    @Override
    public void act(float delta){
        setX(body.getPosition().x);
        setY(body.getPosition().y);
        if (((GameScreen) game.getScreen()).getPlayer().getPosition().dst(getX(), getY()) < 6){
            switch (type){
                case BOUNCE: ((GameScreen) game.getScreen()).getPlayer().setBounce(Player.MAX_BOUNCE);
                    break;
                case GROW: ((GameScreen) game.getScreen()).getPlayer().setGrow(Player.MAX_GROW);
                    break;
                case GRAVITY: ((GameScreen) game.getScreen()).getPlayer().setGravity(Player.MAX_GRAVITY);
                    break;
                default: break;
            }
        }
    }

    @Override
    public boolean remove(){
        super.remove();
        body.destroyFixture(body.getFixtureList().get(0));
        return false;
    }


    @Override
    public void beginContact(Contact contact) {}

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    @Override
    public Class getType() { return this.getClass(); }

    public Type getUpgradeType() {
        return type;
    }



}
