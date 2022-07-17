package shaders;

/**
 * Implementation of the ShaderProgram for pre-written, static shaders.
 */
public class StaticShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

    /**
     * Create a new static shader using pre-written GLSL vertex and fragment shaders.
     */
    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
