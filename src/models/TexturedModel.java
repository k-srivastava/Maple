package models;

import textures.ModelTexture;

/**
 * Create a new textured model used by the engine to render batches of vertices with a texture to the display.
 *
 * @param rawModel Model containing vertex position and data.
 * @param texture  Texture to be wrapped onto the model.
 */
public record TexturedModel(RawModel rawModel, ModelTexture texture) {
}
