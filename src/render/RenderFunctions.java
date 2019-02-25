package render;

import java.awt.image.BufferedImage;

public class RenderFunctions {

    static final int X = 0;
    static final int Y = 1;
    static final int Z = 2;

    static void drawLineUnsafe(int x1, int y1, int x2, int y2, int rgb, BufferedImage img) {
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
            int  error = 0;
            int dError = dy;
            int y = y1;
            for (int x = x1; x <= x2; x++) {
                img.setRGB(x, y, rgb);
                error += dError;
                if((error<<1) >= dx){
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
            for(int y = y1; y <= y2; y++){
                img.setRGB(x, y, rgb);
                error += dError;
                if(error<<1 >= dy){
                    x += dirX;
                    error -= dy;
                }
            }
        }
    }
}
