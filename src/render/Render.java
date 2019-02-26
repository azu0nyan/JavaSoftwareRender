package render;


import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
        int offsetX = 900;
        int offsetY = 800;
        float scaleX = 1;
        float scaleY = -1;
        for (int[] triangle : m.triangles) {
            float[] p0 = m.verts[triangle[0]];
            float[] p1 = m.verts[triangle[1]];
            float[] p2 = m.verts[triangle[2]];
            drawTriangleWireUnsafe(
                    (int) (p0[X] * scaleX + offsetX), (int) (p0[Y] * scaleY + offsetY),
                    (int)(p1[X] * scaleX + offsetX), (int) (p1[Y] * scaleY + offsetY),
                    (int) (p2[X] * scaleX + offsetX), (int) (p2[Y] * scaleY + offsetY), rgb, img);
        }
    }


    void testLines(BufferedImage img) {
        for (float a = 0; a < Math.PI * 2; a += Math.PI / 3600) {
            drawLineUnsafe(500, 500, (int) (500 + 400 * Math.sin(a)), (int) (500 + 400 * Math.cos(a)), 0xFFFFFF, img);
        }
    }
}
