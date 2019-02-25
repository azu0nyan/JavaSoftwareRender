package render;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import static render.RenderFunctions.*;

public class RenderWindow extends JFrame {
    static final int width = 1920;
    static final int height = 1080;

    RenderWindow() {
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
    }

    boolean showFps = true;
    int frameNum = 0;
    long[] lastFramesLength = new long[50];

    void startRendering() {
        setVisible(true);
        createBufferStrategy(2);
        long lastFrame = System.currentTimeMillis();
        while (true) {
            Graphics g = getBufferStrategy().getDrawGraphics();
            render(g);
            if (showFps) {
                long tSum = 0;
                for (int i = 0; i < lastFramesLength.length; i++) {
                    tSum += lastFramesLength[i];
                }
                long fps = tSum / lastFramesLength.length != 0 ? (long) (1000.0 / (tSum / (double) lastFramesLength.length)) : 0;
                g.setColor(Color.RED);
                g.drawString("FPS: " + fps + " frame:" + frameNum, 40, 40);
              //  g.drawOval(300, 300, 400, 400);
            }
            getBufferStrategy().show();
            g.dispose();

            long current = System.currentTimeMillis();
            long currentLength = current - lastFrame;
            lastFrame = current;
            lastFramesLength[frameNum % lastFramesLength.length] = currentLength;
            frameNum++;
        }
    }


    Render render = new Render();
    {
        render.addModel(Model.readFromObj("models/garg.obj"));
    }

    void render(Graphics g) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        render.render(img);
        g.drawImage(img, 0, 0, null);
    }

}
