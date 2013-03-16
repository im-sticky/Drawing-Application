package anpaint;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

//singleton pattern
//this class will handle the application window and the button events
public class AppWindow extends JFrame {
    //single instance, used globally
    private static AppWindow _instance;
    //data used within the class
    String[] _shapes = { "Line", "Rectangle", "Circle", "Triangle" };
    String[] _colours = { "Black", "Red", "Green", "Blue" };
    String[] _weight = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
    String[] _style = { "Solid", "Dashed" };

    //singleton related methods
    private AppWindow() {
        buildFrame();
        buildMenu();
        buildToolbar();

        //pack();
    }

    public static AppWindow getInstance() {
        if (_instance == null)
            _instance = new AppWindow();
        return _instance;
    }

    //window constructing methods
    private void buildFrame() {
        //the window positioning values
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int)(dim.width * 0.8);
        int width = (int)(dim.height * 0.8);

        //set all the frame properties
        this.setTitle("AN Paint");
        this.setSize(height, width);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setVisible(true);
        this.setLocation((dim.width-(this.getSize().width))/2, (dim.height-(this.getSize().height))/2);
    }

    private void buildMenu() {
        JMenuBar _menuBar;
        JMenu _file, _edit;
        JMenuItem _save, _load, _exit, _copy, _paste, _undo, _redo;
        //creating the bar at the top
        _menuBar = new JMenuBar();
        _file = new JMenu("File");
        _file.setMnemonic(KeyEvent.VK_F);
        _edit = new JMenu("Edit");
        _edit.setMnemonic(KeyEvent.VK_E);
        _menuBar.add(_file);
        _menuBar.add(_edit);

        //creating and adding the items to the file menu
        _save = new JMenuItem("Save");
        _save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        _load = new JMenuItem("Open");
        _load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        _exit = new JMenuItem("Exit");
        _exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        _file.add(_save);
        _file.add(_load);
        _file.add(_exit);

        //creating and adding the items to the edit menu
        _copy = new JMenuItem("Copy");
        _copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        _paste = new JMenuItem("Paste");
        _paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        _undo = new JMenuItem("Undo");
        _undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        _redo = new JMenuItem("Redo");
        _redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        _edit.add(_copy);
        _edit.add(_paste);
        _edit.add(_undo);
        _edit.add(_redo);

        //add menu to the frame
        this.setJMenuBar(_menuBar);
    }

    private void buildToolbar() {
        JToolBar _toolBar;
        JComboBox _shapesDDL;
        JComboBox _colourDDL;
        JComboBox _lineWeightDDL;
        JComboBox _lineStyleDDL;
        JButton _shapeTool;
        JButton _group;
        JButton _ungroup;
        JButton _selectionTool;

        //creating the toolbar
        _toolBar = new JToolBar("Tool Palette");
        _toolBar.setLayout(new BoxLayout(_toolBar, BoxLayout.Y_AXIS));
        _toolBar.setFloatable(false);
        _toolBar.setRollover(true);

        //creating and adding the objects the the toolbar
        _toolBar.add(Box.createRigidArea(new Dimension(0,20)));
        //selection Tool
        _selectionTool = new JButton("Selection Tool");
        _selectionTool.setAlignmentX(Component.CENTER_ALIGNMENT);
        _toolBar.add(_selectionTool);
        _toolBar.add(Box.createRigidArea(new Dimension(0,35)));

        //shape tool
        JLabel tempLbl = new JLabel("Shape Options");
        tempLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        _toolBar.add(tempLbl);
        _toolBar.add(Box.createRigidArea(new Dimension(0,20)));

        //shape options
        _shapesDDL = new JComboBox(_shapes);
        _shapesDDL.setSelectedIndex(0);
        setMaxSize(_shapesDDL);
        _toolBar.add(_shapesDDL);
        _toolBar.add(Box.createRigidArea(new Dimension(0,5)));

        _colourDDL = new JComboBox(_colours);
        _colourDDL.setSelectedIndex(0);
        setMaxSize(_colourDDL);
        _toolBar.add(_colourDDL);
        _toolBar.add(Box.createRigidArea(new Dimension(0,5)));

        _lineWeightDDL = new JComboBox(_weight);
        _lineWeightDDL.setSelectedIndex(0);
        setMaxSize(_lineWeightDDL);
        _toolBar.add(_lineWeightDDL);
        _toolBar.add(Box.createRigidArea(new Dimension(0,5)));

        _lineStyleDDL = new JComboBox(_style);
        _lineStyleDDL.setSelectedIndex(0);
        setMaxSize(_lineStyleDDL);
        _toolBar.add(_lineStyleDDL);
        _toolBar.add(Box.createRigidArea(new Dimension(0,20)));

        _shapeTool = new JButton("Shape Tool");
        _shapeTool.setAlignmentX(Component.CENTER_ALIGNMENT);
        _toolBar.add(_shapeTool);
        _toolBar.add(Box.createRigidArea(new Dimension(0,35)));

        //grouping options
        tempLbl = new JLabel("Gouping Options");
        tempLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        _toolBar.add(tempLbl);
        _toolBar.add(Box.createRigidArea(new Dimension(0,20)));

        _group = new JButton("Group");
        _group.setAlignmentX(Component.CENTER_ALIGNMENT);
        _toolBar.add(_group);

        _ungroup = new JButton("Ungroup");
        _ungroup.setAlignmentX(Component.CENTER_ALIGNMENT);
        _toolBar.add(_ungroup);

        //add the toolbar to the panel within the frame
        this.add(_toolBar, BorderLayout.WEST);
    }

    //changes the maximum size of a java component, so it doesn't stretch to fill the layout
    private void setMaxSize(JComponent jc)
    {
        Dimension max = jc.getMaximumSize();
        Dimension pref = jc.getPreferredSize();
        max.height = pref.height;
        jc.setMaximumSize(max);
    }
}