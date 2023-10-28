
package java2ddrawingapplication;

import javax.swing.JFrame;


public class Java2dDrawingApplication
{
    
    public static void main(String[] args)
    {
        DrawingApplicationFrame frame = new DrawingApplicationFrame();
        frame.setTitle("Java 2D Drawings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650,500);
        frame.setVisible(true);
       
    }
    
}
