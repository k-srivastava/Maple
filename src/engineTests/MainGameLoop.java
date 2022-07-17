package engineTests;

import org.lwjgl.opengl.Display;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;
import shaders.StaticShader;

/**
 * Main game loop for the game engine and entry point for testing the engine.
 */
public class MainGameLoop {
    public static void main(String[] args) {
        DisplayManager.createDisplay();
        Loader loader = new Loader();
        Renderer renderer = new Renderer();

        StaticShader shader = new StaticShader();

        // Vertex positions for a rectangle.
        float[] vertices = {
                -0.5f, 0.5f, 0,
                -0.5f, -0.5f, 0,
                0.5f, -0.5f, 0,
                0.5f, 0.5f, 0
        };

        // Index for rendering order of vertices for a rectangle.
        int[] indices = {
                0, 1, 3,
                3, 1, 2
        };

        RawModel model = loader.loadToVAO(vertices, indices);

        while (!Display.isCloseRequested()) {
            renderer.prepare();

            shader.start();
            renderer.render(model);
            shader.stop();

            DisplayManager.updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
