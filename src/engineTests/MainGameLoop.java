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
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main game loop for the game engine and entry point for testing the engine.
 */
public class MainGameLoop {
    public static void main(String[] args) {
        DisplayManager.createDisplay();

        Loader loader = new Loader();
        MasterRenderer renderer = new MasterRenderer();
        Camera camera = new Camera();

        RawModel model = OBJLoader.loadOBJModel("dragon", loader);
        TexturedModel dragonModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white")));
        Light light = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1, 1, 1));

        List<Entity> dragons = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            float x = random.nextFloat() * 100 - 50;
            float y = random.nextFloat() * 100 - 50;
            float z = random.nextFloat() * -300;

            float rx = random.nextFloat() * 180f;
            float ry = random.nextFloat() * 180f;

            dragons.add(new Entity(dragonModel, new Vector3f(x, y, z), new Vector3f(rx, ry, 0), 1));
        }

        while (!Display.isCloseRequested()) {
            camera.move();

            for (Entity dragon : dragons) {
                renderer.processEntity(dragon);
            }

            renderer.render(light, camera);

            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
