package br.com.vinidiefen.pong.core.collision;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.com.vinidiefen.pong.domain.entities.GameObject;

/**
 * Handles collision detection between game entities.
 */
public class CollisionDetector {
    private final Map<CollisionObserver, List<GameObject>> collisionMap = new HashMap<>();

    /**
     * Add a CollisionObserver with the GameObjects to check collision against
     *
     * @param observer
     * @param collidableObjects
     */
    public void addCollisionObserver(CollisionObserver observer, GameObject... collidableObjects) {
        Objects.requireNonNull(observer, "observer must not be null");
        if (collidableObjects == null || collidableObjects.length == 0) {
            return;
        }

        List<GameObject> collidables = collisionMap.computeIfAbsent(observer, key -> new ArrayList<>());
        Arrays.stream(collidableObjects)
                .filter(Objects::nonNull)
                .filter(obj -> !collidables.contains(obj))
                .forEach(collidables::add);
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
        boolean collisionDetected = false;

        for (Map.Entry<CollisionObserver, List<GameObject>> entry : collisionMap.entrySet()) {
            CollisionObserver observer = entry.getKey();
            for (GameObject obj2 : entry.getValue()) {
                if (checkCollision(observer, obj2)) {
                    observer.onCollision(obj2);
                    collisionDetected = true;
                }
            }
        }
        return collisionDetected;
    }

    /**
     * Check if a CollisionObserver collides with a GameObject
     */
    public boolean checkCollision(CollisionObserver observer, GameObject gameObject2) {
        if (!(observer instanceof GameObject) || gameObject2 == null) {
            return false;
        }

        Rectangle gameObject1Bounds = ((GameObject) observer).getBounds();
        Rectangle gameObject2Bounds = gameObject2.getBounds();

        return gameObject1Bounds.intersects(gameObject2Bounds);
    }

    /**
     * Removes every registered observer/collidable pair.
     */
    public void clear() {
        collisionMap.clear();
    }

}
