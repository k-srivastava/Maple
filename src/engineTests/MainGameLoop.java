package engineTests;

import OBJConverter.OBJFileLoader;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

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

        // Terrain textures.
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        // Model data.
        TexturedModel tree = new TexturedModel(
                OBJFileLoader.loadOBJToVAO("tree", loader),
                new ModelTexture(loader.loadTexture("tree"))
        );

        TexturedModel grass = new TexturedModel(
                OBJFileLoader.loadOBJToVAO("grassModel", loader),
                new ModelTexture(loader.loadTexture("grassTexture"))
        );

        TexturedModel flower = new TexturedModel(
                OBJFileLoader.loadOBJToVAO("grassModel", loader),
                new ModelTexture(loader.loadTexture("flower"))
        );

        TexturedModel fern = new TexturedModel(
                OBJFileLoader.loadOBJToVAO("fern", loader),
                new ModelTexture(loader.loadTexture("fern"))
        );

        TexturedModel lowPolyTree = new TexturedModel(
                OBJFileLoader.loadOBJToVAO("lowPolyTree", loader),
                new ModelTexture(loader.loadTexture("lowPolyTree"))
        );

        TexturedModel stanfordBunny = new TexturedModel(
                OBJFileLoader.loadOBJToVAO("bunny", loader),
                new ModelTexture(loader.loadTexture("white"))
        );

        grass.texture().setHasTransparency(true);
        grass.texture().setUseFakeLighting(true);

        flower.texture().setHasTransparency(true);
        flower.texture().setUseFakeLighting(true);

        fern.texture().setHasTransparency(true);

        // Entity spawning.
        List<Entity> entities = new ArrayList<>();
        Random random = new Random(676452);

        for (int i = 0; i < 400; i++) {
            if (i % 7 == 0) {
                entities.add(new Entity(
                        grass,
                        new Vector3f(random.nextFloat() * 400 - 200, 0, random.nextFloat() * -400),
                        new Vector3f(), 1.8f
                ));

                entities.add(new Entity(
                        flower,
                        new Vector3f(random.nextFloat() * 400 - 200, 0, random.nextFloat() * -400),
                        new Vector3f(), 2.3f
                ));
            }

            if (i % 3 == 0) {
                entities.add(new Entity(
                        fern,
                        new Vector3f(random.nextFloat() * 400 - 200, 0, random.nextFloat() * -400),
                        new Vector3f(0, random.nextFloat() * 360, 0), 0.9f
                ));

                entities.add(new Entity(
                        lowPolyTree,
                        new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),
                        new Vector3f(0, random.nextFloat() * 360, 0), random.nextFloat() * 0.1f + 0.6f
                ));

                entities.add(new Entity(
                        tree,
                        new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),
                        new Vector3f(), random.nextFloat() + 4
                ));
            }
        }

        Player player = new Player(stanfordBunny, new Vector3f(100, 0, -50), new Vector3f(), 1);

        Light light = new Light(new Vector3f(20000, 40000, 20000), new Vector3f(1, 1, 1));

        Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap);
        Terrain terrain2 = new Terrain(1, 0, loader, texturePack, blendMap);

        Camera camera = new Camera();
        MasterRenderer renderer = new MasterRenderer();

        while (!Display.isCloseRequested()) {
            camera.move();
            player.move();

            renderer.processEntity(player);

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);

            for (Entity entity : entities) renderer.processEntity(entity);
            renderer.render(light, camera);

            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
