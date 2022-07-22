package shaders;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import toolbox.EngineMath;

/**
 * Implementation of the ShaderProgram for pre-written, static shaders.
 */
public class StaticShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;

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
     * Bind the particular attributes within the current shader.
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
    }

    /**
     * Get the uniform locations of all the uniform variables within the current shader.
     */
    @Override
    protected void getAllUniformLocations() {
        transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
        projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
        viewMatrixLocation = super.getUniformLocation("viewMatrix");
    }
}
