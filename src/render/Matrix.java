package render;

public class Matrix {

    static float[][] mull(float[][] a, float[][] b) {
        final int n = a.length;
        float res[][] = new float[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    res[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return res;
    }

    static Vector2D mull(float[][] a, Vector2D v) {
        return new Vector2D(
                v.x * a[0][0] + v.y * a[0][1],
                v.x * a[1][0] + v.y * a[1][1]);
    }

    static Vector3D mull(float[][] a, Vector3D v) {
        return new Vector3D(
                v.x * a[0][0] + v.y * a[0][1] + v.z * a[0][2],
                v.x * a[1][0] + v.y * a[1][1] + v.z * a[1][2],
                v.x * a[2][0] + v.y * a[2][1] + v.z * a[2][2]);
    }

    static float[] mull(float[][] a, float[] v) {
        float res[] = new float[v.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < v.length; j++) {
                res[i] += a[i][j] * v[j];
            }
        }
        return res;
    }

    //diagonal matrix
    static float[][] scaleMatrix(float[] scaleCoeffs) {
        float[][] res = new float[scaleCoeffs.length][scaleCoeffs.length];
        for (int i = 0; i < scaleCoeffs.length; i++) {
            res[i][i] = scaleCoeffs[i];
        }
        return res;
    }

    static float[][] scaleMatrix(Vector2D v) {
        return scaleMatrix(new float[]{v.x, v.y});
    }

    static float[][] scaleMatrix(Vector3D v) {
        return scaleMatrix(new float[]{v.x, v.y, v.z});
    }

    static float[][] rotationMatrix2x2(float a) {
        return new float[][]{
                {(float) Math.cos(a), (float) -Math.sin(a)},
                {(float) Math.sin(a), (float) Math.cos(a)},
        };
    }

    static float[][] rotationMatrix2x2Homo(float a) {
        return new float[][]{
                {(float) Math.cos(a), (float) -Math.sin(a), 0},
                {(float) Math.sin(a), (float) Math.cos(a), 0},
                {0, 0, 1},
        };
    }

    /**
     * @param axises оси и перемещение по ним
     * @return матрица трансфомации для работы в гомогенных координатах
     */
    static float[][] homoTransformMatrix(float[] axises) {
        float[][] res = new float[axises.length + 1][axises.length + 1];
        for (int i = 0; i < axises.length; i++) {
            res[i][axises.length] = axises[i];
        }
        for (int i = 0; i < res.length; i++) {
            res[i][i] = 1;
        }
        return res;
    }

    static float[][] cameraZPr(float cameraPosition) {
        return new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, -1 / cameraPosition, 1}
        };
    }
    static float[][] rotateX(float a) {
        return new float[][]{
                {1, 0, 0, 0},
                {0, (float) Math.cos(a), (float) -Math.sin(a), 0},
                {0, (float) Math.sin(a), (float) Math.cos(a), 0},
                {0, 0, 0, 1}
        };
    }
    static float[][] rotateY(float a) {
        return new float[][]{
                {(float) Math.cos(a), 0, (float) Math.sin(a), 0},
                {0, 1, 0, 0},
                {(float) -Math.sin(a), 0, (float) Math.cos(a), 0},
                {0, 0, 0, 1}
        };
    }
    static float[][] rotateZ(float a) {
        return new float[][]{
                {(float) Math.cos(a), (float) -Math.sin(a), 0, 0},
                {(float) Math.sin(a), (float) Math.cos(a), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
    }
}
