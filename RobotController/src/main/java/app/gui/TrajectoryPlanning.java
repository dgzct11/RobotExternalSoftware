package app.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import javax.swing.event.MouseInputListener;
import java.awt.geom.Path2D;
import java.awt.geom.AffineTransform;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import app.gui.control_panel.SCSetPoint;
import app.gui.trajectory.Circle;
import app.gui.trajectory.Line;
import app.gui.trajectory.M;
import app.gui.trajectory.Path;
import app.gui.trajectory.Segment;
import app.networking.NetClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics2D;
public class TrajectoryPlanning extends JFrame{
    JFrame frame;
    Panel panel;
    NetClient net;
    public static String currentPath = Paths.get("").toAbsolutePath().toString();
    public static String fs = File.separator;
    public TrajectoryPlanning(NetClient n){
        net = n;
    }
    
    public void display(){
        frame = new JFrame();
        frame.setTitle("Field");
       
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new Panel("./RobotController" + fs + "resources" + fs + "Commons_drawing.png", net);
        frame.add(panel);
        panel.setFocusable(true);
        frame.setFocusable(true);
     
        frame.requestFocusInWindow();
        panel.requestFocusInWindow();
        System.out.printf("position: %d %d", panel.getWidth(), panel.getHeight());
        System.out.println(panel.getPreferredSize());
       

       
        frame.setSize(panel.getPreferredSize());
       
      
       
        frame.pack();
      
        frame.setLocation(GUIConstants.trajectoryPlanningX, GUIConstants.trajectoryPlanningY);
        frame.setVisible(true);
        GUIConstants.trajectoryPlanningWidth = frame.getWidth();
        
        GUIConstants.trajectoryPlanningHeight = frame.getHeight();
    }
  

    
}


//__________________________________________________________________


class Panel extends JPanel{
    public Image fieldImage;
    NetClient net;
    public double[] robotPos = new double[2];
    public Panel(String path, NetClient n) {
       
        fieldImage = Toolkit.getDefaultToolkit().getImage(path);
        net = n;
        
    }

    @Override
    public void paint(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;
        g.drawImage(fieldImage, 0, 0, null);

     
       
        drawRobot(g);
        repaint();
    }
    public int getHeight(){
        return fieldImage.getHeight(null);
    }
    public int getWidth(){
        return fieldImage.getWidth(null);
    }
    @Override
    public Dimension getPreferredSize() {
        
        return new Dimension(fieldImage.getWidth(null), fieldImage.getHeight(null));
    }

   

   
    public void drawRobot(Graphics g){
     
        int[] pos = M.metersToPixelsInt(net.getPose());
        
        g.setColor(GUIConstants.robotColor);
        
        g.fillRect(pos[0] - (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter/2), pos[1] - (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter/2), (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter), (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter));
     
        repaint(pos[0] - (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter/2), pos[1] - (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter/2), (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter), (int)(GUIConstants.robotWidth*GUIConstants.pixels_per_meter));
    }
   
  
    public void drawArrow(Graphics2D g, double x, double y, double angle){
        Arrow arrow = new Arrow();
        
        AffineTransform oldXForm = g.getTransform();    
        angle = -angle;
        AffineTransform at = new AffineTransform();
        at.translate(2*(x), 2*(y- arrow.getBounds().getHeight()/4));
        at.rotate(Math.toRadians(angle), 0, arrow.getBounds().getHeight()/2);
        g.setTransform(at);
        
        g.setStroke(GUIConstants.arrowStroke);
        g.draw(arrow);
        g.setTransform(oldXForm);
       
    }



    
   

   
}



class Arrow extends Path2D.Double {

    public Arrow() {
        double arrow_breadth = 36;
        double length = 100;
        double arrow_length = 40;
        moveTo(0, arrow_breadth/2);
        lineTo(length, arrow_breadth/2);
        
        moveTo(length - arrow_length, 0);
        lineTo(length, arrow_breadth/2);
        moveTo(length - arrow_length, arrow_breadth);
        lineTo(length, arrow_breadth/2);
        
    }

}