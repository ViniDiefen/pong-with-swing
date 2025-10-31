package br.com.vinidiefen.pong.physics;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.vinidiefen.pong.entities.GameObject;

/**
 * Handles collision detection between game entities
 */
public class CollisionDetector {
    private HashMap<CollisionObserver, List<GameObject>> collisionMap;

    /**
     * Constructor
     */
    public CollisionDetector() {
        collisionMap = new HashMap<>();
    }

    /**
     * Add a CollisionObserver with the GameObjects to check collision against
     * 
     * @param observer
     * @param collidableObjects
     */
    public void addCollisionObserver(CollisionObserver observer, GameObject... collidableObjects) {
        if (collidableObjects.length > 0) {
            collisionMap.put(observer, Arrays.asList(collidableObjects));
        }
    }

    /**
     * Remove a CollisionObserver
     * 
     * @param observer
     */
    public void removeCollisionObserver(CollisionObserver observer) {
        collisionMap.remove(observer);
    }

    /**
     * Check if some of the game objects collide
     * 
     * @return true if collision detected
     */
    public boolean checkCollision() {
        for (CollisionObserver observer : collisionMap.keySet()) {
            List<GameObject> collidableObjects = collisionMap.get(observer);
            for (GameObject obj2 : collidableObjects) {
                if (checkCollision(observer, obj2)) {
                    observer.onCollisionChanged(new CollisionEvent(obj2));
                }
            }
        }
        return false;
    }

    /**
     * Check if a CollisionObserver collides with a GameObject
     */
    public boolean checkCollision(CollisionObserver observer, GameObject gameObject2) {
        if (observer instanceof GameObject) {
            GameObject gameObject1 = (GameObject) observer;
            Rectangle gameObject1Bounds = gameObject1.getBounds();
            Rectangle gameObject2Bounds = gameObject2.getBounds();

            return gameObject1Bounds.intersects(gameObject2Bounds);
        }
        return false;
    }

}
