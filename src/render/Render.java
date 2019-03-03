package render;


import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static render.RenderFunctions.*;

public class Render {
    private ArrayList<Model> toRender = new ArrayList<>();

    void addModel(Model m) {
        if (m == null) return;
        toRender.add(m);
    }

    void render(BufferedImage img) {
        for (Model m : toRender) {
            renderWireModel(m, 0XFF0000, img);
        }
    }

    void renderWireModel(Model m, int rgb, BufferedImage img) {
        Vector3D offset = new Vector3D(1000, 540, 0);
        Vector3D scale = new Vector3D(-3, -3, 1);
        int id = 0;
        for (int[] triangle : m.triangles) {
            Vector3D p0 = m.verts[triangle[0]].mull(scale).add(offset);
            Vector3D p1 = m.verts[triangle[1]].mull(scale).add(offset);
            Vector3D p2 = m.verts[triangle[2]].mull(scale).add(offset);
            //project
            Vector2D p0_ = p0.removeZ();
            Vector2D p1_ = p1.removeZ();
            Vector2D p2_ = p2.removeZ();
            renderTriangle(p0_,p1_,p2_, new Random(id).nextInt(), img);
            id++;
            /*renderTriangleWireUnsafe(
                    (int) (p0[X] * scaleX + offsetX), (int) (p0[Y] * scaleY + offsetY),
                    (int)(p1[X] * scaleX + offsetX), (int) (p1[Y] * scaleY + offsetY),
                    (int) (p2[X] * scaleX + offsetX), (int) (p2[Y] * scaleY + offsetY), rgb, img);*/
        }
    }


    void testLines(BufferedImage img) {
        for (float a = 0; a < Math.PI * 2; a += Math.PI / 3600) {
            renderLineUnsafe(500, 500, (int) (500 + 400 * Math.sin(a)), (int) (500 + 400 * Math.cos(a)), 0xFFFFFF, img);
        }
    }
}
