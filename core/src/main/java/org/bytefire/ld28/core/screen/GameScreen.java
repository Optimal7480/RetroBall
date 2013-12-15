/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bytefire.ld28.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import org.bytefire.ld28.core.LD28;

public class GameScreen extends AbstractScreen {
    private static final int WINDOW_WIDTH = 640;
    private static final int WINDOW_HEIGHT = 480;
    private static final float FRAME_GOAL = 1/60f;

    private OrthographicCamera cam;
    private World world;
    private Box2DDebugRenderer debugRender;


    private boolean mousePressed;
    private ArrayList<Vector2> currentChain;
    private double chainLength;
    private float chainDelta;
    private Body chain;
    private boolean requiresUpdate;

    public GameScreen(LD28 ld28){
        super(ld28);
        world = new World(new Vector2(0, -64), true);
        debugRender = new Box2DDebugRenderer();
        mousePressed = false;
        currentChain = new ArrayList<Vector2>();
        requiresUpdate = false;
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
        input(delta);
        physics(delta);
        cam.translate(WINDOW_WIDTH/16, 0);
    }

    public void input(float delta){
        boolean mousePressedPrev = mousePressed;
        mousePressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        if (mousePressed){
            if (!mousePressedPrev)
                currentChain = new ArrayList<Vector2>();
            Vector2 newPoint = new Vector2(Gdx.input.getX() - (WINDOW_WIDTH/2), WINDOW_HEIGHT - Gdx.input.getY() - (WINDOW_HEIGHT/2));
            if (currentChain.size() > 0) {
                Vector2 lastPoint = currentChain.get(currentChain.size() -1);
                if (lastPoint.dst2(newPoint) > 16 * 16) currentChain.add(newPoint);
            }
            else currentChain.add(newPoint);
            if (currentChain.size() >= 2) requiresUpdate = true;
        }
    }

    public void physics(float delta){
        if (requiresUpdate){
            ChainShape staticChain = new ChainShape();
            Vector2[] vectorArray = new Vector2[currentChain.size()];
            staticChain.createChain(currentChain.toArray(vectorArray));
            chain.createFixture(staticChain, 0.0f);
            staticChain.dispose();
            requiresUpdate = false;
        }
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
        fixtureDef.restitution = 2f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);
        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        circle.dispose();
        body.setLinearVelocity(new Vector2(64, -64));

        BodyDef chainBodyDef = new BodyDef();
        chainBodyDef.type = BodyType.StaticBody;
        chainBodyDef.position.set(0, 0);
        chain = world.createBody(chainBodyDef);
    }
}
