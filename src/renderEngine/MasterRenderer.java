package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper over the Renderer class with its own shader.
 */
public class MasterRenderer {
    private static final int FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private static final Vector3f SKY_COLOR = new Vector3f(0.5f, 0.5f, 0.5f);

    private Matrix4f projectionMatrix;

    private final EntityRenderer ENTITY_RENDERER;
    private final StaticShader STATIC_SHADER = new StaticShader();

    private final TerrainRenderer TERRAIN_RENDERER;
    private final TerrainShader TERRAIN_SHADER = new TerrainShader();

    private final Map<TexturedModel, List<Entity>> ENTITIES = new HashMap<>();
    private final List<Terrain> TERRAINS = new ArrayList<>();

    /**
     * Create a new master renderer to control entity and terrain renderers.
     */
    public MasterRenderer() {
        enableBackFaceCulling();
        createProjectionMatrix();

        ENTITY_RENDERER = new EntityRenderer(STATIC_SHADER, projectionMatrix);
        TERRAIN_RENDERER = new TerrainRenderer(TERRAIN_SHADER, projectionMatrix);
    }

    /**
     * Prepare the renderer to clear the current color on the display. Called before every frame.
     */
    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z, 1);
    }

    /**
     * Process an entity by adding it to a render batch if it exists or creating one if it doesn't.
     *
     * @param entity Entity to be processed.
     */
    public void processEntity(Entity entity) {
        TexturedModel texturedModel = entity.getModel();
        List<Entity> batch = ENTITIES.get(texturedModel);

        if (batch != null) {
            batch.add(entity);
        }

        else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            ENTITIES.put(texturedModel, newBatch);
        }
    }

    /**
     * Process a terrain piece by adding it to a list of terrains.
     *
     * @param terrain Terrain to be processed.
     */
    public void processTerrain(Terrain terrain) {
        TERRAINS.add(terrain);
    }

    /**
     * Render all the entities within all the batches and all the terrains.
     *
     * @param globalLight Global light for the scene, generally the Sun.
     * @param camera      Camera for the display.
     */
    public void render(Light globalLight, Camera camera) {
        prepare();
        // Entity renderer and static shader.
        STATIC_SHADER.start();

        STATIC_SHADER.loadSkyColor(SKY_COLOR);
        STATIC_SHADER.loadLight(globalLight);
        STATIC_SHADER.loadViewMatrix(camera);

        ENTITY_RENDERER.render(ENTITIES);

        STATIC_SHADER.stop();

        // Terrain renderer and terrain shader.
        TERRAIN_SHADER.start();

        TERRAIN_SHADER.loadSkyColor(SKY_COLOR);
        TERRAIN_SHADER.loadLight(globalLight);
        TERRAIN_SHADER.loadViewMatrix(camera);

        TERRAIN_RENDERER.render(TERRAINS);

        TERRAIN_SHADER.stop();

        ENTITIES.clear();
        TERRAINS.clear();
    }

    /**
     * Clean up the shader after use when the display is closed.
     */
    public void cleanUp() {
        STATIC_SHADER.cleanUp();
        TERRAIN_SHADER.cleanUp();
    }

    /**
     * Create a new 4x4 projection matrix using the display and a custom view frustum.
     */
    private void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / Display.getHeight();
        float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -(FAR_PLANE + NEAR_PLANE) / frustumLength;
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -(2 * NEAR_PLANE * FAR_PLANE) / frustumLength;
        projectionMatrix.m33 = 0;
    }

    /**
     * Enable OpenGL back face culling.
     */
    public static void enableBackFaceCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    /**
     * Disable OpenGL back face culling — useful for transparent objects.
     */
    public static void disableBackFaceCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }
}
