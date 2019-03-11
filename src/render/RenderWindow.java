package render;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import static render.RenderFunctions.*;

public class RenderWindow extends JFrame implements KeyListener {
    static final int width = 1920;
    static final int height = 1080;
    boolean showTestOverlay = false;
    RenderWindow() {
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        addKeyListener(this);
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
        render.addModel(Model.readFromObj(
                "models/uaz.obj",
                "models/uaz_d.png",
                "models/uaz_n.png",
                "models/uaz_s.png"));
      /*  render.addModel(Model.readFromObj(
                "models/lamb.obj",
                "models/lamb_d.jpeg",
                null,
                "models/lamb_s.jpeg"));*/
    }

    void render(Graphics g) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        render.render(img);
        g.drawImage(img, 0, 0, null);
        if(showTestOverlay){
            TestOverlay.draw(g);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_Z:
                render.scale = render.scale.scale(1.1f);
                break;
            case KeyEvent.VK_X:
                render.scale = render.scale.scale(0.9f);
                break;
         /*   case KeyEvent.VK_R:
                render.scale = render.scale.mull(new Vector3D(-1, 1, 1));
                break;
            case KeyEvent.VK_F:
                render.scale = render.scale.mull(new Vector3D(1, -1, 1));
                break;
            case KeyEvent.VK_V:
                render.scale = render.scale.mull(new Vector3D(1, 1, -1));
                break;*/


            case KeyEvent.VK_Q:
                render.offset = render.offset.add(new Vector3D(0, 0, -50));
                break;
            case KeyEvent.VK_E:
                render.offset = render.offset.add(new Vector3D(0, 0, 50));
                break;
            case KeyEvent.VK_W:
                render.offset = render.offset.add(new Vector3D(0, 50, 0));
                break;
            case KeyEvent.VK_S:
                render.offset = render.offset.add(new Vector3D(0, -50, 0));
                break;
            case KeyEvent.VK_A:
                render.offset = render.offset.add(new Vector3D(50, 0, 0));
                break;
            case KeyEvent.VK_D:
                render.offset = render.offset.add(new Vector3D(-50, 0, 0));
                break;

            case KeyEvent.VK_R:
                render.rotate = render.rotate.add(new Vector3D((float)(Math.PI / 32), 0, 0));
                break;
            case KeyEvent.VK_F:
                render.rotate = render.rotate.add(new Vector3D(-(float)(Math.PI / 32), 0, 0));
                break;
            case KeyEvent.VK_T:
                render.rotate = render.rotate.add(new Vector3D(0, (float)(Math.PI / 32), 0));
                break;
            case KeyEvent.VK_G:
                render.rotate = render.rotate.add(new Vector3D(0, -(float)(Math.PI / 32), 0));
                break;
            case KeyEvent.VK_Y:
                render.rotate = render.rotate.add(new Vector3D(0, 0, -(float)(Math.PI / 32)));
                break;
            case KeyEvent.VK_H:
                render.rotate = render.rotate.add(new Vector3D(0, 0, (float)(Math.PI / 32)));
                break;

            case KeyEvent.VK_3:
                render.scale = render.scale.scale(0.9f);
                break;
            case KeyEvent.VK_4:
                render.scale = render.scale.scale(1.1f);
                break;
            //camera distance
            case KeyEvent.VK_1:
                render.cameraPosition *= 0.9f;
                break;
            case KeyEvent.VK_2:
                render.cameraPosition *= 1.1f;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
