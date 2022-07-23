package renderEngine;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.TerrainTexturePack;
import toolbox.EngineMath;

import java.util.List;

/**
 * Renderer for the engine to render all the terrains to the display.
 */
public class TerrainRenderer {
    private final TerrainShader SHADER;

    /**
     * Create a new renderer using an existing terrain shader and projection matrix.
     *
     * @param shader           Terrain shader containing a vertex and fragment shader.
     * @param projectionMatrix Projection matrix for the terrain.
     */
    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.SHADER = shader;

        shader.start();

        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();

        shader.stop();
    }

    /**
     * Render all the terrains to the display.
     *
     * @param terrains List of terrains to be rendered.
     */
    public void render(List<Terrain> terrains) {
        for (Terrain terrain : terrains) {
            prepareTerrain(terrain);
            loadModelMatrix(terrain);

            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.rawModel().vertexCount(), GL11.GL_UNSIGNED_INT, 0);

            unbindTerrainTexturedModel();
        }
    }

    /**
     * Prepare the terrain for rendering by enabling the VAOs, binding the texture and loading the specular lighting
     * information.
     *
     * @param terrain Terrain to be prepared.
     */
    private void prepareTerrain(Terrain terrain) {
        RawModel rawModel = terrain.rawModel();

        GL30.glBindVertexArray(rawModel.vaoID());
        GL20.glEnableVertexAttribArray(0); // Position.
        GL20.glEnableVertexAttribArray(1); // Texture coordinates.
        GL20.glEnableVertexAttribArray(2); // Normal.

        bindTextures(terrain);

        SHADER.loadSpecularLightData(1, 0);
    }

    /**
     * Bind the textures of each one of the four terrain textures to a different texture slot.
     *
     * @param terrain Terrain whose textures are to be bound.
     */
    private void bindTextures(Terrain terrain) {
        TerrainTexturePack texturePack = terrain.texturePack();

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.backgroundTexture().textureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.rTexture().textureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.gTexture().textureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.bTexture().textureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.blendMap().textureID());
    }

    /**
     * Unbind the terrain texture by disabling and unbinding the VAOs.
     */
    private void unbindTerrainTexturedModel() {
        GL20.glDisableVertexAttribArray(0); // Position.
        GL20.glDisableVertexAttribArray(1); // Texture coordinates.
        GL20.glDisableVertexAttribArray(2); // Normal.
        GL30.glBindVertexArray(0);
    }

    /**
     * Prepare the terrain for rendering by calculating and loading its transformation matrix to the shader.
     *
     * @param terrain Terrain to prepare for rendering.
     */
    private void loadModelMatrix(Terrain terrain) {
        Matrix4f transformationMatrix = EngineMath.createTransformationMatrix(
                new Vector3f(terrain.x(), 0, terrain.z()), new Vector3f(), 1
        );

        SHADER.loadTransformationMatrix(transformationMatrix);
    }
}
