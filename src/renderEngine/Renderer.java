package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.EngineMath;

/**
 * Renderer for the engine to render all the raw models to the display.
 */
public class Renderer {
    private static final int FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;

    /**
     * Create a new renderer using an existing static shader.
     *
     * @param shader Static shader containing a vertex and fragment shader.
     */
    public Renderer(StaticShader shader) {
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
     * Render a raw model to the display by first binding a VAO and enabling it before drawing the triangles for the
     * model. Then the VAO is disabled and unbound. A texture is added and the entity's transformation matrix is also
     * loaded.
     *
     * @param entity Entity whose model is to be rendered to the display.
     * @param shader Shader code corresponding to the entity.
     */
    public void render(Entity entity, StaticShader shader) {
        TexturedModel texturedModel = entity.getModel();
        RawModel model = texturedModel.rawModel();
        ModelTexture texture = texturedModel.texture();

        Matrix4f transformationMatrix = EngineMath.createTransformationMatrix(
                entity.getPosition(), entity.getRotation(), entity.getScale()
        );

        GL30.glBindVertexArray(model.vaoID());
        GL20.glEnableVertexAttribArray(0); // Position.
        GL20.glEnableVertexAttribArray(1); // Texture coordinates.
        GL20.glEnableVertexAttribArray(2); // Normal.

        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadSpecularLightData(texture.getShineDamping(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.texture().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.vertexCount(), GL11.GL_UNSIGNED_INT, 0);

        GL20.glDisableVertexAttribArray(0); // Position.
        GL20.glDisableVertexAttribArray(1); // Texture coordinates.
        GL20.glDisableVertexAttribArray(2); // Normal.
        GL30.glBindVertexArray(0);
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
