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
 * Renderer for the engine to render all the raw models to the display.
 */
public class Renderer {
    private static final int FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;
    private final StaticShader SHADER;

    /**
     * Create a new renderer using an existing static shader.
     *
     * @param shader Static shader containing a vertex and fragment shader.
     */
    public Renderer(StaticShader shader) {
        this.SHADER = shader;

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

        createProjectionMatrix();

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Prepare the renderer to clear the current color on the display. Called before every frame.
     */
    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0, 0, 0, 1);
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

        SHADER.loadSpecularLightData(texture.getShineDamping(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
    }

    /**
     * Unbind the texture by disabling and unbinding the VAOs.
     */
    private void unbindTexturedModel() {
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
}
