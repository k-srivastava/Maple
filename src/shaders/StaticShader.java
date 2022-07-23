package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.EngineMath;

/**
 * Implementation of the ShaderProgram for pre-written, static shaders.
 */
public class StaticShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/shaders/glsl/vertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/shaders/glsl/fragmentShader.glsl";

    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;

    private int lightPositionLocation;
    private int lightColorLocation;
    private int shineDampingLocation;
    private int reflectivityLocation;
    private int useFakeLightingLocation;

    private int skyColorLocation;

    /**
     * Create a new static shader using pre-written GLSL vertex and fragment shaders.
     */
    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Load a 4x4 transformation matrix within the shader.
     *
     * @param matrix Transformation matrix to be loaded.
     */
    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix4f(transformationMatrixLocation, matrix);
    }

    /**
     * Load a 4x4 projection matrix within the shader.
     *
     * @param matrix Projection matrix to be loaded.
     */
    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix4f(projectionMatrixLocation, matrix);
    }

    /**
     * Load a 4x4 view matrix within the shader.
     *
     * @param camera Camera to use when creating a view matrix.
     */
    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = EngineMath.createViewMatrix(camera);
        super.loadMatrix4f(viewMatrixLocation, viewMatrix);
    }

    /**
     * Load a light with its position and color within the shader.
     *
     * @param light Light to be loaded.
     */
    public void loadLight(Light light) {
        super.loadVector3f(lightPositionLocation, light.getPosition());
        super.loadVector3f(lightColorLocation, light.getColor());
    }

    /**
     * Load specular light information within the shader.
     *
     * @param damping      Damping of the specular lighting to be loaded.
     * @param reflectivity Reflectivity of the texture to be loaded.
     */
    public void loadSpecularLightData(float damping, float reflectivity) {
        super.loadFloat(shineDampingLocation, damping);
        super.loadFloat(reflectivityLocation, reflectivity);
    }

    /**
     * Load a boolean if fake vertical lighting is to be used or not within the shader.
     *
     * @param useFakeLighting Whether to use fake vertical lighting or not to be loaded.
     */
    public void loadFakeLighting(boolean useFakeLighting) {
        super.loadBoolean(useFakeLightingLocation, useFakeLighting);
    }

    /**
     * Load a vector representing the sky color in RGB within the shader.
     *
     * @param color Sky color to be loaded.
     */
    public void loadSkyColor(Vector3f color) {
        super.loadVector3f(skyColorLocation, color);
    }

    /**
     * Bind the particular attributes within the current shader.
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normal");
    }

    /**
     * Get the uniform locations of all the uniform variables within the current shader.
     */
    @Override
    protected void getAllUniformLocations() {
        transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
        projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
        viewMatrixLocation = super.getUniformLocation("viewMatrix");

        lightPositionLocation = super.getUniformLocation("lightPosition");
        lightColorLocation = super.getUniformLocation("lightColor");
        shineDampingLocation = super.getUniformLocation("shineDamping");
        reflectivityLocation = super.getUniformLocation("reflectivity");
        useFakeLightingLocation = super.getUniformLocation("useFakeLighting");

        skyColorLocation = super.getUniformLocation("skyColor");
    }
}
