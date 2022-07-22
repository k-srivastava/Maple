package textures;

/**
 * Class to store a texture ID for a model.
 */
public class ModelTexture {
    private final int textureID;
    private float shineDamping = 1;
    private float reflectivity = 0;

    /**
     * Create a new model texture using a given texture location.
     *
     * @param textureID Location of the texture of the model.
     */
    public ModelTexture(int textureID) { this.textureID = textureID; }

    public int getTextureID() { return textureID; }

    public float getShineDamping() {
        return shineDamping;
    }

    public void setShineDamping(float shineDamping) {
        this.shineDamping = shineDamping;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
