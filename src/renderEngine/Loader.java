package renderEngine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

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
     * Store the vertex data in a new float buffer.
     *
     * @param data Vertex data to be stored in a new float bufer.
     * @return Float buffer with vertex data.
     */
    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
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
     * Store the vertex data in an attribute list in a particular slot.
     *
     * @param attributeNumber Slot number of where data is stored.
     * @param data            Vertex data to be stored in an attribute list.
     */
    private void storeDataInAttributeList(int attributeNumber, float[] data) {
        int vboID = GL15.glGenBuffers();
        VBOs.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
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
     * Load a model's vertex positions in a VAO.
     *
     * @param positions Vertex positions of the model.
     * @param indices   Indices of vertex positions of the model.
     * @return Raw model stored in a VAO.
     */
    public RawModel loadToVAO(float[] positions, int[] indices) {
        int vaoID = createVAO();

        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, positions);

        unbindVAO();

        return new RawModel(vaoID, indices.length);
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
    }
}
