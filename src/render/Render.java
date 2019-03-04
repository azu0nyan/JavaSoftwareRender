package render;


import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static render.RenderFunctions.*;

enum RenderMode {
    WIRE, RANDOM_COLORS, DIFFUSE
}

public class Render {
    private ArrayList<Model> toRender = new ArrayList<>();

    RenderMode renderMode = RenderMode.DIFFUSE;
    int defaultColor = 0xFF0000;

    float [] zBuffer;
    BufferedImage img;

    Vector3D offset = new Vector3D(1000, 740, 0);
    Vector3D scale = new Vector3D(1f, -1f, -1f).scale(2);

    Vector3D lightDirection = new Vector3D(0, 0, 1);
    Vector3D cameraDirection = new Vector3D(0, 0, 1);

    public void setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode;
    }

    void addModel(Model m) {
        if (m == null) return;
        toRender.add(m);
    }

    void render(BufferedImage img) {
        zBuffer = new float[img.getWidth() * img.getHeight()];
        Arrays.fill(zBuffer, Float.MAX_VALUE);
        this.img = img;
        for (Model m : toRender) {
            renderModel(m);
        }
    }

    void renderModel(Model m) {

        int id = 0;
        switch (renderMode) {
            case WIRE:
                for (int[] triangle : m.triangles) {
                    Vector3D p0 = m.verts[triangle[0]].mull(scale).add(offset);
                    Vector3D p1 = m.verts[triangle[1]].mull(scale).add(offset);
                    Vector3D p2 = m.verts[triangle[2]].mull(scale).add(offset);
                    renderTriangleWireUnsafe((int) p0.x, (int) p0.y, (int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y, defaultColor, img);
                }
                break;
            case DIFFUSE:
                for (int i = 0; i < m.triangles.length; i++) {
                    renderTriangleOnModel(m, i, colorModeGreyScale, lightingModeFlatShading, this);
                }
                break;
            case RANDOM_COLORS:
                for (int[] triangle : m.triangles) {
                    Vector3D p0 = m.verts[triangle[0]].mull(scale).add(offset);
                    Vector3D p1 = m.verts[triangle[1]].mull(scale).add(offset);
                    Vector3D p2 = m.verts[triangle[2]].mull(scale).add(offset);
                    //project
                    Vector2D p0_ = p0.removeZ();
                    Vector2D p1_ = p1.removeZ();
                    Vector2D p2_ = p2.removeZ();
                    renderTriangleColor(p0_, p1_, p2_, new Random(id).nextInt(), img);
                    id++;
                }
                break;
            default:
                System.out.println("Render mode " + renderMode + " not supported yet");
        }


    }


    void testLines(BufferedImage img) {
        for (float a = 0; a < Math.PI * 2; a += Math.PI / 3600) {
            renderLineUnsafe(500, 500, (int) (500 + 400 * Math.sin(a)), (int) (500 + 400 * Math.cos(a)), 0xFFFFFF, img);
        }
    }
    int toZbIndex(int x, int y){
        return x  + y * img.getWidth();
    }
}
