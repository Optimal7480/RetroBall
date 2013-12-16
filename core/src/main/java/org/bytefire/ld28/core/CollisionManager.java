/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bytefire.ld28.core;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 *
 * @author timn
 */
public interface CollisionManager {

    public void beginContact(Contact contact);

    public void endContact(Contact contact);

    public void preSolve(Contact contact, Manifold oldManifold);

    public void postSolve(Contact contact, ContactImpulse impulse);

    public Class getType();
}
