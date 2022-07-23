package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * Camera class for the display.
 */
public class Camera {
    private final Vector3f POSITION = new Vector3f();
    private final Vector3f ROTATION = new Vector3f();

    /**
     * Create a new camera.
     */
    public Camera() { }

    /**
     * Move the camera in all three axes using the keyboard.
     */
    public void move() {
        float displacement = 0.2f;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) POSITION.z -= displacement;
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) POSITION.z += displacement;
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) POSITION.x += displacement;
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) POSITION.x -= displacement;
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) POSITION.y += displacement;
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) POSITION.y -= displacement;
    }

    public Vector3f position() {
        return POSITION;
    }

    public Vector3f rotation() {
        return ROTATION;
    }
}
