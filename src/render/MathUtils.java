package render;

import java.awt.*;

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

    static Vector3D transform(Vector3D v, Vector3D offset, Vector3D scale) {
        return v.mull(scale).add(offset);
    }

    static Vector3D toBarycentric(Vector2D a, Vector2D b, Vector2D c, Vector2D p) {
        Vector2D ab = b.sub(a);
        Vector2D ac = c.sub(a);
        Vector2D pa = a.sub(p);
        Vector3D x = new Vector3D(ab.getX(), ac.getX(), pa.getX());
        Vector3D y = new Vector3D(ab.getY(), ac.getY(), pa.getY());
        Vector3D uv1 = x.crossProduct(y);
        Vector3D res = new Vector3D(1f - (uv1.getX() + uv1.getY()) / uv1.getZ(), uv1.getX() / uv1.getZ(), uv1.getY() / uv1.getZ());
        return res;
    }

    static boolean inTriangle(Vector2D a, Vector2D b, Vector2D c, Vector2D p) {
        Vector3D bar = toBarycentric(a, b, c, p);
        return bar.getX() >= 0 && bar.getY() >= 0 && bar.getZ() >= 0;
    }

    static Vector2D weighted(Vector2D a, Vector2D b, Vector2D c, Vector3D weight) {
        return (a.scale(weight.x)).add(b.scale(weight.y)).add(c.scale(weight.z));
    }

    static Vector3D weighted(Vector3D a, Vector3D b, Vector3D c, Vector3D weight) {
        return (a.scale(weight.x)).add(b.scale(weight.y)).add(c.scale(weight.z));
    }

    static int lightColor(int rgb, float intensity) {
        int r = (int) (((rgb & 0x00FF0000) >> 16) * intensity);
        int g = (int) (((rgb & 0x0000FF00) >> 8) * intensity);
        int b = (int) (((rgb & 0x000000FF) >> 0) * intensity);
        return (r << 16) | (g << 8) | b;
    }

}
