package br.com.vinidiefen.pong.core.collision;

import br.com.vinidiefen.pong.domain.entities.GameObject;

/**
 * Observers that react to collisions.
 */
public interface CollisionObserver {

    /**
     * Called when a collision happens
     *
     * @param other The other GameObject involved in the collision
     */
    void onCollision(GameObject other);
    
}
