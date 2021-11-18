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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.MouseInputListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import app.gui.control_panel.SCSetPoint;
import app.gui.trajectory.Kinematics;
import app.gui.trajectory.Line;
import app.gui.trajectory.M;
import app.gui.trajectory.Path;
import app.gui.trajectory.Segment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics2D;
public class SubsystemControlPanel extends JFrame implements ActionListener{
    JFrame frame;
    JPanel buttonPanel;
    TrajectoryPlanning trajectory;
    SCPanel panel;

    JLabel navxTitle = new JLabel("NavX:");
    JButton setDegrees = new JButton("Align Angle");
    JTextField navxDegreeInput = new JTextField("", 10);
    JLabel limelightTitle = new JLabel("LimeLight:");


    public void display(){
        frame = new JFrame();
        frame.setTitle("Subsystem Control Panel");
       

        GUIConstants.controlPanelX = GUIConstants.trajectoryPlanningX + GUIConstants.trajectoryPlanningWidth;
        GUIConstants.controlPanelY = GUIConstants.trajectoryPlanningY;

        buttonPanel = new JPanel();
        addButtons();
        buttonPanel.setPreferredSize(new Dimension(GUIConstants.controlPanelWidth - 2* GUIConstants.controlPanelAxisOffset, GUIConstants.controlPanelHeight));

        panel = new SCPanel(trajectory, this);
       
        frame.add(panel);
        frame.add(buttonPanel);
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(GUIConstants.controlPanelBackground);
        Dimension size =  panel.getPreferredSize();
        Dimension buttonSize = buttonPanel.getPreferredSize();
        panel.setBounds(0, 0, (int)size.getWidth(), (int)size.getHeight());
        buttonPanel.setBounds((int)size.getWidth()+100, 0, (int)buttonSize.getWidth(), (int)buttonSize.getHeight());
        frame.setSize((int)size.getWidth() + (int)buttonSize.getWidth(), (int)size.getHeight());
        frame.pack();
        frame.setLocation(GUIConstants.controlPanelX, GUIConstants.controlPanelY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public void setTrajectory(TrajectoryPlanning trajectoryPlanning){
        trajectory = trajectoryPlanning;
    }
    public void addButtons(){
        navxTitle.setFont(GUIConstants.controlPanelFont);
        navxTitle.setBounds(GUIConstants.controlPanelLeftTextX, GUIConstants.controlPanelAxisOffset, 5+(int)navxTitle.getPreferredSize().getWidth(), 5+(int)navxTitle.getPreferredSize().getHeight());
     
        
        setDegrees.setBounds(GUIConstants.controlPanelButtonsLeftX, GUIConstants.controlPanelAxisOffset + navxTitle.getHeight() + GUIConstants.controlPanelPadding,5+(int)setDegrees.getPreferredSize().getWidth(), 5+(int)setDegrees.getPreferredSize().getHeight() );
        setDegrees.addActionListener(this);
        navxDegreeInput.setBounds(GUIConstants.controlPanelButtonsLeftX + GUIConstants.controlPanelPadding + setDegrees.getWidth(), setDegrees.getY(), 5+(int)navxDegreeInput.getPreferredSize().getWidth(), 5+(int)navxDegreeInput.getPreferredSize().getHeight());
        
        limelightTitle.setFont(GUIConstants.controlPanelFont);
        limelightTitle.setBounds(GUIConstants.controlPanelLeftTextX, GUIConstants.controlPanelAxisOffset + navxTitle.getHeight() + GUIConstants.controlPanelPadding + GUIConstants.controlPanelPadding +  5+(int)setDegrees.getPreferredSize().getHeight() , 5+(int)limelightTitle.getPreferredSize().getWidth(), 5+(int)limelightTitle.getPreferredSize().getHeight());
        //limelightTitle.setBounds(200, 500, 5+(int)limelightTitle.getPreferredSize().getWidth(), 5+(int)limelightTitle.getPreferredSize().getHeight());
     

        navxTitle.setVisible(true);
        setDegrees.setVisible(true);
        navxDegreeInput.setVisible(true);
        limelightTitle.setVisible(true);

        buttonPanel.add(navxTitle);
        buttonPanel.add(setDegrees);
        buttonPanel.add(navxDegreeInput);
        buttonPanel.add(limelightTitle);
        
       
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(trajectory.panel.path == null){
            return;
        }
        if(e.getSource() == setDegrees){
            panel.mode = "first point";
            panel.setPoints.add(new SCSetPoint(0, panel.trajectory.panel.path.totalDistance, "navx"));
            try{
                double angle = Double.parseDouble(navxDegreeInput.getText());
                panel.setPoints.get(panel.setPoints.size()-1).inputs.add(angle);
            }catch(Exception event){
                System.out.println(event);
            }
            
        }
        System.out.println("button clicked");
    }
}
class SCPanel extends JPanel implements MouseInputListener, KeyListener{
  
  
    public ArrayList<SCSetPoint> setPoints = new ArrayList<SCSetPoint>();
    public TrajectoryPlanning trajectory;
    public double[] mousePos = {0,0};
    
    public String mode = "null";

   

    SubsystemControlPanel controlPanel;
    public SCPanel(TrajectoryPlanning trajectoryPlanning, SubsystemControlPanel cp) {
        trajectory = trajectoryPlanning;
        controlPanel = cp;
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    
   this.setBackground(GUIConstants.controlPanelBackground);
    }

    public void addPanelButtons(){
        
       
        
    }
    @Override
    public void paint(Graphics g1) {
        super.paintComponent(g1);
        
        Graphics2D g = (Graphics2D) g1;
       
        g.setColor(GUIConstants.controlPanelAxisColor);
        g.setStroke(GUIConstants.controlPanelAxisStroke);
        g.drawLine(GUIConstants.controlPanelAxisOffset, GUIConstants.controlPanelAxisOffset , GUIConstants.controlPanelAxisOffset,  GUIConstants.controlPanelHeight - GUIConstants.controlPanelAxisOffset);
    
        
        //display points
        for(SCSetPoint point: setPoints){
            g.setColor(GUIConstants.controlPanelDotColor);
            g.fillOval(GUIConstants.controlPanelAxisOffset -  GUIConstants.controlPanelDotRadius/2, (int)yToGY(point.startDistance) -  GUIConstants.controlPanelDotRadius/2, GUIConstants.controlPanelDotRadius, GUIConstants.controlPanelDotRadius);
            g.fillOval(GUIConstants.controlPanelAxisOffset -  GUIConstants.controlPanelDotRadius/2, (int)yToGY(point.endDistance) -  GUIConstants.controlPanelDotRadius/2, GUIConstants.controlPanelDotRadius, GUIConstants.controlPanelDotRadius);
       
        }
      
       updateHover();
    }

    public void updateMode(){
        if(mode.equals("first point")){
            setPoints.get(setPoints.size()-1).startDistance = gyToY(mousePos[1]);
            mode = "second point";
        }
        else if(mode.equals("second point")){
            mode = "null";
        }
    }
    public void updateHover(){
        if(mode.equals("null")) return;
        if(mode.equals("first point")){
            if(gyToY(mousePos[1])>0 && gyToY(mousePos[1])<trajectory.panel.path.totalDistance)
                setPoints.get(setPoints.size()-1).startDistance = gyToY(mousePos[1]);

        }
        else if(mode.equals("second point")){
            if(gyToY(mousePos[1])>setPoints.get(setPoints.size()-1).startDistance && gyToY(mousePos[1])<trajectory.panel.path.totalDistance)
                setPoints.get(setPoints.size()-1).endDistance = gyToY(mousePos[1]);

        }
    }
    public double yToGY(double y){
        return y/trajectory.panel.path.totalDistance * (GUIConstants.controlPanelHeight - 2*GUIConstants.controlPanelAxisOffset) + GUIConstants.controlPanelAxisOffset;
    }
    public double gyToY(double gy){
        return ( gy - GUIConstants.controlPanelAxisOffset ) * trajectory.panel.path.totalDistance / (GUIConstants.controlPanelHeight - 2*GUIConstants.controlPanelAxisOffset);
    }
  
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(GUIConstants.controlPanelAxisOffset*2, GUIConstants.controlPanelHeight);
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
        updateMode();
      repaint();
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




