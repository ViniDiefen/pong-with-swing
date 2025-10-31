package br.com.vinidiefen.pong.physics;

import java.util.EventObject;

public class CollisionEvent extends EventObject {

    /**
     * Constructor
     * 
     * @param source the other object involved in the collision
     */
    public CollisionEvent(Object source) {
        super(source);
    }
    
}
