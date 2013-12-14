/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bytefire.ld28.core.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytefire.ld28.core.LD28;

public class GameScreen extends AbstractScreen{
    private static final int WINDOW_WIDTH = 640;
    private static final int WINDOW_HEIGHT = 480;
    private static final float FRAME_GOAL = 1/60f;

    private OrthographicCamera cam;
    private World world;
    private Box2DDebugRenderer debugRender;

    public GameScreen(LD28 ld28){
        super(ld28);
        world = new World(new Vector2(0, -64), true);
        debugRender = new Box2DDebugRenderer();
    }

    @Override
    public void render(float delta){
        super.render(delta);

        debugRender.render(world, cam.combined);

        //if (delta < FRAME_GOAL) try {
        //    Thread.sleep((long) ((FRAME_GOAL - delta) * 1000));
        //} catch (InterruptedException ex) {
        //    Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        //}
        world.step(FRAME_GOAL, 6, 2);
    }

    @Override
    public void show(){
        super.show();
        cam = new OrthographicCamera(WINDOW_WIDTH, WINDOW_HEIGHT);
        //cam.zoom = 0.25F;
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyType.DynamicBody;
        // Set our body's starting position in the world
        bodyDef.position.set(-300, 200);

        // Create our body in the world using our body definition
        Body body = world.createBody(bodyDef);

        // Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        circle.setRadius(6f);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.2f;
        fixtureDef.friction = 1000.0f;
        fixtureDef.restitution = 0.3f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);
        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        circle.dispose();
        body.setLinearVelocity(new Vector2(64, -64));

        
        // Create our body definition
        BodyDef groundBodyDef =new BodyDef();
        groundBodyDef.type = BodyType.StaticBody;
        // Set its world position
        groundBodyDef.position.set(new Vector2(0, 10));

        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);

        // Create a polygon shape
        ChainShape chain = new ChainShape();
        // Set the chain to be a ramp
        chain.createChain(new Vector2 [] { 
            new Vector2(-245, 90),
            new Vector2(0, -120),
            new Vector2(10, -125),
            new Vector2(25, -128),
            new Vector2(35, -122),
            new Vector2(40, -115),
            new Vector2(45,-112)});
        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.restitution = 3.0f;
        // Create a fixture from our polygon shape and add it to our ground body
        Fixture fixture2 = groundBody.createFixture(chain, 0.0f);

        // Clean up after ourselves
        chain.dispose();
    }
}
