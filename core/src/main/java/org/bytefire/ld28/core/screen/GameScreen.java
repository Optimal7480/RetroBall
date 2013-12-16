/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bytefire.ld28.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import org.bytefire.ld28.core.CollisionManager;
import org.bytefire.ld28.core.DrawnStatic;
import org.bytefire.ld28.core.LD28;
import org.bytefire.ld28.core.Player;

public class GameScreen extends AbstractScreen implements ContactListener{
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final float FRAME_GOAL = 1/60f;
    private static final int BOX_SCALE = 8;
    public static final float PLATFORM_CAP = 640;

    private static final boolean DEBUG_RENDER = false;

    private ShapeRenderer gui;
    private OrthographicCamera cam;
    private World world;
    private Box2DDebugRenderer debugRender;
    private float worldTime;
    private boolean mousePressed;
    private Color globalColor;
    private float totalPlatformLength;

    private ArrayList<DrawnStatic> staticWalls;
    private DrawnStatic currentWall;
    private Player player;

    public GameScreen(LD28 main){
        super(main);
        gui = new ShapeRenderer();
        world = new World(new Vector2(0, -64), true);
        debugRender = new Box2DDebugRenderer();
        world.setContactListener(this);
        mousePressed = false;
        staticWalls = new ArrayList<DrawnStatic>();
        currentWall = null;
        player = null;
        globalColor = Color.RED;
        totalPlatformLength = 0;
    }

    @Override
    public void render(float delta){
        super.render(delta);
        cam.position.x = player.getX();
        cam.position.y = 120;
        cam.update();
        gui(delta);
        if (DEBUG_RENDER) debugRender.render(world, cam.combined);

        //if (delta < FRAME_GOAL) try {
        //    Thread.sleep((long) ((FRAME_GOAL - delta) * 1000));
        //} catch (InterruptedException ex) {
        //    Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        //}
        input(delta);
        physics(delta);
    }

    public void input(float delta){
        boolean mousePressedPrev = mousePressed;
        mousePressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        Vector2 mouse = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        if (mousePressed){
            if (!mousePressedPrev){
                if (currentWall != null) staticWalls.add(currentWall);
                currentWall = new DrawnStatic(game);
            }
            currentWall.addPoint(
                new Vector2(mouse.x, mouse.y), worldTime);
        }
    }

    public void physics(float delta){
        if (currentWall != null){
            totalPlatformLength = (float) currentWall.getLength();
            for (DrawnStatic platform : staticWalls)
                totalPlatformLength += platform.getLength();
        }

        worldTime += FRAME_GOAL;
        world.step(FRAME_GOAL, 6, 2);
    }

    @Override
    public void show(){
        super.show();
        player = new Player(game);

        //stage.setViewport(WINDOW_WIDTH, WINDOW_HEIGHT, true);
        cam = new OrthographicCamera(WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setCamera(cam);
        cam.zoom = 0.50F;
        cam.update();
    }

    public void gui(float delta){
        gui.begin(ShapeRenderer.ShapeType.Line);
        gui.setColor(Color.WHITE);
        gui.rect(50, 560, 700, 20);
        gui.end();
        gui.begin(ShapeRenderer.ShapeType.Filled);
        gui.setColor(Color.WHITE);
        gui.rect(50, 560, 700 - (700 * totalPlatformLength / PLATFORM_CAP), 20);
        gui.end();
    }

    @Override
    public void beginContact(Contact contact) {
        ((CollisionManager) contact.getFixtureA().getBody().getUserData()).beginContact(contact);
    }

    @Override
    public void endContact(Contact contact) {
        ((CollisionManager)contact.getFixtureA().getBody().getUserData()).endContact(contact);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        ((CollisionManager)contact.getFixtureA().getBody().getUserData()).preSolve(contact, oldManifold);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        ((CollisionManager)contact.getFixtureA().getBody().getUserData()).postSolve(contact, impulse);
    }

    public World getWorld() {
        return world;
    }

    public float getWorldTime() {
        return worldTime;
    }

    public float getTotalPlatformLength() {
        return totalPlatformLength;
    }
}
