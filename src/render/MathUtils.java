package render;

public class MathUtils {
    /*static void crossProduct(float[] a, float[] b, float[] result) {
        result[0] = a[2] * b[3] - a[3] * b[2];
        result[0] = a[3] * b[1] - a[1] * b[3];
        result[0] = a[1] * b[2] - a[2] * b[1];
    }

    static float[] crossProduct(float[] a, float[] b) {
        float[] res = new float[3];
        crossProduct(a, b, res);
        return res;
    }*/
    static Vector2D toBarycentric(Vector2D a, Vector2D b, Vector2D c, Vector2D p){
        Vector2D ab = b.sub(a);
        Vector2D ac = c.sub(a);
        Vector2D pa = a.sub(p);
        Vector3D x = new Vector3D(ab.getX(), ac.getX(), pa.getX());
        Vector3D y = new Vector3D(ab.getY(), ac.getY(), pa.getY());
        Vector3D uv1 = x.crossProduct(y);
        Vector2D res = new Vector2D(uv1.getX() / uv1.getZ(), uv1.getY() / uv1.getZ());
        return res;
    }
    static boolean inTriangle(Vector2D a, Vector2D b, Vector2D c, Vector2D p){
        Vector2D bar = toBarycentric(a, b, c, p);
        return bar.getX() >=0 && bar.getY() >= 0 && (bar.getX() + bar.getY())<1;
    }
}
