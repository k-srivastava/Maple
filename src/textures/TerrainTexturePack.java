package textures;

/**
 * Record to hold all four textures for the multi-textured terrain.
 *
 * @param backgroundTexture Background texture for the terrain.
 * @param rTexture          First blend texture for the terrain — red in the blend map.
 * @param gTexture          Second blend texture for the terrain — green in the blend map.
 * @param bTexture          Third blend texture for the terrain — blue in the blend map.
 */
public record TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture,
                                 TerrainTexture bTexture) { }
