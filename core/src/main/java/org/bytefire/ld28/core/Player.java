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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import org.bytefire.ld28.core.asset.Sprite;
import org.bytefire.ld28.core.asset.SpriteHandler;
import org.bytefire.ld28.core.screen.AbstractScreen;
import org.bytefire.ld28.core.screen.GameScreen;

public class Player extends Actor implements CollisionManager{

    private final Body body;
    private final Fixture fix;
    private final LD28 game;
    private final TextureRegion tex;

    public Player(LD28 game) {
        this.game = game;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(160, 200);
        body = ((GameScreen) game.getScreen()).getWorld().createBody(bodyDef);
        CircleShape circle = new CircleShape();
        circle.setRadius(5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 1.0f;
        fixtureDef.restitution = 0.1f;
        fix = body.createFixture(fixtureDef);
        circle.dispose();
        body.setLinearVelocity(new Vector2(64, -64));
        body.setUserData(this);

        tex = game.getSpriteHandler().getRegion(Sprite.PLAYER);
        ((AbstractScreen) game.getScreen()).getStage().addActor(this);
        setTouchable(Touchable.enabled);

    }

    @Override
    public void draw (SpriteBatch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(tex, getX() - (tex.getRegionWidth() / 2), getY() - (tex.getRegionHeight() / 2));

                SpriteHandler s = game.getSpriteHandler();
        float maxtime = 6f;
        float time = 2.5f;
        int height = s.getRegion(Sprite.FLY_OFF).getRegionHeight();
        int midpoint = Math.round(time / maxtime * height);

//        TextureRegion top = s.getRegion(Sprite.FLY_OFF);
//        top.setRegionHeight(height - midpoint);
//        TextureRegion bot = s.getRegion(Sprite.FLY_ON);
//        bot.setRegionY(bot.getRegionY() + height - midpoint);
//        bot.setRegionHeight(midpoint);
//
//        Color color = getColor();
//        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
//        batch.draw(top, getX() - (top.getRegionWidth() / 2), getY() - (height / 2) + midpoint);
//        batch.draw(bot, getX() - (bot.getRegionWidth() / 2), getY() - (height / 2));
    }

    @Override
    public void act(float delta){
        setX(body.getPosition().x);
        setY(body.getPosition().y);
    }

    @Override
    public void beginContact(Contact contact) {
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    @Override
    public Class getType() {return this.getClass();}

    public Body getBody() {
        return body;
    }

    public Fixture getFix() {
        return fix;
    }

    public Vector2 getPosition(){
        return body.getPosition();
    }
}
