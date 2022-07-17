package renderEngine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
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
     * Load a model's vertex positions in a VAO.
     *
     * @param positions Vertex positions of the model.
     * @return Raw model stored in a VAO.
     */
    public RawModel loadToVAO(float[] positions) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, positions);
        unbindVAO();

        return new RawModel(vaoID, positions.length);
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
