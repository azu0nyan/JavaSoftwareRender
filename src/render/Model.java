package render;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Model {
    Vector3D[] verts;
    int[][] triangles;
    int[][] triangleUVs;
    int[][] triangleNormals;
    Vector2D[] uvs;
    Vector3D[] normals;
    boolean hasUV = false;
    boolean hasNormals = false;

    BufferedImage diffuseTexture = null;
    BufferedImage normalTexture = null;
    BufferedImage specularTexture = null;

    public Model(Vector3D[] verts, int[][] triangles, int[][] triangleUvs, int[][] triangleNormals, Vector2D[] uvs, Vector3D[] normals) {
        this.verts = verts;
        this.triangles = triangles;
        this.triangleUVs = triangleUvs;
        this.triangleNormals = triangleNormals;
        this.uvs = uvs;
        this.normals = normals;
        hasNormals = triangles.length == triangleNormals.length;
        hasUV = triangles.length == uvs.length;
    }

    static Model readFromObj(String modelFileName, String diffuseTextureFileName, String normalTextureFileName, String specularTextureFileName) {
        String lastLine = "";
        try {
            System.out.println("Loading:" + modelFileName);
            File fromFile = new File(modelFileName);
            long probableLength =  fromFile.length() / 38;
            int line = 0;
            System.out.printf("Size: %.1fMb ~%d lines\n", fromFile.length() / (1024.0 * 1024.0), fromFile.length() / 19);

            Scanner f = new Scanner(fromFile);

            ArrayList<Vector3D> verts = new ArrayList<>();
            ArrayList<int[]> triangles = new ArrayList<>();
            ArrayList<int[]> triangleUVs = new ArrayList<>();
            ArrayList<int[]> triangleVNs = new ArrayList<>();
            ArrayList<Vector2D> uvs = new ArrayList<>();
            ArrayList<Vector3D> normals = new ArrayList<>();
            //for logging
            boolean missedNormals = false;
            boolean missedUVs = false;
            while (f.hasNextLine()) {
                if(line % 100_000 == 0){
                    System.out.printf("Reading line:%10d %.1f\n", line, 100.0 * line / probableLength);
                }
                String str = f.nextLine();
                lastLine = str;//to Logging
                line++;

                if (str.isBlank() || str.isEmpty()) continue;

                Scanner current = new Scanner(str);
                String first = current.next();

                if (first.startsWith("#")) continue;

                switch (first) {
                    case "v":
                        Vector3D vertex = new Vector3D(Float.parseFloat(current.next()), Float.parseFloat(current.next()), Float.parseFloat(current.next()));
                        verts.add(vertex);
                        break;
                    case "vt"://uv
                        Vector2D uv = new Vector2D(Float.parseFloat(current.next()), Float.parseFloat(current.next()));
                        uvs.add(uv);
                        break;
                    case "vn"://normals
                        Vector3D normal = new Vector3D(Float.parseFloat(current.next()), Float.parseFloat(current.next()), Float.parseFloat(current.next()));
                        normals.add(normal);
                        break;
                    case "vp"://param space
                        break;
                    case "f":
                        String s1 [] = current.next().split("/");
                        String s2 [] = current.next().split("/");
                        String s3 [] = current.next().split("/");
                        int p1 = Integer.parseInt(s1[0]) - 1;
                        int p2 = Integer.parseInt(s2[0]) - 1;
                        int p3 = Integer.parseInt(s3[0]) - 1;
                        int[] triangle = {p1, p2, p3};
                        triangles.add(triangle);
                        if(s1.length < 2 || s2.length <2 || s3.length < 2){
                            if(!missedUVs){
                                missedUVs = true;
                                System.out.println("Missed uvs in " + modelFileName);
                            }
                            continue;
                        }
                        if(!s1[1].isEmpty()  && !s2[1].isEmpty() && !s3[1].isEmpty()) {
                            int uv1 = Integer.parseInt(s1[1]) - 1;
                            int uv2 = Integer.parseInt(s2[1]) - 1;
                            int uv3 = Integer.parseInt(s3[1]) - 1;
                            int[] triangleUV = {uv1, uv2, uv3};
                            triangleUVs.add(triangleUV);
                        }
                        if(s1.length < 3 || s2.length <3 || s3.length < 3) {
                            if(!missedNormals){
                                missedNormals = true;
                                System.out.println("Missed normals in " + modelFileName);
                            }
                            continue;
                        }
                        int vn1 = Integer.parseInt(s1[2]) - 1;
                        int vn2 = Integer.parseInt(s2[2]) - 1;
                        int vn3 = Integer.parseInt(s3[2]) - 1;
                        int [] triangleVN = {vn1, vn2, vn3};
                        triangleVNs.add(triangleVN);
                        break;
                    case "l"://line
                        break;
                    default:
                        break;
                }
            }
            System.out.println("Loaded verts: " + verts.size() + ", tris: " + triangles.size());
            Model m =  new Model(verts.toArray(new Vector3D[0]),
                    triangles.toArray(new int[0][]), triangleUVs.toArray(new int[0][]), triangleVNs.toArray(new int[0][]),
                    uvs.toArray(new Vector2D[0]), normals.toArray(new Vector3D[0]));
            if(diffuseTextureFileName != null){
                m.diffuseTexture = ImageIO.read(new File(diffuseTextureFileName));
            }
            if(normalTextureFileName != null){
                m.normalTexture = ImageIO.read(new File(normalTextureFileName));
            }
            if(specularTextureFileName != null){
                m.specularTexture = ImageIO.read(new File(specularTextureFileName));
            }
            return m;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e ) {
            System.out.println("Last line: " + lastLine);
            e.printStackTrace();
        }
        return null;
    }

    int getDiffuseAt(Vector2D uv){
        return diffuseTexture != null ?  diffuseTexture.getRGB((int)(diffuseTexture.getWidth() *  uv.getX()), (int)(diffuseTexture.getHeight() * (1f - uv.getY()))): 0xFF;
    }
}
