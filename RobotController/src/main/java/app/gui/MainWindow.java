package app.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class MainWindow {
     SubsystemControlPanel controlPanel;
     TrajectoryPlanning trajectoryPlanning;
    Velocity velocity;
    public void display() {
        trajectoryPlanning = new TrajectoryPlanning();
        controlPanel = new SubsystemControlPanel();
        velocity = new Velocity();
       
        trajectoryPlanning.setVelocity(velocity);
        trajectoryPlanning.setControlPanel(controlPanel);
        velocity.setTrajectory(trajectoryPlanning);
        controlPanel.setTrajectory(trajectoryPlanning);
        
        trajectoryPlanning.display();
        velocity.display();
        controlPanel.display();
    }
}