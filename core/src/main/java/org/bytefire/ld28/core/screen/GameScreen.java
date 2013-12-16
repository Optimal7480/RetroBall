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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.Random;
import org.bytefire.ld28.core.CollisionManager;
import org.bytefire.ld28.core.Platform;
import org.bytefire.ld28.core.LD28;
import org.bytefire.ld28.core.Player;
import org.bytefire.ld28.core.Upgrade;
import org.bytefire.ld28.core.Upgrade.Type;

public class GameScreen extends AbstractScreen implements ContactListener{
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final float FRAME_GOAL = 1/60f;
    private static final int BOX_SCALE = 8;
    public static final float PLATFORM_CAP = 4000;

    private static final boolean DEBUG_RENDER = false;

    private ShapeRenderer gui;
    private OrthographicCamera cam;
    private World world;
    private Box2DDebugRenderer debugRender;
    private float worldTime;
    private boolean mousePressed;
    private Color globalColor;
    private float totalPlatformLength;
    private Random spawn;
    private long spawnSeed;

    private ArrayList<Platform> platforms;
    private Platform currentPlatform;
    private Player player;

    private float playerX;
    private float xdelta;

    public GameScreen(LD28 main){
        super(main);
        gui = new ShapeRenderer();
        world = new World(new Vector2(0, -64), true);
        debugRender = new Box2DDebugRenderer();
        world.setContactListener(this);
        mousePressed = false;
        platforms = new ArrayList<Platform>();
        currentPlatform = null;
        player = null;
        globalColor = Color.RED;
        totalPlatformLength = 0;
        spawn = new Random(System.nanoTime());
        spawnSeed = spawn.nextLong();
        xdelta = 0;
    }

    @Override
    public void show(){
        super.show();
        player = new Player(game);
        playerX = player.getBody().getPosition().x;

        //stage.setViewport(WINDOW_WIDTH, WINDOW_HEIGHT, true);
        cam = new OrthographicCamera(WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setCamera(cam);
        cam.zoom = 0.50F;
        cam.update();
    }

    @Override
    public void render(float delta){
        cam.position.x = player.getX();
        cam.position.y = 120;
        cam.update();
        super.render(delta);
        gui(delta);

        input(delta);

        xdelta += delta;
        for (int i = 0; i < Math.round(player.getPosition().x - playerX); i++){
            forX(delta, Math.round(playerX) + i);
            delta = 0;
        }
        playerX = player.getPosition().x;

        physics(delta);
    }

    public void input(float delta){
        boolean mousePressedPrev = mousePressed;
        mousePressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        Vector2 mouse = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        if (mousePressed){
            if (!mousePressedPrev){
                if (currentPlatform != null) platforms.add(currentPlatform);
                currentPlatform = new Platform(game);
            }
            currentPlatform.addPoint(
                new Vector2(mouse.x, mouse.y), worldTime);
        }
    }

    public void physics(float delta){
        if (currentPlatform != null){
            totalPlatformLength = (float) currentPlatform.getLength();
            for (Platform platform : platforms)
                totalPlatformLength += platform.getLength();
        }

        worldTime += FRAME_GOAL;
        world.step(FRAME_GOAL, 6, 2);
        world.getContactList();
    }

    public void forX(float delta, int x){
        spawn.setSeed(x ^ spawnSeed);
        if (DEBUG_RENDER) debugRender.render(world, cam.combined);
        if(spawn.nextInt(500) == 1) spawnUpgrade();
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

    public Upgrade spawnUpgrade(){
        int spawnChance = spawn.nextInt(4);
        switch (spawnChance){
            case 0: return new Upgrade(game, Type.BOUNCE);
            case 1: return new Upgrade(game, Type.MAGNET);
            case 2: return new Upgrade(game, Type.FLY);
            case 3: return new Upgrade(game, Type.LARGE_BALL);
            case 4: return new Upgrade(game, Type.LOW_GRAV);
        }
        return new Upgrade(game, Type.BOUNCE);
    }

    @Override
    public void beginContact(Contact contact) {
        ((CollisionManager) contact.getFixtureA().getBody().getUserData()).beginContact(contact);
        ((CollisionManager) contact.getFixtureB().getBody().getUserData()).beginContact(contact);

    }

    @Override
    public void endContact(Contact contact) {
        ((CollisionManager)contact.getFixtureA().getBody().getUserData()).endContact(contact);
        ((CollisionManager)contact.getFixtureB().getBody().getUserData()).endContact(contact);

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        ((CollisionManager)contact.getFixtureA().getBody().getUserData()).preSolve(contact, oldManifold);
        ((CollisionManager)contact.getFixtureB().getBody().getUserData()).preSolve(contact, oldManifold);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        ((CollisionManager)contact.getFixtureA().getBody().getUserData()).postSolve(contact, impulse);
        ((CollisionManager)contact.getFixtureB().getBody().getUserData()).postSolve(contact, impulse);
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

    public Player getPlayer() {
        return player;
    }
}
