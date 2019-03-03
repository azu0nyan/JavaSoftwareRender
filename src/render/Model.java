package render;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Model {
    Vector3D[] verts;
    int [][] triangles;

    public Model(Vector3D[] verts, int[][] triangles) {
        this.verts = verts;
        this.triangles = triangles;
    }

    static Model readFromObj(String filename) {
        try {
            System.out.println("Loading:" + filename);
            Scanner f = new Scanner(new File(filename));
            ArrayList<Vector3D> verts = new ArrayList<>();
            ArrayList<int[]> triangles = new ArrayList<>();
            while (f.hasNextLine()) {
                String str = f.nextLine();
                if(str.isBlank() || str.isEmpty()) continue;
                Scanner current = new Scanner(str);
                String first = current.next();
                if (first.startsWith("#")) continue;
                switch (first) {
                    case "v":
                        Vector3D vertex = new Vector3D(current.nextFloat(), current.nextFloat(), current.nextFloat());
                        verts.add(vertex);
                        break;
                    case "vt"://uv
                        break;
                    case "vn"://normals
                        break;
                    case "vp"://param space
                        break;
                    case "f":
                        int p1 = Integer.parseInt(current.next().split("/")[0]) - 1;
                        int p2 = Integer.parseInt(current.next().split("/")[0]) - 1;
                        int p3 = Integer.parseInt(current.next().split("/")[0]) - 1;
                        int [] triangle = {p1, p2, p3};
                        triangles.add(triangle);
                        break;
                    case "l"://line
                        break;
                    default:
                        break;
                }
            }
            System.out.println("Loaded verts: "  + verts.size()  + ", tris: " + triangles.size());
            return new Model(verts.toArray(new Vector3D[0]), triangles.toArray(new int[0][]));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
