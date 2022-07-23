package OBJConverter;

/**
 * Record for model data within an OBJ file.
 *
 * @param vertices           Vertices of the model.
 * @param textureCoordinates Texture coordinates of the model.
 * @param normals            Normal vectors of the model.
 * @param indices            Indices of the model.
 * @param furthestPoint      Furthest point in the model.
 */
public record ModelData(float[] vertices, float[] textureCoordinates, float[] normals, int[] indices,
                        float furthestPoint) { }
