package renderEngine;

import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Load a model by putting its vertex information into a VBO which is stored in the attribute list of a VAO.
 */
public class Loader {
    private final List<Integer> VAOs = new ArrayList<>();
    private final List<Integer> VBOs = new ArrayList<>();
    private final List<Integer> TEXTURES = new ArrayList<>();

    /**
     * Create a new VAO and bind it for use.
     *
     * @return ID of the newly created VAO.
     */
    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        VAOs.add(vaoID);
        GL30.glBindVertexArray(vaoID);

        return vaoID;
    }

    /**
     * Unbind the newly created VAO.
     */
    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    /**
     * Load a model's vertex positions in a VAO.
     *
     * @param positions          Vertex positions of the model.
     * @param textureCoordinates Coordinates of the texture to be mapped onto the model.
     * @param normals            Normal vectors of the model.
     * @param indices            Indices of vertex positions of the model.
     * @return Raw model stored in a VAO.
     */
    public RawModel loadToVAO(float[] positions, float[] textureCoordinates, float[] normals, int[] indices) {
        int vaoID = createVAO();

        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoordinates);
        storeDataInAttributeList(2, 3, normals);

        unbindVAO();

        return new RawModel(vaoID, indices.length);
    }

    /**
     * Load a texture using an existing PNG file.
     *
     * @param filename Name of PNG texture file.
     * @return Location of the texture ID.
     */
    public int loadTexture(String filename) {
        Texture texture;

        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + filename + ".png"));
        }

        catch (IOException e) {
            System.err.println("Tried to load texture " + filename + ".png unsuccessfully.");
            throw new RuntimeException(e);
        }

        int textureID = texture.getTextureID();
        TEXTURES.add(textureID);
        return textureID;
    }

    /**
     * Bind an array of indices to an OpenGL element array buffer.
     *
     * @param indices Indices to be bound.
     */
    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        VBOs.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);

        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    /**
     * Store the index data in a new integer buffer.
     *
     * @param data Index data to be stored in a new integer buffer.
     * @return Integer buffer with index data.
     */
    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    /**
     * Store the vertex data in a new float buffer.
     *
     * @param data Vertex data to be stored in a new float buffer.
     * @return Float buffer with vertex data.
     */
    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    /**
     * Store the vertex data in an attribute list in a particular slot.
     *
     * @param attributeNumber Slot number of where data is stored.
     * @param coordinateSize  Size of the coordinates to be stored in an attribute list.
     * @param data            Vertex data to be stored in an attribute list.
     */
    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        VBOs.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Delete all created VAOs and VBOs.
     */
    public void cleanUp() {
        for (int vao : VAOs) {
            GL30.glDeleteVertexArrays(vao);
        }

        for (int vbo : VBOs) {
            GL15.glDeleteBuffers(vbo);
        }

        for (int texture : TEXTURES) {
            GL11.glDeleteTextures(texture);
        }
    }
}
