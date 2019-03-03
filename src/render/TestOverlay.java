package render;

import java.awt.*;

public class TestOverlay {
    static void draw(Graphics g){
        g.setColor(Color.WHITE);
        Vector2D a = new Vector2D(100, 100);
        Vector2D b = new Vector2D(800, 200);
        Vector2D c = new Vector2D(600, 500);
        g.drawLine(a.getXInt(), a.getYInt(), b.getXInt(), b.getYInt());
        g.drawLine(a.getXInt(), a.getYInt(), c.getXInt(), c.getYInt());
        g.drawLine(c.getXInt(), c.getYInt(), b.getXInt(), b.getYInt());
        Vector2D p = new Vector2D(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
        g.drawString(MathUtils.toBarycentric(a,b, c, p).toString(), p.getXInt(), p.getYInt());
    }

}
