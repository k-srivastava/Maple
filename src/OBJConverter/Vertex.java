package OBJConverter;

import org.lwjgl.util.vector.Vector3f;

/**
 * Vertex representation within an OBJ file.
 */
public class Vertex {
    private final int INDEX;
    private final Vector3f POSITION;
    private final float LENGTH;

    private int textureIndex = NO_INDEX;
    private int normalIndex = NO_INDEX;
    private Vertex duplicateVertex = null;

    private static final int NO_INDEX = -1;

    /**
     * Create a new vertex.
     *
     * @param index    Index of the vertex within a vertex list.
     * @param position Position of the vertex in the model.
     */
    public Vertex(int index, Vector3f position) {
        this.INDEX = index;
        this.POSITION = position;
        this.LENGTH = position.length();
    }

    /**
     * Check if the texture coordinate index and normal vector index of the vertex are set or not.
     *
     * @return True if the vertex is not set, else false.
     */
    public boolean isNotSet() {
        return textureIndex == NO_INDEX || normalIndex == NO_INDEX;
    }

    /**
     * Compare the vertex with another to check if they have the same texture coordinate index and normal vector index.
     *
     * @param textureIndexOther Texture coordinate index of the other vector.
     * @param normalIndexOther  Normal vector index of the other vector.
     * @return True if they are equal, else false.
     */
    public boolean hasSameTextureAndNormal(int textureIndexOther, int normalIndexOther) {
        return textureIndex == textureIndexOther && normalIndex == normalIndexOther;
    }

    public int index() {
        return INDEX;
    }

    public Vector3f position() {
        return POSITION;
    }

    public float length() {
        return LENGTH;
    }

    public int textureIndex() {
        return textureIndex;
    }

    public void setTextureIndex(int textureIndex) {
        this.textureIndex = textureIndex;
    }

    public int normalIndex() {
        return normalIndex;
    }

    public void setNormalIndex(int normalIndex) {
        this.normalIndex = normalIndex;
    }

    public Vertex duplicateVertex() {
        return duplicateVertex;
    }

    public void setDuplicateVertex(Vertex duplicateVertex) {
        this.duplicateVertex = duplicateVertex;
    }
}
