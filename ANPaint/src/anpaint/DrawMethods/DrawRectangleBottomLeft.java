package anpaint.DrawMethods;

import anpaint.BasicShapes.BasicShape;
import java.awt.Graphics;

public class DrawRectangleBottomLeft extends DrawTemplate{

    public DrawRectangleBottomLeft(BasicShape shape, Graphics g) {
        super(shape, g);
    }

    @Override
    void draw(int i, BasicShape shape, Graphics g) {
        g.drawRect(shape._pointSet.get(1).x + i, shape._pointSet.get(1).y - (shape._pointSet.get(1).y - shape._pointSet.get(0).y) + i, shape._pointSet.get(0).x - shape._pointSet.get(1).x - (i * 2), shape._pointSet.get(1).y - shape._pointSet.get(0).y - (i * 2));
    }
}