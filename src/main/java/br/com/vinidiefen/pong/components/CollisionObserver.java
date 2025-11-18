package br.com.vinidiefen.pong.components;

public interface CollisionObserver {

    /**
     * Called when a collision happens
     * 
     * @param other The other GameObject involved in the collision
     */
    void onCollision(GameObject other);
    
}
