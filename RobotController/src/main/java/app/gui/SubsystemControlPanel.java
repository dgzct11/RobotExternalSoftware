package app.gui;

import javax.swing.JFrame;

public class SubsystemControlPanel extends JFrame{
    JFrame frame;
    public void display(){
        frame = new JFrame();
        frame.setTitle("Subsystem Control Panel");
        frame.setSize(GUIConstants.controlPanelWidth, GUIConstants.controlPanelHeight);

        GUIConstants.controlPanelX = GUIConstants.trajectoryPlanningX + GUIConstants.trajectoryPlanningWidth;
        GUIConstants.controlPanelY = GUIConstants.trajectoryPlanningY;

        
        frame.setLocation(GUIConstants.controlPanelX, GUIConstants.controlPanelY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
}



