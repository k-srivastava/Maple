package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
    private final Vector3f POSITION = new Vector3f();
    private final Vector3f ROTATION = new Vector3f();

    public Camera() { }

    public void move() {
        float displacement = 0.02f;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            POSITION.z -= displacement;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            POSITION.x += displacement;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            POSITION.x -= displacement;
        }
    }

    public Vector3f getPosition() {
        return POSITION;
    }

    public Vector3f getRotation() {
        return ROTATION;
    }
}
