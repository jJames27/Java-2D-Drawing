
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;


public class DrawingApplicationFrame extends JFrame
{

    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.
    private final JPanel panelTop;
    private final JPanel panelTopLine1;
    private final JPanel panelTopLine2;

    // create the widgets for the firstLine Panel.
    private final JComboBox shapesCombo;
    private final JButton firstColor;
    private final JButton secondColor;
    private final JButton clear; 
    private final JButton undo; 
    
    //create the widgets for the secondLine Panel.
    private final JCheckBox filled;
    private final JCheckBox gradient;
    private final JCheckBox dashed;
    private final JLabel lineWidth;
    private final JLabel dashLength;
    private final JSpinner line;
    private final JSpinner dash;
    
    // Variables for drawPanel.
    private final DrawPanel drawPanel = new DrawPanel();
    private ArrayList<MyShapes> shapesDrawn;
    private Color color1; 
    private Color color2; 

    // add status label
    private final JPanel panelBottom; 
    private final JLabel statusLabel;  
  
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {
        // add widgets to panels  
        // firstLine widgets
        panelTopLine1 = new JPanel();
        panelTopLine1.setLayout(new FlowLayout());
        panelTopLine1.add(new JLabel("Shape: "));
        String shapeChoices[] = { "Line", "Rectangle", "Oval"};
        shapesCombo = new JComboBox(shapeChoices); 
        panelTopLine1.add(shapesCombo);
        shapesDrawn = new ArrayList<>(); 
        firstColor = new JButton("1st Color...");
        
        panelTopLine1.add(firstColor);
        secondColor = new JButton("2nd Color...");
        panelTopLine1.add(secondColor);
        undo = new JButton("Undo");
        panelTopLine1.add(undo);
        clear = new JButton("Clear");
        panelTopLine1.add(clear);
        panelTopLine1.setBackground(Color.CYAN);
        
        // secondLine widgets
        panelTopLine2 = new JPanel();
        panelTopLine2.setLayout(new FlowLayout());
        panelTopLine2.add(new JLabel("Options: "));
        filled = new JCheckBox("Filled");
        panelTopLine2.add(filled);
        gradient = new JCheckBox("Use Gradient");
        panelTopLine2.add(gradient);
        dashed = new JCheckBox("Dashed");
        panelTopLine2.add(dashed);
        lineWidth = new JLabel("Line Width:");
        panelTopLine2.add(lineWidth);
        line = new JSpinner();
        panelTopLine2.add(line);
        dashLength = new JLabel("Dash Length:");
        panelTopLine2.add(dashLength);
        dash = new JSpinner();
        panelTopLine2.add(dash);
        panelTopLine2.setBackground(Color.CYAN);
        
        //status label of coordinates
        panelBottom = new JPanel();
        panelBottom.setLayout(new BorderLayout());
        statusLabel = new JLabel("( , )"); 
        panelBottom.add(statusLabel, BorderLayout.WEST);
        panelBottom.setBackground(Color.lightGray);
        
        // add top panel of two panels
        panelTop= new JPanel(); 
        panelTop.setLayout(new BorderLayout());
        panelTop.add(panelTopLine1, BorderLayout.NORTH);
        panelTop.add(panelTopLine2, BorderLayout.SOUTH); 
        
        // add topPanel to North, drawPanel to Center, and statusLabel to South
        drawPanel.setBackground(Color.WHITE);
        add(panelTop, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER); 
        add(panelBottom, BorderLayout.SOUTH);
        
        //add listeners and event handlers
        color1 = Color.RED;
        color2 = Color.BLACK;
      
        undo.addActionListener((ActionEvent arg0) -> {
            shapesDrawn.remove(shapesDrawn.size() - 1);
            repaint();
            
        });
        clear.addActionListener((ActionEvent clearActionEvent) -> {
            shapesDrawn.clear();
            repaint();
            
        });
        firstColor.addActionListener((ActionEvent event) -> {
            color1 = JColorChooser.showDialog(null, "Choose a color", Color.BLACK);
        });
        secondColor.addActionListener((ActionEvent event) -> {
            color2 = JColorChooser.showDialog(null, "Choose a color", Color.BLACK);
        });
        
    }
     
    // Create event handlers, if needed
    
    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel  
    {
        private MyShapes shapes;
        
        public DrawPanel()
        {
            MouseHandler handler = new MouseHandler();
            addMouseListener(handler);
            addMouseMotionListener(handler);
        }

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            //loop through and draw each shape in the shapes arraylist
            for (MyShapes shapes: shapesDrawn){
                    shapes.draw(g2d); 
            }
            
        }
        
        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {
            private Point point; 
             
            
            public void mousePressed(MouseEvent event)
            {
                Point startPoint = event.getPoint();
                Point endPoint = event.getPoint();
                
                MyShapes newShape = NewShape(startPoint, endPoint);
                shapesDrawn.add(newShape);
                repaint();
            }

            public void mouseReleased(MouseEvent event)
            {
                shapes = null; 
            }

            public void mouseDragged(MouseEvent event)
            {
                shapes.setEndPoint(event.getPoint());
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent event)
            {
                statusLabel.setText(String.format("(%d, %d)", event.getX(),event.getY()));
            }
        }
        // constructor used in MouseHandler to create new shapes
        public MyShapes NewShape(Point startPoint, Point endPoint){
            Paint paint; 
            BasicStroke stroke ;
            if (gradient.isSelected())
            {
            paint = new GradientPaint(0, 0, color1, 50, 50, color2, true);
            }else
                paint = color1; 
            if (dashed.isSelected())
            {
                stroke = new BasicStroke((int)line.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, new float[] {(float)(int)dash.getValue()}, 0);
            } else
            {
                stroke = new BasicStroke((int)line.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            }
            
            String newShapeType = (String)shapesCombo.getSelectedItem();
            
           if (newShapeType.equals("Rectangle")){
                shapes = new MyRectangle(startPoint, endPoint, paint, stroke, filled.isSelected());
            }else if (newShapeType.equals("Line")){
                shapes = new MyLine(endPoint, endPoint, paint, stroke); 
            }else if (newShapeType.equals("Oval")){
                shapes = new MyOval(startPoint, endPoint, paint, stroke, filled.isSelected()); 
        }
            return shapes; 
        }
    }
}
