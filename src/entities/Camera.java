package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Camera class for the display.
 */
public class Camera {
    private final Vector3f POSITION = new Vector3f(100, 35, 50);
    private final Vector3f ROTATION = new Vector3f();

    /**
     * Create a new camera.
     */
    public Camera() { }

    /**
     * Move the camera in all three axes using the keyboard.
     */
    public void move() { }

    public Vector3f position() {
        return POSITION;
    }

    public Vector3f rotation() {
        return ROTATION;
    }
}
