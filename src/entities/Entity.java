package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Entity class containing a textured model, positional and rotational data. Can be rendered to the display.
 */
public class Entity {
    private TexturedModel model;
    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    /**
     * Create a new entity that can be rendered to the display.
     *
     * @param model    Textured model for the entity.
     * @param position Position of the entity.
     * @param rotation Rotation of the entity.
     * @param scale    Scale of the entity.
     */
    public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    /**
     * Displace the entity by a new displacement.
     *
     * @param displacement Displacement by which the entity is to be moved.
     */
    public void displace(Vector3f displacement) {
        Vector3f.add(this.position, displacement, this.position);
    }

    /**
     * Rotate the entity by a new angular displacement.
     *
     * @param angularDisplacement Displacement by which the entity is to be rotated.
     */
    public void rotate(Vector3f angularDisplacement) {
        Vector3f.add(this.rotation, angularDisplacement, this.rotation);
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
