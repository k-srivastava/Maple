package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

/**
 * Create and manage the display throughout its lifecycle.
 */
public class DisplayManager {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int FPS_CAP = 120;

    /**
     * Create the display with OpenGL 3.2 in the top-left corner of the screen with resolution of 1280x720.
     */
    public static void createDisplay() {
        ContextAttribs contextAttribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), contextAttribs);
            Display.setTitle("Maple Window");
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    /**
     * Update the display every frame and sync it to a maximum FPS cap.
     */
    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
    }

    /**
     * Close the display at the end of the program.
     */
    public static void closeDisplay() {
        Display.destroy();
    }
}
