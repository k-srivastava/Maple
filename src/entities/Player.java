package entities;

import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

/**
 * Main player class for the game; subclass of the entity class.
 */
public class Player extends Entity {
    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float verticalSpeed = 0;
    private boolean isJumping = false;

    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private static final float JUMP_POWER = 30;
    private static final float GRAVITY = -50;
    private static final float TERRAIN_HEIGHT = 0;

    /**
     * Create a new player class.
     *
     * @param texturedModel Textured model for the player.
     * @param position      Position of the player.
     * @param rotation      Rotation of the player.
     * @param scale         Scale of the player.
     */
    public Player(TexturedModel texturedModel, Vector3f position, Vector3f rotation, float scale) {
        super(texturedModel, position, rotation, scale);
    }

    /**
     * Move the player by polling for inputs every frame.
     */
    public void move() {
        pollInputs();

        verticalSpeed += GRAVITY * DisplayManager.deltaTime();

        float distance = currentSpeed * DisplayManager.deltaTime();
        Vector3f displacement = new Vector3f(
                (float) (distance * Math.sin(Math.toRadians(rotation().y))),
                verticalSpeed * DisplayManager.deltaTime(),
                (float) (distance * Math.cos(Math.toRadians(rotation().y)))
        );

        Vector3f rotation = new Vector3f(0, currentTurnSpeed * DisplayManager.deltaTime(), 0);

        displace(displacement);
        rotate(rotation);

        if (position().y < TERRAIN_HEIGHT) {
            verticalSpeed = 0;
            position().y = TERRAIN_HEIGHT;
            isJumping = false;
        }
    }

    /**
     * Poll for keyboard inputs every frame for movement and jumping.
     */
    private void pollInputs() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) currentSpeed = RUN_SPEED;
        else if (Keyboard.isKeyDown(Keyboard.KEY_S)) currentSpeed = -RUN_SPEED;
        else currentSpeed = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) currentTurnSpeed = TURN_SPEED;
        else if (Keyboard.isKeyDown(Keyboard.KEY_D)) currentTurnSpeed = - TURN_SPEED;
        else currentTurnSpeed = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) jump();
    }

    /**
     * Make the player jump by setting the vertical speed to a specified force if not already in air.
     */
    private void jump() {
        if (!isJumping) {
            verticalSpeed = JUMP_POWER;
            isJumping = true;
        }
    }
}
