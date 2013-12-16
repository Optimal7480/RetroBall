/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bytefire.ld28.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.ArrayList;
import org.bytefire.ld28.core.asset.Sprite;
import org.bytefire.ld28.core.screen.AbstractScreen;
import org.bytefire.ld28.core.screen.GameScreen;

public class DrawnStatic extends Actor implements CollisionManager{
    private static final long FADE_TIME = 4;

    private final ArrayList<Vector2> chain;
    private float length;
    private final ArrayList<Float> time;
    private final Body body;
    private Fixture fix;
    private boolean requiresUpdate;
    private ShapeRenderer shape;
    private LD28 game;

    public DrawnStatic(LD28 game){
        this.game = game;
        BodyDef chainBodyDef = new BodyDef();
        chainBodyDef.type = BodyDef.BodyType.StaticBody;
        chainBodyDef.position.set(0, 0);
        body = ((GameScreen) game.getScreen()).getWorld().createBody(chainBodyDef);
        body.setUserData(this);

        if (body.getFixtureList().size() > 0) body.getFixtureList().get(body.getFixtureList().size() - 1).setRestitution(2.0f);
        chain = new ArrayList<Vector2>();
        length = 0;
        time = new ArrayList<Float>();
        requiresUpdate = false;
        fix = null;
        shape = new ShapeRenderer();
        ((AbstractScreen) game.getScreen()).getStage().addActor(this);
        setColor(Color.RED);
    }

    @Override
    public void draw (SpriteBatch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        for (int i = 0; i < chain.size() - 1; i++){
            Vector2 start = chain.get(i).cpy();
            Vector2 end = chain.get(i + 1).cpy();
            Vector2 dir = new Vector2(start.x - end.x, start.y - end.y);
            batch.draw(
                game.getSpriteHandler().getRegion(Sprite.LINE),
                start.x - 8 - (MathUtils.cosDeg(dir.angle()) * 8),
                start.y - 8 - (MathUtils.sinDeg(dir.angle()) * 8),
                8f, 8f, 16f, dir.len(), 1f, 1f , dir.angle() + 90
            );
//            batch.draw(
//                game.getSpriteHandler().getRegion(Sprite.LINECAP),
//                start.x - 8 - (MathUtils.cosDeg(dir.angle()) * 8),
//                start.y - 8 - (MathUtils.sinDeg(dir.angle()) * 8),
//                8f, 8f, 16f, 8f, 1f, 1f , dir.angle() + 90
//            );
        }
    }

    @Override
    public void act(float delta){
        for (int i = 0; i < time.size(); i++){
            Float segtime = time.get(i);
            if (((GameScreen) game.getScreen()).getWorldTime() > segtime + FADE_TIME){
                //for (int ii = 0; ii < time.size() - i; i++) time.remove(i + ii);
                time.remove(i);
                chain.remove(i);
                if (chain.size() > 1) length -= chain.get(i).dst(chain.get(i + 1));
                else length = 0;
                requiresUpdate = true;
            }
        }
        if (requiresUpdate) update();

        setX(body.getPosition().x);
        setY(body.getPosition().y);
    }

    public ArrayList<Vector2> getChain() {
        return chain;
    }

    public void addPoint(Vector2 vector, float worldTime){
        if (chain.isEmpty()){
            chain.add(vector);
            time.add(worldTime);
        }
        float worldLength = ((GameScreen) game.getScreen()).getTotalPlatformLength();
        Vector2 segment = vector.cpy().sub(chain.get(chain.size() - 1).cpy());
        float segmentLength = Math.abs(segment.len());
        Vector2 lastPoint = chain.get(chain.size() -1);
        if (lastPoint.dst(vector.cpy()) > 16){
            if (segmentLength + worldLength < GameScreen.PLATFORM_CAP){
                chain.add(vector);
                time.add(worldTime);
                length += segmentLength;
            }
            else if (GameScreen.PLATFORM_CAP - worldLength > 1f){
                float targetLength = GameScreen.PLATFORM_CAP - worldLength;
                chain.add(lastPoint.cpy().add(segment.cpy().scl(targetLength / segmentLength)));
                time.add(worldTime);
                length += targetLength;
            }
            if (chain.size() >= 2) requiresUpdate = true;
        }
    }

    public float getLength() {
        return length;
    }

    public Body getBody() {
        return body;
    }

    public void update(){
        if (chain.size() == 1 && fix != null){
            body.destroyFixture(fix);
        }
        else if (chain.size() == 2) {
            EdgeShape staticEdge = new EdgeShape();
            Vector2[] vectorArray = new Vector2[chain.size()];
            staticEdge.set(chain.get(0), chain.get(1));
            if (fix != null) body.destroyFixture(fix);
            fix = body.createFixture(staticEdge, 0.0f);
            staticEdge.dispose();
        }
        else if (chain.size() > 2) {
            ChainShape staticChain = new ChainShape();
            Vector2[] vectorArray = new Vector2[chain.size()];
            staticChain.createChain(chain.toArray(vectorArray));
            body.destroyFixture(fix);
            if (fix != null) fix = body.createFixture(staticChain, 0.0f);
            staticChain.dispose();
        }
        requiresUpdate = false;
    }

    @Override
    public void beginContact(Contact contact) {
        Class type = ((CollisionManager) contact.getFixtureB().getBody().getUserData()).getType();
        if(type == Player.class) {
            Body body = contact.getFixtureB().getBody();
            Vector2 velocity = body.getLinearVelocity().cpy();
            if (velocity.y < 1) body.applyForceToCenter(velocity.cpy().scl(25f), true);
        }
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
}
