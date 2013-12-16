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
    private static final long FADE_TIME = 2;

    private final ArrayList<Vector2> chain;
    private double length;
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

        if(body.getFixtureList().size()>0) body.getFixtureList().get(body.getFixtureList().size()-1).setRestitution(1.0f);
        chain = new ArrayList<Vector2>();
        length = 0;
        time = new ArrayList<Float>();
        requiresUpdate = false;
        fix = null;
        shape = new ShapeRenderer();
        ((AbstractScreen) game.getScreen()).getStage().addActor(this);
        setColor(Color.WHITE);
    }

    @Override
    public void draw (SpriteBatch batch, float parentAlpha) {
//        batch.end();
//        Gdx.gl20.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        shape.setProjectionMatrix(batch.getProjectionMatrix().cpy());
//        shape.begin(ShapeRenderer.ShapeType.Line);
//        Color color = getColor();
//        Vector2 lastv = null;
//        for (Vector2 vector : chain){
//            if (lastv == null) lastv = vector;
//            else {
//                shape.setColor(color.r, color.g, color.b, 0.01f);
//                Gdx.gl20.glLineWidth(9);
//                shape.flush();
//                shape.line(lastv, vector);
//                shape.setColor(color.r, color.g, color.b, 0.03f);
//                Gdx.gl20.glLineWidth(7);
//                shape.flush();
//                shape.line(lastv, vector);
//                shape.setColor(color.r, color.g, color.b, 0.12f);
//                Gdx.gl20.glLineWidth(5);
//                shape.flush();
//                shape.line(lastv, vector);
//                shape.setColor(color.r, color.g, color.b, 0.16f);
//                Gdx.gl20.glLineWidth(3);
//                shape.flush();
//                shape.line(lastv, vector);
//                lastv = vector;
//            }
//        }
//        shape.end();
//        Gdx.gl20.glDisable(GL20.GL_BLEND);
//        batch.begin();
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        for (int i = 0; i < chain.size()-1; i++){
            Vector2 start = chain.get(i).cpy();
            Vector2 dir = new Vector2(start.x-chain.get(i+1).x, start.y-chain.get(i+1).y);
            batch.draw(
                game.getSpriteHandler().getRegion(Sprite.LINE),
                start.x-8, start.y-8,
                8f, 8f, 16f, dir.len(), 1f, 1f , dir.angle()+90
            );
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
        //Pass Checks
        if (chain.size() > 0) {
            Vector2 lastPoint = chain.get(chain.size() -1);
            if (lastPoint.dst2(vector) > 16 * 16) addVector(vector, worldTime);
        }
        else addVector(vector, worldTime);
        if (chain.size() >= 1) requiresUpdate = true;
    }

    private void addVector(Vector2 vector, float worldTime){
        //Add Vector
        chain.add(vector);
        //Add time
        time.add(worldTime);
        //Add length
        length += vector.dst(0, 0);
    }

    public double getLength() {
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
            if (velocity.y < 0.5) body.applyForceToCenter(velocity.cpy().scl(100f), true);
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
