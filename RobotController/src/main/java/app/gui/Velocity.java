package app.gui;

import javax.swing.JFrame;

public class Velocity extends JFrame{
    JFrame frame;
    public void display(){
        frame = new JFrame();
        frame.setTitle("Velocity Planning");
        GUIConstants.velocityPlanningY = GUIConstants.trajectoryPlanningY + GUIConstants.trajectoryPlanningHeight;
        GUIConstants.velocityPlanningWidth = GUIConstants.trajectoryPlanningWidth;
        
        frame.setSize(GUIConstants.velocityPlanningWidth, GUIConstants.velocityPlanningHeight);
        frame.setLocation(GUIConstants.velocityPlanningX, GUIConstants.velocityPlanningY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
}
