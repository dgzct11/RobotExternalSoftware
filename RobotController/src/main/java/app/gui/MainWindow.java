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

import app.networking.NetClient;


public class MainWindow {
     
     TrajectoryPlanning trajectoryPlanning;
 
    public void display() {
        NetClient net = new NetClient();
        net.run();
        trajectoryPlanning = new TrajectoryPlanning(net);
     
        
        trajectoryPlanning.display();
       
    }
}