package toolbox;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Commonly used Mathematical operations used within the engine.
 */
public class EngineMath {
    /**
     * Create a new transformation matrix with specified values.
     *
     * @param translation Translation of the matrix.
     * @param rotation    Rotation of the body in the matrix.
     * @param scale       Scale of the matrix.
     * @return New transformation matrix.
     */
    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();

        matrix.translate(translation);
        matrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
        matrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        matrix.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1));
        matrix.scale(new Vector3f(scale, scale, scale));

        return matrix;
    }

    /**
     * Create a new view matrix with the camera.
     *
     * @param camera Camera for which a view matrix is to be created.
     * @return New view matrix.
     */
    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        Vector3f cameraPosition = camera.getPosition();
        Vector3f negativeCameraPosition = new Vector3f(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);

        viewMatrix.setIdentity();
        viewMatrix.rotate((float) Math.toRadians(camera.getRotation().x), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(camera.getRotation().y), new Vector3f(0, 1, 0));
        viewMatrix.translate(negativeCameraPosition);

        return viewMatrix;
    }
}
