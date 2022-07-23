package engineTests;

import OBJConverter.ModelData;
import OBJConverter.OBJFileLoader;
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
import terrains.Terrain;
import textures.ModelTexture;

/**
 * Main game loop for the game engine and entry point for testing the engine.
 */
public class MainGameLoop {
    public static void main(String[] args) {
        DisplayManager.createDisplay();

        Loader loader = new Loader();
        MasterRenderer renderer = new MasterRenderer();
        Camera camera = new Camera();

        ModelData data = OBJFileLoader.loadOBJ("tree");
        RawModel treeModel = loader.loadToVAO(data.vertices(), data.textureCoordinates(), data.normals(), data.indices());
        TexturedModel staticModel = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("tree")));

        Entity entity = new Entity(staticModel, new Vector3f(0, 0, -25), new Vector3f(), 1);
        Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));

        Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("grass")));

        while (!Display.isCloseRequested()) {
            entity.rotate(new Vector3f(0, 1, 0));
            camera.move();

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processEntity(entity);

            renderer.render(light, camera);

            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
