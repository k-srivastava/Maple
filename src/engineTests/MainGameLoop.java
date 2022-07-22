package engineTests;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
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
        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);
        Camera camera = new Camera();

        RawModel model = OBJLoader.loadOBJModel("dragon", loader);
        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white")));
        ModelTexture texture = staticModel.texture();
        texture.setShineDamping(10);
        texture.setReflectivity(1);

        Entity entity = new Entity(staticModel, new Vector3f(0, 0, -25), new Vector3f(), 1);
        Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));

        while (!Display.isCloseRequested()) {
            entity.rotate(new Vector3f(0, 1, 0));
            camera.move();

            renderer.prepare();
            shader.start();
            shader.loadLight(light);

            shader.loadViewMatrix(camera);
            renderer.render(entity, shader);

            shader.stop();
            DisplayManager.updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
