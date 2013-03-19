package anpaint.Graphics;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

//the Line class is a leaf that describes a straight line
public class Line extends Graphic {

    public Line(Point p1, Point p2, Color colour, boolean style, int weight) {
        _pointSet = new Point[] {p1, p2};
        _colour = colour;
        _style = style;
        _weight = weight;
    }

    @Override
    void addGraphic(Graphic g) {
        throw new UnsupportedOperationException("Operation not supported.");
    }

    @Override
    void removeGraphic(Graphic g) {
        throw new UnsupportedOperationException("Operation not supported.");
    }

    @Override
    ArrayList<Graphic> getChildren() {
        throw new UnsupportedOperationException("Operation not supported.");
    }

    @Override
    void draw() {
        //unfinished implementation
    }

    @Override
    void move(int dx, int dy) {
        for (int i = 0; i < _pointSet.length; i++) {
            _pointSet[i].translate(dx, dy);
        }

        draw();
    }

    @Override
    void resize() {
        //unfinished implementation
        draw();
    }
}