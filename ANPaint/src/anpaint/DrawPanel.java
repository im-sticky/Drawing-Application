package anpaint;

import anpaint.BasicShapes.*;
import anpaint.Commands.DrawCommand;
import anpaint.Creators.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The DrawPanel class will contain the implementations of the various commands
 * that a user can do. It acts as a receiver to the menu commands
 *
 * This is part of the Command Pattern
 */
public class DrawPanel extends JPanel {

    private ArrayList<BasicShape> _shapeSet;
    private Point _point;
    private AppWindow _window;
    private RectangleShapeCreator _rectangleFactory;
    private CircleShapeCreator _circleFactory;
    private LineShapeCreator _lineFactory;
    private TriangleShapeCreator _triangleFactory;
    Graphics _g;
    private ArrayList<BasicShape> _backup;
    private ArrayList<BasicShape> _copyBuffer;
    private ArrayList<BasicShape> _copyBufferBackup;
    private PanelState _state;

    public DrawPanel(AppWindow app) {
        _shapeSet = new ArrayList<>();
        _backup = new ArrayList<>();
        _copyBuffer = new ArrayList<>();
        _copyBufferBackup = new ArrayList<>();
        _shapeSet = new ArrayList<>();
        _state = PanelState.DRAW;
        //_window = AppWindow.getInstance();      /** not returning the unique instance **/
        _window = app;
        /**
         * this is a temp fix *
         */
        _rectangleFactory = new RectangleShapeCreator();
        _circleFactory = new CircleShapeCreator();
        _lineFactory = new LineShapeCreator();
        _triangleFactory = new TriangleShapeCreator();
        this.setBackground(Color.white);
        addListeners();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < _shapeSet.size(); i++) {
            _shapeSet.get(i).draw(g);
        }
    }

    public void removeCmdHistory() {
        _window.clearCommandsBackup();
    }

    // will have to find a way to parse the file
    public void load() {
        try {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Neal Paint Images", "nap");
            chooser.setFileFilter(filter);
            int returnVal;
            returnVal = chooser.showOpenDialog(_window);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                setCurrentSet((ArrayList<BasicShape>) in.readObject());
                in.close();
                fileIn.close();
                setBackupSet(_shapeSet);
                removeCmdHistory();
                refreshCanvas();
            }


        } catch (Exception ex) {
            System.out.println("Loading error, StackTrace:");
            ex.printStackTrace();
        }
    }

    // will have to redo the file formatting to make it easier to parse
    public void save() {
        if (!_shapeSet.isEmpty()) {
            try {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Neal Paint Images", "nap");
                chooser.setFileFilter(filter);
                int returnVal;
                returnVal = chooser.showSaveDialog(_window);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    if (!chooser.getSelectedFile().getAbsolutePath().endsWith(".nap")) {
                        file = new File(chooser.getSelectedFile() + ".nap");
                    }
                    FileOutputStream fileOut = new FileOutputStream(file);
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(_shapeSet);
                    out.close();
                    fileOut.close();
                }
            } catch (Exception ex) {
                System.out.println("Saving error, StackTrace:");
                ex.printStackTrace();
            }
        }
    }

    public void exit() {
        // maybe clear some stuff before executing the line below?
        System.exit(0);
    }

    public void copy() {
        try {
            clearCopyBuffer();
            BasicShape tempBS = null;
            for (BasicShape bs : _shapeSet) {
                if (bs.getSelected()) {
                    if (bs instanceof Circle) {
                        tempBS = new Circle((Circle) bs);
                    } else if (Line.class == bs.getClass()) {
                        tempBS = new Line((Line) bs);
                    } else if (Triangle.class == bs.getClass()) {
                        tempBS = new Triangle((Triangle) bs);
                    } else if (Rectangle.class == bs.getClass()) {
                        tempBS = new Rectangle((Rectangle) bs);
                    } else if (Group.class == bs.getClass()) {
                        tempBS = new Group((Group) bs);
                    }
                    if (tempBS != null) {
                        _copyBuffer.add(tempBS);
                    }
                    System.out.println(bs + "copied\n");
                }
            }
        } catch (Exception ex) {
            System.out.println("Copying error, StackTrace:");
            ex.printStackTrace();
        }
    }

    public void paste() {
        try {
            ArrayList<BasicShape> temp = new ArrayList<>();
            if (_copyBuffer != null) {
                for (BasicShape bs : _copyBuffer) {
                    bs.moveShape(-bs._pointSet.get(0).x, -bs._pointSet.get(0).y);
                    temp.add(bs);
                }
                _shapeSet.addAll(temp);
                refreshCanvas();
            }
        } catch (Exception ex) {
            System.out.println("Pasting error, StackTrace:");
            ex.printStackTrace();
        }
    }

    public void undoPaste() {
        try {
            if (_shapeSet != null && _copyBuffer != null) {
                _shapeSet.removeAll(_copyBuffer);
            }
            refreshCanvas();
        } catch (Exception ex) {
            System.out.println("Undoing paste error, StackTrace:");
            ex.printStackTrace();
        }
    }

    //remove the last drawing done
    public void undoDraw() {
        if (_shapeSet.size() > 0) {
            int i = _shapeSet.size() - 1;
            _shapeSet.remove(_shapeSet.get(i));
            repaint();
        }
    }

    public ArrayList<BasicShape> getCurrentSet() {
        return _shapeSet;
    }

    public void setCurrentSet(ArrayList<BasicShape> source) {
        _shapeSet = new ArrayList<>(source);
        repaint();
    }

    public ArrayList<BasicShape> getBackupSet() {
        return _backup;
    }

    public void setBackupSet(ArrayList<BasicShape> source) {
        _backup = new ArrayList<>(source);
        repaint();
    }

    public ArrayList<BasicShape> getCBuffer() {
        return _copyBuffer;
    }

    public void setCBuffer(ArrayList<BasicShape> source) {
        _copyBuffer = new ArrayList<>(source);
        repaint();
    }

    public ArrayList<BasicShape> getCBufferBackup() {
        return _copyBufferBackup;
    }

    public void setCBufferBackup(ArrayList<BasicShape> source) {
        _copyBufferBackup = new ArrayList<>(source);
        repaint();
    }

    public void clearCopyBuffer() {
        _copyBuffer.clear();
    }

    public void refreshCanvas() {
        repaint();
    }

    private void addListeners() {
        //this gets the initial click of the mouse
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                _point = new Point(e.getX(), e.getY());
            }
        });

        /**
         * This will get the release of the mouse button and draw a shape using
         * the clicked point and the released point based on what was selected
         */
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (_state == PanelState.DRAW) {
                    drawShape(e);
                } else if (_state == PanelState.SELECT) {
                    selectShapes(e);
                } else if (_state == PanelState.RESIZE) {
                    resizeShape(e);
                } else if (_state == PanelState.MOVE) {
                    moveShape(e);
                }

                repaint();
            }
        });
    }

    private void moveShape(MouseEvent e) {
        int n = _shapeSet.size();
        int x = e.getX();
        int y = e.getY();

        for (int i = 0; i < n; i++) {
            if (_shapeSet.get(i)._selected) {
                _shapeSet.get(i).moveShape(x - _shapeSet.get(i)._pointSet.get(0).x, y - _shapeSet.get(i)._pointSet.get(0).y);
            }
        }
    }

    private void resizeShape(MouseEvent e) {
    }

    private void drawShape(MouseEvent e) {
        BasicShape shape;

        switch (_window.getShapeType()) {
            case "Rectangle":
                shape = _rectangleFactory.createShape(_point, e, _window);
                break;
            case "Triangle":
                shape = _triangleFactory.createShape(_point, e, _window);
                break;
            case "Circle":
                shape = _circleFactory.createShape(_point, e, _window);
                break;
            case "Line":
                shape = _lineFactory.createShape(_point, e, _window);
                break;
            default:
                shape = _lineFactory.createShape(_point, e, _window);
                break;
        }
        _shapeSet.add(shape);
        _window.addCommand(new DrawCommand((DrawPanel) e.getComponent()));
        _window.clearBackup();
    }

    private void selectShapes(MouseEvent e) {
        int smallX, bigX, smallY, bigY;

        if (e.getX() < _point.x) {
            bigX = _point.x;
            smallX = e.getX();
        } else {
            bigX = e.getX();
            smallX = _point.x;
        }

        if (e.getY() < _point.y) {
            bigY = _point.y;
            smallY = e.getY();
        } else {
            bigY = e.getY();
            smallY = _point.y;
        }

        for (int i = 0; i < _shapeSet.size(); i++) {
            ArrayList<Point> pointSet = _shapeSet.get(i)._pointSet;

            if (_shapeSet.get(i)._selected) {
                _shapeSet.get(i).toggleSelected();
            }

            for (int j = 0; j < pointSet.size(); j++) {
                int x = pointSet.get(j).x;
                int y = pointSet.get(j).y;

                if (x < bigX && x > smallX && y < bigY && y > smallY) {
                    _shapeSet.get(i).toggleSelected();
                    System.out.println("Shape " + i + " Selected: " + _shapeSet.get(i)._selected);
                    j = pointSet.size();
                }
            }
        }
    }

    public void groupShapes() {
        Group group = new Group();
        int n = _shapeSet.size();
        int removed = 0;

        for (int i = 0; i < n; i++) {
            if (_shapeSet.get(i - removed)._selected) {
                _shapeSet.get(i - removed).toggleSelected();
                group.add(_shapeSet.get(i - removed));
                _shapeSet.remove(i - removed);
                removed++;
            }
        }

        _shapeSet.add(group);
    }

    public void unGroupShapes() {
        int n = _shapeSet.size();

        for (int i = 0; i < n; i++) {
            if (_shapeSet.get(i)._selected && _shapeSet.get(i) instanceof Group) {
                ArrayList<BasicShape> adding = _shapeSet.get(i).getChildren();
                _shapeSet.remove(i);

                for (int j = 0; j < adding.size(); j++) {
                    _shapeSet.add(adding.get(j));
                }
            }
        }
    }

    public void changeState(PanelState state) {
        _state = state;
    }
}