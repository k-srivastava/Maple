package OBJConverter;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage the loading of OBJ files in the engine.
 */
public class OBJFileLoader {
    private static final String RES_DIRECTORY_PATH = "res/";

    /**
     * Load an OBJ file in the "res" directory into new model data.
     *
     * @param filename Name of the OBJ file.
     * @return Model data of the OBJ file.
     */
    public static ModelData loadOBJ(String filename) {
        FileReader fileReader = null;
        File objFile = new File(RES_DIRECTORY_PATH + filename + ".obj");

        try {
            fileReader = new FileReader(objFile);
        }

        catch (FileNotFoundException e) {
            System.err.println("File not found in 'res' folder. Don't use any extension.");
        }

        assert fileReader != null;
        BufferedReader reader = new BufferedReader(fileReader);
        String line;

        List<Vertex> vertices = new ArrayList<>();
        List<Vector2f> textureCoordinates = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        try {
            while (true) {
                line = reader.readLine();

                if (line.startsWith("v ")) {
                    String[] currentLine = line.split(" ");

                    Vector3f vertex = new Vector3f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3])
                    );

                    Vertex newVertex = new Vertex(vertices.size(), vertex);
                    vertices.add(newVertex);
                }

                else if (line.startsWith("vt ")) {
                    String[] currentLine = line.split(" ");

                    Vector2f textureCoordinate = new Vector2f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2])
                    );

                    textureCoordinates.add(textureCoordinate);
                }

                else if (line.startsWith("vn ")) {
                    String[] currentLine = line.split(" ");

                    Vector3f normal = new Vector3f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3])
                    );

                    normals.add(normal);
                }

                else if (line.startsWith("f ")) break;
            }

            while (line != null && line.startsWith("f ")) {
                String[] currentLine = line.split(" ");

                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, vertices, indices);
                processVertex(vertex2, vertices, indices);
                processVertex(vertex3, vertices, indices);

                line = reader.readLine();
            }

            reader.close();
        }

        catch (IOException e) {
            System.err.println("Error reading the file.");
        }

        removeUnusedVertices(vertices);

        float[] verticesArray = new float[vertices.size() * 3];
        float[] textureCoordinatesArray = new float[vertices.size() * 2];
        float[] normalsArray = new float[vertices.size() * 3];

        int[] indicesArray = convertIndicesListToArray(indices);
        float furthest = convertDataToArrays(
                vertices, textureCoordinates, normals,
                verticesArray, textureCoordinatesArray, normalsArray
        );

        return new ModelData(verticesArray, textureCoordinatesArray, normalsArray, indicesArray, furthest);
    }

    /**
     * Load an OBJ file in the "res" directory into a new complete model.
     *
     * @param filename Name of the OBJ file.
     * @param loader   Loader class to load the object into a VAO.
     * @return Object model data loaded into a VAO.
     */
    public static RawModel loadOBJToVAO(String filename, Loader loader) {
        ModelData data = loadOBJ(filename);
        return loader.loadToVAO(data.vertices(), data.textureCoordinates(), data.normals(), data.indices());
    }

    /**
     * Update the textures and normals array with the correct positions for the vertex information.
     *
     * @param vertex   Vertex from the OBJ file.
     * @param vertices List of vertices for the model.
     * @param indices  List of indices for the model.
     */
    private static void processVertex(String[] vertex, List<Vertex> vertices, List<Integer> indices) {
        int index = Integer.parseInt(vertex[0]) - 1;
        Vertex currentVertex = vertices.get(index);

        int textureCoordinateIndex = Integer.parseInt(vertex[1]) - 1;
        int normalIndex = Integer.parseInt(vertex[2]) - 1;

        if (currentVertex.isNotSet()) {
            currentVertex.setTextureIndex(textureCoordinateIndex);
            currentVertex.setNormalIndex(normalIndex);
            indices.add(index);
        }

        else {
            manageProcessedVertex(currentVertex, textureCoordinateIndex, normalIndex, indices, vertices);
        }
    }

    /**
     * Convert a Java Integer list into a Java int array.
     *
     * @param indices List of indices for the model.
     * @return Array of indices for the model.
     */
    private static int[] convertIndicesListToArray(List<Integer> indices) {
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indicesArray.length; i++) indicesArray[i] = indices.get(i);

        return indicesArray;
    }

    /**
     * Convert Java lists into Java arrays at scale.
     *
     * @param vertices                List of vertices for the model.
     * @param textureCoordinates      List of texture coordinates for the model.
     * @param normals                 List of normal vectors for the model.
     * @param verticesArray           Array of vertices for the model.
     * @param textureCoordinatesArray Array of texture coordinates for the model.
     * @param normalsArray            Array of normals for the model.
     * @return Furthest point within the model.
     */
    private static float convertDataToArrays(
            List<Vertex> vertices, List<Vector2f> textureCoordinates, List<Vector3f> normals, float[] verticesArray,
            float[] textureCoordinatesArray, float[] normalsArray
    ) {
        float furthestPoint = 0;

        for (int i = 0; i < vertices.size(); i++) {
            Vertex currentVertex = vertices.get(i);

            if (currentVertex.getLength() > furthestPoint) furthestPoint = currentVertex.getLength();

            Vector3f position = currentVertex.getPosition();
            Vector2f textureCoordinate = textureCoordinates.get(currentVertex.getTextureIndex());
            Vector3f normalVector = normals.get(currentVertex.getNormalIndex());

            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;

            textureCoordinatesArray[i * 2] = textureCoordinate.x;
            textureCoordinatesArray[i * 2 + 1] = 1 - textureCoordinate.y;

            normalsArray[i * 3] = normalVector.x;
            normalsArray[i * 3 + 1] = normalVector.y;
            normalsArray[i * 3 + 2] = normalVector.z;
        }

        return furthestPoint;
    }

    /**
     * Manage an already processed vertex.
     *
     * @param previousVertex            Previous vertex to be managed.
     * @param newTextureCoordinateIndex New texture coordinate index for the vertex.
     * @param newNormalIndex            New normal vector index for the vertex.
     * @param indices                   List of indices for the model.
     * @param vertices                  List of vertices for the model.
     */
    private static void manageProcessedVertex(
            Vertex previousVertex, int newTextureCoordinateIndex, int newNormalIndex, List<Integer> indices,
            List<Vertex> vertices
    ) {
        if (previousVertex.hasSameTextureAndNormal(newTextureCoordinateIndex, newNormalIndex)) {
            indices.add(previousVertex.getIndex());
        }

        else {
            Vertex duplicateVertex = previousVertex.getDuplicateVertex();

            if (duplicateVertex != null) {
                manageProcessedVertex(duplicateVertex, newTextureCoordinateIndex, newNormalIndex, indices, vertices);
            }

            else {
                Vertex secondDuplicate = new Vertex(vertices.size(), previousVertex.getPosition());
                secondDuplicate.setTextureIndex(newTextureCoordinateIndex);
                secondDuplicate.setNormalIndex(newNormalIndex);

                previousVertex.setDuplicateVertex(secondDuplicate);
                vertices.add(secondDuplicate);
                indices.add(secondDuplicate.getIndex());
            }
        }
    }

    /**
     * Remove any unused vertices from the model data.
     *
     * @param vertices List of vertices for the model.
     */
    private static void removeUnusedVertices(List<Vertex> vertices) {
        for (Vertex vertex : vertices) {
            if (vertex.isNotSet()) {
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
    }
}
