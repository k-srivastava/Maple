package shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Abstract layout for a shader program in OpenGL consisting of a vertex and fragment shader.
 */
public abstract class ShaderProgram {
    private final int PROGRAM_ID;
    private final int VERTEX_SHADER_ID;
    private final int FRAGMENT_SHADER_ID;

    /**
     * Create a new shader program.
     *
     * @param vertexFile File path for the vertex shader.
     * @param fragmentFile File path for the fragment shader.
     */
    public ShaderProgram(String vertexFile, String fragmentFile) {
        VERTEX_SHADER_ID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        FRAGMENT_SHADER_ID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        PROGRAM_ID = GL20.glCreateProgram();

        GL20.glAttachShader(PROGRAM_ID, VERTEX_SHADER_ID);
        GL20.glAttachShader(PROGRAM_ID, FRAGMENT_SHADER_ID);
        GL20.glLinkProgram(PROGRAM_ID);
        GL20.glValidateProgram(PROGRAM_ID);

        bindAttributes();
    }

    /**
     * Start using the shader program.
     */
    public void start() {
        GL20.glUseProgram(PROGRAM_ID);
    }

    /**
     * Stop using the shader program.
     */
    public void stop() {
        GL20.glUseProgram(0);
    }

    /**
     * Stop using the shader program and detach and delete both the vertex and fragment shaders before deleting the program.
     */
    public void cleanUp() {
        stop();

        GL20.glDetachShader(PROGRAM_ID, VERTEX_SHADER_ID);
        GL20.glDetachShader(PROGRAM_ID, FRAGMENT_SHADER_ID);

        GL20.glDeleteShader(VERTEX_SHADER_ID);
        GL20.glDeleteShader(FRAGMENT_SHADER_ID);

        GL20.glDeleteProgram(PROGRAM_ID);
    }

    /**
     * Abstract layout to multiple variable names with specific data.
     */
    protected abstract void bindAttributes();

    /**
     * Bind a particular attribute with a specific variable name in the shader program.
     *
     * @param attribute Attribute to be bound.
     * @param variableName Name of the variable to which the attribute is bound.
     */
    protected void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(PROGRAM_ID, attribute, variableName);
    }

    /**
     * Load a specific GLSL shader in OpenGL.
     *
     * @param file File path of the shader to be loaded.
     * @param type Type of the shader to be loaded — vertex or fragment shaders.
     * @return ID of the newly loaded shader.
     */
    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("Could not read file!");
            e.printStackTrace();
            System.exit(-1);
        }

        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }

        return shaderID;
    }
}
