package render;

public class Vector3D {

    private static Vector3D infVector = new Vector3D(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
    float x = 0, y = 0, z = 0;

    static final Vector3D zeroVector = new Vector3D(0,0, 0);
    static final Vector3D xVector = new Vector3D(1,0, 0);
    static final Vector3D yVector = new Vector3D(0,1, 0);
    static final Vector3D zVector = new Vector3D(0,0, 1);

    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Суммирует вектор с аргументом покоординатно
     *
     * @param v слагаемое
     * @return сумма вектора и аргумента
     */
    public Vector3D add(Vector3D v) {
        return new Vector3D(x + v.x, y + v.y, z + v.z);
    }

    /**
     * @param v1 слагаемое 1
     * @param v2 слагаемое 2
     * @return покоординатная сумма v1  + v2
     */
    public static Vector3D add(Vector3D v1, Vector3D v2) {
        return v1.add(v2);
    }

    /**
     * Умножает вектора покоординатно
     *
     * @param v множитель
     * @return произведение вектора и аргумента
     */
    public Vector3D mull(Vector3D v) {
        return new Vector3D(x * v.x, y * v.y, z * v.z);
    }

    /**
     * @param v1 множитель 1
     * @param v2 множитель 2
     * @return покоординатное произведение v1 * v2
     */
    public static Vector3D mull(Vector3D v1, Vector3D v2) {
        return v1.mull(v2);
    }

    /**
     * Вычитает аргумент из вектора покоординатно
     *
     * @param v вычитаемое
     * @return разность вектора и аргумента
     */
    public Vector3D sub(Vector3D v) {
        return new Vector3D(x - v.x, y - v.y, z - v.z);
    }

    /**
     * @param v1 уменьшаемое
     * @param v2 ввычитаемое
     * @return покоординатная разность v1 - v2
     */
    public static Vector3D sub(Vector3D v1, Vector3D v2) {
        return v1.sub(v2);
    }

    /**
     * Делит вектор на аргумент покоординатно
     *
     * @param v делитель
     * @return частное вектора и аргумента
     */
    public Vector3D div(Vector3D v) {
        return new Vector3D(v.x == 0 ? 0 : x / v.x, v.y == 0 ? 0 : y / v.y, v.z == 0 ? 0 : z / v.z);
    }

    /**
     * @param v1 делимое
     * @param v2 делитель
     * @return покоординатное частное v1 / v2
     */
    public static Vector3D div(Vector3D v1, Vector3D v2) {
        return v1.div(v2);
    }

    /**
     * @return строковое предствавление вектора
     */
    @Override
    public String toString() {
        return "{" + x + ", " + y + "," + z + "}";
    }

    /**
     * Масштабирует вектор
     *
     * @param d масштаб
     * @return масштабированный вектор
     */
    public Vector3D scale(float d) {
        return new Vector3D(x * d, y * d, z * d);
    }

    /**
     * Масштабирует вектор
     *
     * @param v вектор
     * @param d масштаб
     * @return масштабированный вектор
     */
    public static Vector3D scale(Vector3D v, float d) {
        return v.scale(d);
    }

    /**
     * @return вектор направленный в противоположную сторону(развернутый на 180 градусов), такой же длинны
     */
    public Vector3D opposite() {
        return scale(-1);
    }

    /**
     * @param v ветор
     * @return вектор направленный в противоположную сторону(развернутый на 180 градусов), такой же длинны
     */
    public static Vector3D opposite(Vector3D v) {
        return v.scale(-1);
    }
//todo rotate around axis
//    public Vector3D rotate(float a) {
//        a *= rotationDirection;
//        return new Vector3D(x * Math.cos(a) + y * Math.sin(a), x * -Math.sin(a) + y * Math.cos(a));
//    }


    /**
     * @return длинна вектора
     */
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * @param v вектор
     * @return длинна вектора
     */
    public static float length(Vector3D v) {
        return v.length();
    }

    /**
     * Нормализует вектор
     *
     * @return вектор длинны 1 смотрящий в том же направлении что и данный
     */
    public Vector3D normalize() {
        if (length() == 0) {
            return new Vector3D(0, 0, 0);
        }
        return scale(1 / length());
    }

    /**
     * Нормализует вектор
     *
     * @param v вектор
     * @return вектор длинны 1 смотрящий в том же направлении что и данный
     */
    public static Vector3D normalize(Vector3D v) {
        return v.normalize();
    }

    /**
     * скалярное произведение
     *
     * @param v множитель
     * @return скалярное произведенние вектора с аргументом функции
     */
    public float scalarProduct(Vector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    /**
     * Скалярное произведение
     *
     * @param v1 множитель 1
     * @param v2 множитель 2
     * @return скалярное произведение v1 и v2
     */
    public static float scalarProduct(Vector3D v1, Vector3D v2) {
        return v1.scalarProduct(v2);
    }
//todo angle
//    public float angle( Vector3D v) {
//        return Math.atan2(v.y, v.x) - Math.atan2(y, x);
//    }


    /**
     * @return координата x вектора
     */
    public float getX() {
        return x;
    }

    /**
     * @return координата y вектора
     */
    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    /**
     * @return координата x вектора, небезопасно т.к. float.MAX_VALUE > Integer.MAX_VALUE
     */
    public int getXInt() {
        return (int) x;
    }

    /**
     * @return координата y вектора небезопасно т.к. float.MAX_VALUE > Integer.MAX_VALUE
     */
    public int getYInt() {
        return (int) y;
    }

    public int getZInt() {
        return (int) z;
    }

    public float distance(Vector3D v) {
        return distance(this, v);
    }

    public static float distance(Vector3D v1, Vector3D v2) {
        return v1.sub(v2).length();
    }

    public Vector3D crossProduct(Vector3D o) {
        return new Vector3D(
                y * o.z - z * o.y,
                z * o.x - x * o.z,
                x * o.y - y * o.x
        );
    }
    public  static Vector3D crossProduct(Vector3D v1, Vector3D v2){
        return v1.crossProduct(v2);
    }
    public Vector2D removeZ(){
        return new Vector2D(x, y);
    }

    public Vector2D from2Homogeneous() {
        return z!= 0 ?new Vector2D(x / z, y / z): Vector2D.infVector;
    }

    public float[] toArray() {
        return new float[]{x, y, z};
    }

    public static Vector3D from3Homogeneous(float [] v){
        return v[3]!= 0 ?new Vector3D(v[0] / v[3], v[1] / v[3], v[2] / v[3] ): Vector3D.infVector;
    }

    public float [] to3Homogeneous(){
        return new float[]{x, y, z, 1};
    }
}