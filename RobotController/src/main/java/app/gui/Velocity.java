package app.gui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.security.Guard;
import java.util.ArrayList;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    TrajectoryPlanning trajectory;
    public void setTrajectory(TrajectoryPlanning trajectoryPlanning){
        trajectory = trajectoryPlanning;
    }
    public void display(){
        frame = new JFrame();
        frame.setTitle("Velocity Planning");
        GUIConstants.velocityPlanningY = GUIConstants.trajectoryPlanningY + GUIConstants.trajectoryPlanningHeight;
        GUIConstants.velocityPlanningWidth = GUIConstants.trajectoryPlanningWidth;

        panel = new VPanel(trajectory);
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

 
    public TrajectoryPlanning trajectory;
    public ArrayList<int[][]>  lines = new ArrayList<int[][]>();
    public ArrayList<double[]> points = new ArrayList<double[]>();
    public Kinematics kinematics;

    String totalTime;
    String totalDistance;


    public VPanel(TrajectoryPlanning trajectoryPlanning) {
        trajectory = trajectoryPlanning;
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        mode = "point distance";
        double[] point = {0,0};
        points.add(point);
        double[] point1 = {1,0};
        points.add(point1);

     
    }

    @Override
    public void paint(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;
        
        g.setBackground(GUIConstants.velocityPlanningColor);
        g.drawString("Total Time: ", GUIConstants.timeX, GUIConstants.timeY);
        g.drawString("Total Distance: " + points.get(1)[0], GUIConstants.distanceX, GUIConstants.distanceY);

        //draw axis
        g.setColor(GUIConstants.velocityAxisColor);
        g.drawLine(GUIConstants.velocityAxisOffset, GUIConstants.velocityPlanningHeight - GUIConstants.velocityAxisOffset, GUIConstants.velocityPlanningWidth-GUIConstants.velocityAxisOffset,  GUIConstants.velocityPlanningHeight - GUIConstants.velocityAxisOffset);
        g.drawLine(GUIConstants.velocityAxisOffset, GUIConstants.velocityPlanningHeight - GUIConstants.velocityAxisOffset, GUIConstants.velocityAxisOffset,   GUIConstants.velocityAxisOffset);
   
        //draw Dots

        g.setColor(GUIConstants.velocityDotsColor);
        for(double[] point: points){
            g.fillOval(xToGX(point[0]) -  GUIConstants.velocityDotRadius/2, yToGY(point[1]) -  GUIConstants.velocityDotRadius/2, GUIConstants.velocityDotRadius, GUIConstants.velocityDotRadius);
        }

        //draw lines
        g.setColor(GUIConstants.velocityLineColor);
        g.setStroke(GUIConstants.velocityLineStroke);
        for(int i = 1; i<points.size(); i++){
            g.drawLine(xToGX(points.get(i-1)[0]), yToGY(points.get(i-1)[1]), xToGX(points.get(i)[0]), yToGY(points.get(i)[1]));
        }
        updateHover(g);

    }

    public int xToGX(double x){
        if(trajectory.panel.path == null){
            if(x == 0){
                return GUIConstants.velocityAxisOffset;
            }
            return (int)(GUIConstants.velocityMaxX) + GUIConstants.velocityAxisOffset;
        }
        
        return (int)(x/trajectory.panel.path.totalDistance * GUIConstants.velocityMaxX + GUIConstants.velocityAxisOffset);
    }
    public int yToGY(double y){
        return (int)(GUIConstants.velocityPlanningHeight  - GUIConstants.velocityAxisOffset - y/GUIConstants.maxVelocity * (GUIConstants.velocityPlanningHeight-GUIConstants.velocityAxisOffset));
    }
    public void update(){
        repaint();
    }
    public void updateFinalDistance(){
        points.get(points.size()-1)[0] = trajectory.panel.path.totalDistance;
    }

    public void updateHover(Graphics g){
        if(mode.equals("point distance")){
            g.setColor(GUIConstants.velocityHoverPoint);
            g.fillOval(mousePos[0] - GUIConstants.velocityDotRadius/2, yToGY(0) - GUIConstants.velocityDotRadius/2, GUIConstants.velocityDotRadius, GUIConstants.velocityDotRadius);
        }
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
        mousePos[0] = e.getX();
        mousePos[1] = e.getY();
        repaint();
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
