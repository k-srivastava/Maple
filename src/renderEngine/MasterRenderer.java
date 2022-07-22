package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper over the Renderer class with its own shader.
 */
public class MasterRenderer {
    private final StaticShader SHADER = new StaticShader();
    private final Renderer RENDERER = new Renderer(SHADER);
    private final Map<TexturedModel, List<Entity>> ENTITIES = new HashMap<>();

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
     * Render all the entities within all the batches.
     *
     * @param globalLight Global light for the scene, generally the Sun.
     * @param camera      Camera for the display.
     */
    public void render(Light globalLight, Camera camera) {
        RENDERER.prepare();
        SHADER.start();

        SHADER.loadLight(globalLight);
        SHADER.loadViewMatrix(camera);

        RENDERER.render(ENTITIES);

        SHADER.stop();
        ENTITIES.clear();
    }

    /**
     * Clean up the shader after use when the display is closed.
     */
    public void cleanUp() {
        SHADER.cleanUp();
    }
}
