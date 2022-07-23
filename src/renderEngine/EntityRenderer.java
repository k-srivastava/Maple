package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.EngineMath;

import java.util.List;
import java.util.Map;

/**
 * Renderer for the engine to render all the entities to the display.
 */
public class EntityRenderer {
    private final StaticShader SHADER;

    /**
     * Create a new renderer using an existing static shader.
     *
     * @param shader Static shader containing a vertex and fragment shader.
     */
    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.SHADER = shader;

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Render all the entities to the display.
     *
     * @param entities Hash map of textures and entities corresponding to that texture.
     */
    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel texturedModel : entities.keySet()) {
            prepareTexturedModel(texturedModel);
            List<Entity> batch = entities.get(texturedModel);

            for (Entity entity : batch) {
                prepareEntity(entity);

                GL11.glDrawElements(GL11.GL_TRIANGLES, texturedModel.rawModel().vertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }

            unbindTexturedModel();
        }
    }

    /**
     * Prepare the texture for rendering by enabling the VAOs, binding the texture and loading the specular lighting
     * information.
     *
     * @param texturedModel Texture for the model to be prepared.
     */
    private void prepareTexturedModel(TexturedModel texturedModel) {
        RawModel rawModel = texturedModel.rawModel();
        ModelTexture texture = texturedModel.texture();

        GL30.glBindVertexArray(rawModel.vaoID());
        GL20.glEnableVertexAttribArray(0); // Position.
        GL20.glEnableVertexAttribArray(1); // Texture coordinates.
        GL20.glEnableVertexAttribArray(2); // Normal.

        if (texture.isHasTransparency()) MasterRenderer.disableBackFaceCulling();

        SHADER.loadSpecularLightData(texture.getShineDamping(), texture.getReflectivity());
        SHADER.loadFakeLighting(texture.isUseFakeLighting());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
    }

    /**
     * Unbind the texture by disabling and unbinding the VAOs.
     */
    private void unbindTexturedModel() {
        MasterRenderer.enableBackFaceCulling();

        GL20.glDisableVertexAttribArray(0); // Position.
        GL20.glDisableVertexAttribArray(1); // Texture coordinates.
        GL20.glDisableVertexAttribArray(2); // Normal.
        GL30.glBindVertexArray(0);
    }

    /**
     * Prepare the entity for rendering by calculating and loading its transformation matrix to the shader.
     *
     * @param entity Entity to prepare for rendering.
     */
    private void prepareEntity(Entity entity) {
        Matrix4f transformationMatrix = EngineMath.createTransformationMatrix(
                entity.getPosition(), entity.getRotation(), entity.getScale()
        );

        SHADER.loadTransformationMatrix(transformationMatrix);
    }
}
