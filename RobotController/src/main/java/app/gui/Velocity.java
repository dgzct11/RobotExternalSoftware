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
        frame.pack();
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

    int currentIndex = 1;


    public VPanel(TrajectoryPlanning trajectoryPlanning) {
        trajectory = trajectoryPlanning;
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        mode = "point distance";
        double[] point = {0,0};
        points.add(point);
        double[] point1 = {1,0};
        points.add(point1);
        double[] point2 = {1,0};
        points.add(point2);
        this.setBackground(GUIConstants.velocityPlanningColor);
     
    }

    @Override
    public void paint(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;
        
        g.setBackground(GUIConstants.velocityPlanningColor);

        g.setColor(GUIConstants.velocityTextColor);
        g.drawString("Total Time: " + (kinematics == null ? "":kinematics.getTotalTime()), GUIConstants.timeX, GUIConstants.timeY);
        g.drawString("Total Distance: " + points.get(points.size()-1)[0], GUIConstants.distanceX, GUIConstants.distanceY);

        //draw axis
        g.setColor(GUIConstants.velocityAxisColor);
        g.setStroke(GUIConstants.velocityAxisStroke);
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
        trajectory.panel.repaint();
        displayAccelerations(g);
        sortPoints();
    }
    public int[] arrToG(double[] point){
        int[] result = {xToGX(point[0]), yToGY(point[1])};
        return result;
    }
    public int[] gToArr(int[] mp){
        int[] result = {(int)gxToX(mp[0]), (int)gxToX(mp[1])};
        return result;
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
    public double gxToX(double gx){
        if(trajectory.panel.path == null){
            return 0;
        }
        return (gx-GUIConstants.velocityAxisOffset) /GUIConstants.velocityMaxX * trajectory.panel.path.totalDistance;
    }
    public double gyToY(double gy){
        if(trajectory.panel.path == null)
            return 0;
        return  (GUIConstants.velocityPlanningHeight  - GUIConstants.velocityAxisOffset -gy) * GUIConstants.maxVelocity / (GUIConstants.velocityPlanningHeight-GUIConstants.velocityAxisOffset);
    }
    
    public int yToGY(double y){
        return (int)(GUIConstants.velocityPlanningHeight  - GUIConstants.velocityAxisOffset - y/GUIConstants.maxVelocity * (GUIConstants.velocityPlanningHeight-GUIConstants.velocityAxisOffset));
    }
    public void update(){
        repaint();
    }
    public void updateFinalDistance(){
        if(trajectory.panel.path == null) return;
        points.get(points.size()-1)[0] = trajectory.panel.path.totalDistance;
        double[][] v = new double[points.size()][2];
        for(int i = 0; i<v.length; i++){
            v[i] = points.get(i);
        }
        kinematics = new Kinematics(trajectory.panel.path, v);
    }

    public void sortPoints(){
        for(int i = 0; i<currentIndex; i++){
            if(points.get(i)[0]>points.get(currentIndex)[0]){
                double[] currentPoint = points.remove(currentIndex);
                currentIndex = i;
                points.add(i, currentPoint);
                break;
            }
        }
        for(int i = currentIndex+1; i<points.size(); i++){
            if(points.get(i)[0]<points.get(currentIndex)[0]){
                double[] currentPoint = points.remove(currentIndex);
                currentIndex = i;
                points.add(i, currentPoint);
                break;
            }
        }
    }//asdfhas;lkdjfh
    public void updateHover(Graphics g){
        if(mode.equals("point distance")){
            double[] point = {gxToX(mousePos[0]), 0};
            if(trajectory.panel.path != null && point[0]<trajectory.panel.path.totalDistance && point[0]>0)
                points.set(currentIndex, point);
        }
        else if(mode.equals("point velocity")){
            points.get(currentIndex)[1] = gyToY(mousePos[1]);
            
            double[][] v = new double[points.size()][2];
            for(int i = 0; i<v.length; i++){
                v[i] = points.get(i);
            }
            kinematics = new Kinematics(trajectory.panel.path, v);
            displayAccelerations(g);
        }
        else if(mode.equals("edit")){
            int index = 1;
            double minDistance = M.distance(points.get(1),gToArr(mousePos));
            for(int i = 1; i<points.size()-1; i++){
                if(M.distance(points.get(i),gToArr(mousePos)) < minDistance){
                    index = i;
                    minDistance = M.distance(points.get(i),gToArr(mousePos));
                }
            }
            int[] point = arrToG( points.get(index));
            
          
            g.setColor(GUIConstants.velocityHighlightColor);
            g.fillOval(point[0]-GUIConstants.highlightRadius/2, point[1]  - GUIConstants.highlightRadius/2, GUIConstants.highlightRadius, GUIConstants.highlightRadius);
         
            g.setColor(GUIConstants.velocityDotsColor);
            g.fillOval(point[0]-GUIConstants.dotRadius/2, point[1]  - GUIConstants.dotRadius/2, GUIConstants.dotRadius, GUIConstants.dotRadius);
     
        }
        else if(mode.equals("edit drag")){
            int index = 1;
            double minDistance = M.distance(points.get(0),gToArr(mousePos));
            for(int i = 1; i<points.size()-1; i++){
                if(M.distance(points.get(i),gToArr(mousePos)) < minDistance){
                    index = i;
                    minDistance = M.distance(points.get(i),gToArr(mousePos));
                }
            }
            double[] point = {gxToX(mousePos[0]), gyToY(mousePos[1])};
            if(point[0]<0 || point[0] > points.get(points.size()-1)[0]) return;
            points.set(index,point);
            currentIndex = index;
            sortPoints();
           
            double[][] v = new double[points.size()][2];
            for(int i = 0; i<v.length; i++){
                v[i] = points.get(i);
            }
            kinematics = new Kinematics(trajectory.panel.path, v);
            displayAccelerations(g);
        }
    }
    public void updateMode(MouseEvent e){
         if(e.getButton() == MouseEvent.BUTTON3){
            if(mode.equals("point distance") || mode.equals("point velocity"))
                points.remove(currentIndex);
            mode = "stop";
           
            return;
        }
        else if(e.getButton() == MouseEvent.BUTTON2){
            mode = "edit";
        }
        else if(mode.equals("point distance")){
            mode ="point velocity";
        }
        else if(mode.equals("point velocity")){
            double[][] v = new double[points.size()][2];
            for(int i = 0; i<v.length; i++){
                v[i] = points.get(i);
            }
            kinematics = new Kinematics(trajectory.panel.path, v);
            double[] point = {0,0};
            points.add(currentIndex+1,point);
            currentIndex ++;
            
            mode = "point distance";
        }
       
        else if(mode.equals("stop")){
            mode = "point distance";
            double[] point = {gxToX(mousePos[0]), 0};
            points.add(currentIndex, point);
        }
        else if(mode.equals("edit")){
           mode = "edit drag";
        }
        else if(mode.equals("edit drag")){
            mode = "edit";
        }
       
    }
    public void displayAccelerations(Graphics g){
        if(kinematics == null) return;
        for(int i = 0; i<kinematics.segments.size(); i++){
            if(kinematics.segments.get(i).acceleration > 0)
                g.drawString(String.format("%f m/s^2", kinematics.segments.get(i).acceleration), xToGX((points.get(i+1)[0]+points.get(i)[0])/2), yToGY((points.get(i+1)[1]+points.get(i)[1])/2)+20);
            else
                g.drawString(String.format("%f m/s^2", kinematics.segments.get(i).acceleration), xToGX((kinematics.velocities[i+1][0]+kinematics.velocities[i][0])/2), yToGY((kinematics.velocities[i+1][1]+kinematics.velocities[i][1])/2)-10);
            if(i<kinematics.segments.size()-1){
                int x = xToGX(kinematics.segments.get(i).end[0]) + 10;
                int y = yToGY(kinematics.segments.get(i).end[1]);
                if(kinematics.segments.get(i+1).acceleration > 0) y += 15;
                else y -= 6;
                g.drawString(String.format("%f m/s", kinematics.segments.get(i).end[1]), x, y);
            }
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
        mousePos[0] = e.getX();
        mousePos[1] = e.getY();
        updateMode(e);
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
        mousePos[0] = e.getX();
        mousePos[1] = e.getY();
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        mousePos[0] = e.getX();
        mousePos[1] = e.getY();
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
