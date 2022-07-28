package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Camera class for the display.
 */
public class Camera {
    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;

    private final Player PLAYER;
    private final Vector3f POSITION = new Vector3f(100, 35, 50);
    private final Vector3f ROTATION = new Vector3f(20, 0, 0);

    /**
     * Create a new camera.
     *
     * @param player Player which the camera follows.
     */
    public Camera(Player player) {
        this.PLAYER = player;
    }

    /**
     * Move the camera in all three axes to follow the player from a third-person perspective.
     */
    public void move() {
        calculateZoom();
        calculateAngleAroundPlayer();
        calculatePitch();

        calculatePosition();
        rotation().y = 180 - (PLAYER.rotation().y + angleAroundPlayer);
    }

    /**
     * Calculate and change the distance of the camera from the player based on the mouse wheel scrolling.
     */
    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        distanceFromPlayer -= zoomLevel;
    }

    /**
     * Calculate and change the angle of the camera's yaw around the player based on the mouse movement in
     * the horizontal direction.
     */
    private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(1)) {
            float angleDelta = Mouse.getDX() * 0.3f;
            angleAroundPlayer -= angleDelta;
        }
    }

    /**
     * Calculate and change the angle of the camera's pitch around the player based on the mouse movement in
     * the vertical direction.
     */
    private void calculatePitch() {
        if (Mouse.isButtonDown(1)) {
            float pitchDelta = Mouse.getDY() * 0.1f;
            rotation().x -= pitchDelta;
        }
    }

    /**
     * Calculate the position of the camera based on the player movement and current pitch.
     */
     private void calculatePosition() {
        Vector2f distance = new Vector2f(
                (float) (distanceFromPlayer * Math.cos(Math.toRadians(rotation().x))),
                (float) (distanceFromPlayer * Math.sin(Math.toRadians(rotation().x)))
        );

        float theta = PLAYER.rotation().y + angleAroundPlayer;
        Vector2f offset = new Vector2f(
                (float) (distance.x * Math.sin(Math.toRadians(theta))),
                (float) (distance.x * Math.cos(Math.toRadians(theta)))
        );

        POSITION.x = PLAYER.position().x - offset.x;
        POSITION.y = PLAYER.position().y + distance.y;
        POSITION.z = PLAYER.position().z - offset.y;
    }

    public Vector3f position() {
        return POSITION;
    }

    public Vector3f rotation() {
        return ROTATION;
    }
}
