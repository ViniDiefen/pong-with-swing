package com.diefen.vini.physics;

public interface CollisionObserver {

    /**
     * Called when a collision happen
     * 
     * @param event The collision event containing the other element
     */
    void onCollisionChanged(CollisionEvent event);
    
}
