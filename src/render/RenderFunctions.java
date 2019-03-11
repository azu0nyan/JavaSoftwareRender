package render;

import java.awt.image.BufferedImage;

import static render.MathUtils.*;

public class RenderFunctions {

    static void renderTriangleColor(Vector2D a, Vector2D b, Vector2D c, int rgb, BufferedImage img) {
        int minX = Math.max(0, Math.min(a.getXInt(), Math.min(b.getXInt(), c.getXInt())));
        int minY = Math.max(0, Math.min(a.getYInt(), Math.min(b.getYInt(), c.getYInt())));
        int maxX = Math.min(img.getWidth() - 1, Math.max(a.getXInt(), Math.max(b.getXInt(), c.getXInt())));
        int maxY = Math.min(img.getHeight() - 1, Math.max(a.getYInt(), Math.max(b.getYInt(), c.getYInt())));
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (MathUtils.inTriangle(a, b, c, new Vector2D(x, y))) {
                    img.setRGB(x, y, rgb);
                }
            }
        }
    }

    static final int colorModeGreyScale = 0;
    static final int colorModeDiffuse = 1;

    static final int lightingModeNoLighting = 0;
    static final int lightingModeFlatShading = 1;
    static final int lightingModeGouraud = 2;

    static void renderTriangleOnModel(Model m, int id, int colorMode, int lightingMode, Render worldInfo) {
        Vector3D a = m.verts[m.triangles[id][0]];
        Vector3D b = m.verts[m.triangles[id][1]];
        Vector3D c = m.verts[m.triangles[id][2]];

       /* a = transform(a, worldInfo.offset, worldInfo.scale);
        b = transform(b, worldInfo.offset, worldInfo.scale);
        c = transform(c, worldInfo.offset, worldInfo.scale);
        */
       a = transform( worldInfo.transformMatrix, a);
       b = transform( worldInfo.transformMatrix, b);
       c = transform( worldInfo.transformMatrix, c);


        //backFace culling
        Vector3D ab = b.sub(a);
        Vector3D ac = b.sub(c);
        Vector3D triangleNormal = Vector3D.crossProduct(ab, ac).normalize();
        float scalarProduct = Vector3D.scalarProduct(triangleNormal, worldInfo.cameraDirection);
        if (scalarProduct <= 0) return;


        //project on screen
        Vector2D a2 = a.removeZ();
        Vector2D b2 = b.removeZ();
        Vector2D c2 = c.removeZ();


        //clamp probable triangle area
        int minX = Math.max(0, Math.min(a2.getXInt(), Math.min(b2.getXInt(), c2.getXInt())));
        int minY = Math.max(0, Math.min(a2.getYInt(), Math.min(b2.getYInt(), c2.getYInt())));
        int maxX = Math.min(worldInfo.img.getWidth() - 1, Math.max(a2.getXInt(), Math.max(b2.getXInt(), c2.getXInt())));
        int maxY = Math.min(worldInfo.img.getHeight() - 1, Math.max(a2.getYInt(), Math.max(b2.getYInt(), c2.getYInt())));

        //prepare textureCordsIfNeeded
        Vector2D at = colorMode == colorModeDiffuse ? m.uvs[m.triangleUVs[id][0]] : Vector2D.zeroVector;
        Vector2D bt = colorMode == colorModeDiffuse ? m.uvs[m.triangleUVs[id][1]] : Vector2D.zeroVector;
        Vector2D ct = colorMode == colorModeDiffuse ? m.uvs[m.triangleUVs[id][2]] : Vector2D.zeroVector;

        //calc lightIntensity at points if needed
        Vector3D an = lightingMode == lightingModeGouraud ? m.normals[m.triangleNormals[id][0]].mull(worldInfo.scale).normalize() : Vector3D.zeroVector;
        Vector3D bn = lightingMode == lightingModeGouraud ? m.normals[m.triangleNormals[id][1]].mull(worldInfo.scale).normalize() : Vector3D.zeroVector;
        Vector3D cn = lightingMode == lightingModeGouraud ? m.normals[m.triangleNormals[id][2]].mull(worldInfo.scale).normalize() : Vector3D.zeroVector;

        float aLightIntensity = Vector3D.scalarProduct(an, worldInfo.lightDirection);
        float bLightIntensity = Vector3D.scalarProduct(bn, worldInfo.lightDirection);
        float cLightIntensity = Vector3D.scalarProduct(cn, worldInfo.lightDirection);

        //Vector3D pNormal = weighted(an, bn, cn, bar).normalize();
        //lightIntecicity = Vector3D.scalarProduct(pNormal, worldInfo.lightDirection);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                Vector2D p = new Vector2D(x, y);
                Vector3D bar = toBarycentric(a2, b2, c2, p);
                if (bar.x >= 0 && bar.y >= 0 && bar.z >= 0) {
                   // Vector3D placeIn3D = weighted(a, b, c, bar);
                    Vector3D placeIn3D = a.add(Vector3D.add(ab.scale(bar.y), ac.scale(bar.z)));
                   // System.out.println(placeIn3D.z);
                    if (placeIn3D.z > worldInfo.zBuffer[worldInfo.toZbIndex(x, y)]) {
                        //get diffuse Color
                        int rgb = worldInfo.defaultColor;
                        switch (colorMode) {
                            case colorModeGreyScale:
                                rgb = 0xFFFFFF;
                                break;
                            case colorModeDiffuse:
                                //texture cords
                                Vector2D uv = weighted(at, bt, ct, bar);
                                rgb = m.getDiffuseAt(uv);
                                break;
                        }
                        float lightIntensity = 0;
                        switch (lightingMode) {
                            case lightingModeFlatShading:
                                lightIntensity = Vector3D.scalarProduct(triangleNormal, worldInfo.lightDirection);
                                break;
                            case lightingModeNoLighting:
                                lightIntensity = 1f;
                                break;
                            case lightingModeGouraud:
                                lightIntensity = aLightIntensity * bar.x + bLightIntensity * bar.y + cLightIntensity * bar.z;
                                break;
                        }
                        rgb = lightColor(rgb, lightIntensity);
                        //write color
                        worldInfo.img.setRGB(x, y, rgb);
                        //write zBuffer
                        worldInfo.zBuffer[worldInfo.toZbIndex(x, y)] = placeIn3D.z;
                    }
                }
            }
        }
    }


    static void renderTriangleWireUnsafe(int x1, int y1, int x2, int y2, int x3, int y3, int rgb, BufferedImage img) {
        renderLineUnsafe(x1, y1, x2, y2, rgb, img);
        renderLineUnsafe(x1, y1, x3, y3, rgb, img);
        renderLineUnsafe(x2, y2, x3, y3, rgb, img);
    }

    static void renderLineUnsafe(int x1, int y1, int x2, int y2, int rgb, BufferedImage img) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        if (dx >= dy) {
            if (x2 < x1) {
                int tx = x1;
                x1 = x2;
                x2 = tx;
                int ty = y1;
                y1 = y2;
                y2 = ty;
            }
            int dirY = (int) Math.signum(y2 - y1);
            int error = 0;
            int dError = dy;
            int y = y1;
            for (int x = x1; x <= x2; x++) {
                img.setRGB(x, y, rgb);
                error += dError;
                if ((error << 1) >= dx) {
                    y += dirY;
                    error -= dx;
                }
            }
        } else {
            if (y2 < y1) {
                int tx = x1;
                x1 = x2;
                x2 = tx;
                int ty = y1;
                y1 = y2;
                y2 = ty;
            }
            int dirX = (int) Math.signum(x2 - x1);
            int error = 0;
            int dError = dx;
            int x = x1;
            for (int y = y1; y <= y2; y++) {
                img.setRGB(x, y, rgb);
                error += dError;
                if (error << 1 >= dy) {
                    x += dirX;
                    error -= dy;
                }
            }
        }
    }
}
