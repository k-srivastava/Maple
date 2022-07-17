package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Renderer for the engine to render all the raw models to the display.
 */
public class Renderer {
    /**
     * Prepare the renderer to clear the current color on the display. Called before every frame.
     */
    public void prepare() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClearColor(1, 0, 0, 1);
    }

    /**
     * Render a raw model to the display by first binding a VAO and enabling it before drawing the triangles for
     * the model. Then the VAO is disabled and unbound.
     *
     * @param model Raw model to be rendered to the display.
     */
    public void render(RawModel model) {
        GL30.glBindVertexArray(model.vaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.vertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
}
