package renderEngine;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loader for an OBJ file.
 */
public class OBJLoader {
    /**
     * Load a 3D object from an existing OBJ file.
     *
     * @param filename Filename of the OBJ file.
     * @param loader   Loader class to assist in loading the file.
     * @return Raw model stored in a VAO.
     */
    public static RawModel loadOBJModel(String filename, Loader loader) {
        FileReader fileReader;

        try {
            fileReader = new FileReader("res/" + filename + ".obj");
        }

        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        float[] verticesArray;
        float[] texturesArray;
        float[] normalsArray;
        int[] indicesArray;

        try {
            while (true) {
                line = bufferedReader.readLine();
                String[] currentLine = line.split(" ");

                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3])
                    );

                    vertices.add(vertex);
                }

                else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2])
                    );

                    textures.add(texture);
                }

                else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3])
                    );

                    normals.add(normal);
                }

                else if (line.startsWith("f ")) {
                    texturesArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = bufferedReader.readLine();
                    continue;
                }

                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);

                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        }

        catch (Exception e) {
            throw new RuntimeException(e);
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

        return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
    }

    /**
     * Update the textures and normals array with the correct positions for the vertex information.
     *
     * @param vertexData   Vertex data from the OBJ file.
     * @param indices      List of indices for the model.
     * @param textures     List of textured for the model.
     * @param normals      List of normals for the model.
     * @param textureArray Array of textures for the OBJ file.
     * @param normalsArray Array of normals for the OBJ file.
     */
    private static void processVertex(
            String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals,
            float[] textureArray, float[] normalsArray
    ) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);

        Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currentVertexPointer * 2] = currentTexture.x;
        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y;

        Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertexPointer * 3] = currentNormal.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNormal.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNormal.z;
    }
}
