package app.gui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.util.ArrayList;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import app.gui.trajectory.Kinematics;
import app.gui.trajectory.Line;
import app.gui.trajectory.M;
import app.gui.trajectory.Path;
import app.gui.trajectory.Segment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics2D;
public class Velocity extends JFrame{
    JFrame frame;
    VPanel panel;
    public void display(){
        frame = new JFrame();
        frame.setTitle("Velocity Planning");
        GUIConstants.velocityPlanningY = GUIConstants.trajectoryPlanningY + GUIConstants.trajectoryPlanningHeight;
        GUIConstants.velocityPlanningWidth = GUIConstants.trajectoryPlanningWidth;

        panel = new VPanel();
        frame.add(panel);
            
        frame.setSize(panel.getPreferredSize());
        frame.setLocation(GUIConstants.velocityPlanningX, GUIConstants.velocityPlanningY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
}


 class VPanel extends JPanel implements MouseInputListener, KeyListener{
  
    public String mode;
    public int[] mousePos = new int[2];

 
   
    public ArrayList<int[][]>  lines = new ArrayList<int[][]>();
    public ArrayList<double[]> points = new ArrayList<double[]>();
    public Kinematics kinematics;


    public VPanel() {
   
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        mode = "start";
    }

    @Override
    public void paint(Graphics g1) {
        
        Graphics2D g = (Graphics2D) g1;
        g.setBackground(GUIConstants.velocityPlanningColor);
        
        //draw axis
        g.setColor(GUIConstants.velocityAxisColor);
        g.drawLine(GUIConstants.velocityAxisOffset, GUIConstants.velocityPlanningHeight - GUIConstants.velocityAxisOffset, GUIConstants.velocityPlanningWidth-GUIConstants.velocityAxisOffset,  GUIConstants.velocityPlanningHeight - GUIConstants.velocityAxisOffset);
        g.drawLine(GUIConstants.velocityAxisOffset, GUIConstants.velocityPlanningHeight - GUIConstants.velocityAxisOffset, GUIConstants.velocityAxisOffset,   GUIConstants.velocityAxisOffset);
   
    }
  
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(GUIConstants.velocityPlanningWidth, GUIConstants.velocityPlanningHeight);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    
}
