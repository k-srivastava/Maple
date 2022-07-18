package engineTests;

import models.TexturedModel;
import org.lwjgl.opengl.Display;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

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

        // Texture coordinates for a rectangle.
        float[] textureCoordinates = {
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };

        RawModel model = loader.loadToVAO(vertices, textureCoordinates, indices);
        ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        while (!Display.isCloseRequested()) {
            renderer.prepare();

            shader.start();
            renderer.render(texturedModel);
            shader.stop();

            DisplayManager.updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
