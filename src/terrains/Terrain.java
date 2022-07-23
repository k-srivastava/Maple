package terrains;

import models.RawModel;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

/**
 * Terrain class for the engine.
 */
public class Terrain {
    private static final float SIZE = 800;
    private static final int VERTEX_COUNT = 128;

    private final float X;
    private final float Z;

    private final RawModel RAW_MODEL;
    private final TerrainTexturePack TEXTURE_PACK;
    private final TerrainTexture BLEND_MAP;

    /**
     * Create a new terrain piece at a given location using a given texture.
     *
     * @param gridX       X position of the terrain.
     * @param gridZ       Z position of the terrain.
     * @param loader      Loader class to load the terrain model.
     * @param texturePack Texture pack for all the four textured to be blended onto the terrain.
     * @param blendMap    Texture describing how to blend all the textured on the terrain.
     */
    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap) {
        this.X = gridX * SIZE;
        this.Z = gridZ * SIZE;
        this.TEXTURE_PACK = texturePack;
        this.BLEND_MAP = blendMap;

        this.RAW_MODEL = generateTerrain(loader);
    }

    /**
     * Generate a new terrain piece using a loader to set the vertices, texture coordinates, normals and indices for
     * each piece before loading them to a VAO.
     *
     * @param loader Loader class to generate a raw model.
     * @return Terrain raw model.
     */
    private RawModel generateTerrain(Loader loader) {
        int count = VERTEX_COUNT * VERTEX_COUNT;
        int vertexPointer = 0;
        int pointer = 0;

        float[] vertices = new float[count * 3];
        float[] textureCoordinates = new float[count * 2];
        float[] normals = new float[count * 3];
        int[] indices = new int[(VERTEX_COUNT - 1) * (VERTEX_COUNT - 1) * 6];

        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexPointer * 3] = -(float) j / (VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer * 3 + 1] = 0;
                vertices[vertexPointer * 3 + 2] = -(float) i / (VERTEX_COUNT - 1) * SIZE;

                textureCoordinates[vertexPointer * 2] = (float) j / (VERTEX_COUNT - 1);
                textureCoordinates[vertexPointer * 2 + 1] = (float) i / (VERTEX_COUNT - 1);

                normals[vertexPointer * 3] = 0;
                normals[vertexPointer * 3 + 1] = 1;
                normals[vertexPointer * 3 + 1] = 0;

                vertexPointer++;
            }
        }

        for (int groundZ = 0; groundZ < VERTEX_COUNT - 1; groundZ++) {
            for (int groundX = 0; groundX < VERTEX_COUNT - 1; groundX++) {
                int topLeft = (groundZ * VERTEX_COUNT) + groundX;
                int topRight = topLeft + 1;
                int bottomLeft = ((groundZ + 1) * VERTEX_COUNT) + groundX;
                int bottomRight = bottomLeft + 1;

                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return loader.loadToVAO(vertices, textureCoordinates, normals, indices);
    }

    public float x() {
        return X;
    }

    public float z() {
        return Z;
    }

    public RawModel rawModel() {
        return RAW_MODEL;
    }

    public TerrainTexturePack texturePack() {
        return TEXTURE_PACK;
    }

    public TerrainTexture blendMap() {
        return BLEND_MAP;
    }
}
